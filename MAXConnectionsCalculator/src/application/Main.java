package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {

	private int[][] cost, directions;// for DP table
	private int[] LEDs;
	private int n = 0, items = 0;// number of LEDs

	@Override
	public void start(Stage primaryStage) {

		// initializing panes & stages & scenes
		MainPane front = new MainPane();
		TablesPane tablePane = new TablesPane();
		ConnectionPane connect = new ConnectionPane();
		Stage connectionsStage = new Stage();
		Stage tableStage = new Stage();
		Scene connectionsScene = new Scene(connect);
		Scene DPScene = new Scene(tablePane);
		Scene mainScene = new Scene(front, 950, 650);

		// setting scene to stage
		connectionsStage.setScene(connectionsScene);

		front.getReadFromFile().setOnAction(e -> {

			String msg = readTheFile();// returns warning string to report a problem

			if (items != 0 && n != 0) {// some values were valid but not could be not all
				front.getText().setText(seperateWithCommas(LEDs, items));

				front.getNumber().setText(n + "");// setting text to number
				front.getWarning().setText(msg);

				// clearing any existing values in the result
				front.getAnswer().setText("");
				front.getResult().setText("");

				// enable buttons
				front.getRandom().setDisable(false);
				front.getCalculate().setDisable(false);

				// cannot view DP table or LED connection until calculation is complete
				front.getView().setDisable(true);
				front.getSeeConnection().setDisable(true);
				tableStage.close();// closing DP table
				connectionsStage.close();// closing Connections

			} else if (items == 0) {// no valid item was entered at all

				front.getWarning().setText(msg);

				// clearing wrong inputs
				front.getNumber().clear();
				front.getText().clear();

				// disable buttons
				front.getRandom().setDisable(true);
				front.getCalculate().setDisable(true);

			}

		});

		// this button generates a random sequence of LEDs in range of N
		front.getRandom().setOnAction(e -> {
			front.getText().setText(RandomlyGenerateNumbers(n));
			front.getNumber().setText(n + "");

			// clearing any existing values
			front.getAnswer().setText("");
			front.getResult().setText("");

			// cannot view DP table or LED connection until calculation is complete
			front.getView().setDisable(true);
			front.getSeeConnection().setDisable(true);
			tableStage.close();// closing DP table
			connectionsStage.close();// closing Connections

		});

		// every value entered in the text area is checked to make sure it's valid
		front.getText().setOnKeyTyped(e -> {
			front.getText().setText(checkValid(front.getText().getText(), n));
			front.getText().positionCaret(front.getText().getLength());
			front.getView().setDisable(true);
			front.getSeeConnection().setDisable(true);
			tableStage.close();// closing DP table
			connectionsStage.close();// closing Connections

		});

		// this button opens a new stage containing the DP table
		front.getView().setOnAction(e -> {

			// filling the gridPane with a=values and arrows
			tablePane.fillGrid(n, LEDs, cost, directions, (n < 50) ? true : false);

			// opening stage with table
			tableStage.toFront();
			tableStage.setScene(DPScene);
			tableStage.setTitle("Dinamic Programming Table ");
			tableStage.show();

		});

		// takes in the number entered, checks if it's valid, then acts accordingly
		front.getEnter().setOnAction(e -> {

			String Ntext = front.getNumber().getText();

			if (isPositiveInteger(Ntext)) {// when the number entered is approved

				n = Integer.parseInt(Ntext);// setting N to the value given
				LEDs = new int[n];// initializes the LED array with n as length
				front.getWarning().setText("");// clear warning
				front.getText().clear();// empty input area for new values

				// enable buttons
				front.getRandom().setDisable(false);
				front.getCalculate().setDisable(false);

			} else {// give warning

				front.getWarning().setText("Invalid input: N must be positive integer");

				// clearing wrong inputs
				front.getNumber().clear();
				front.getText().clear();

				// disable buttons
				front.getRandom().setDisable(true);
				front.getCalculate().setDisable(true);

			}

			// clearing any existing values
			front.getAnswer().setText("");
			front.getResult().setText("");

			// cannot view DP table until calculation is complete
			front.getView().setDisable(true);
			front.getSeeConnection().setDisable(true);
			tableStage.close();// closing DP table
			connectionsStage.close();// closing Connections

		});

		// this button calculates the result and answer of the best solution
		front.getCalculate().setOnAction(e -> {

			// sets warning to show whether input LED sequence is valid or not(number<n & N
			// numbers entered)
			front.getWarning().setText(fillArray(n, LEDs, front.getText().getText()));

			// setting text area with the current valid LED sequence separated by commas
			front.getText().setText(seperateWithCommas(LEDs, items));

			// if valid inputs equal N numbers, calculate the DP arrays
			if (items == n) {

				front.getWarning().setText("");// clear warning

				// initializing the arrays with length of N
				cost = new int[n + 1][n + 1];
				directions = new int[n][n];

				// filling the cost array
				calculateNumbers(LEDs, cost, directions);

				// calculating solution sequence
				String solution = getSolution(directions, LEDs, n - 1, n - 1);

				// setting the answers to the solution generated
				front.getAnswer().setText(solution.substring(0, solution.length() - 1));
				front.getResult().setText(cost[n][n] + "");

				// can now view the DP table and LED connections
				front.getSeeConnection().setDisable(false);
				front.getView().setDisable(false);

			} // else, don't calculate (not enough correct items entered)

		});

		front.getSeeConnection().setOnAction(e -> {

			connect.fillLEDLights(LEDs);// fills the VBoxes with the Battery and LED sequences

			int[] answers = new int[cost[n][n]];
			fillArray(n, answers, front.getAnswer().getText());// fills the answer array with the
																// answers sequence
			connect.drawLines(LEDs, answers);
			connect.lightUpAnswers(LEDs, answers);// turns on the lights of the answers

			connectionsStage.setTitle("LED Connections");
			connectionsStage.show();
			connectionsStage.toFront();

		});

		// opening the front stage
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Best LED connection calculator");
		primaryStage.show();

	}

	// returns string with numbers separated by commas
	private String seperateWithCommas(int[] numbers, int n) {

		String text = "";
		for (int i = 0; i < n; i++)
			text = text + numbers[i] + ",";

		return text;

	}

	public static void main(String[] args) {
		launch(args);
	}

	// fills the array with N valid inputs from the string separated by commas
	private String fillArray(int n, int[] x, String text) {

		items = 0;// to keep track of accepted values

		if (text == null || text.isBlank()) {
			items = 0;// # of valid items in array is 0
			return ". . . . Empty Input . . . .";// not a valid input
		}

		Scanner scan = new Scanner(text);// scanning text given
		scan.useDelimiter(",");// use comma as delimiter

		int duplicate = 0;// to track whether a duplicate was found
		while (scan.hasNext() && items < n) {// fills in correct values only from text area

			duplicate = 0;// reset duplicate flag
			String numberString = scan.next();// saving number

			if (isPositiveInteger(numberString)) {// string contains integer

				int num = Integer.parseInt(numberString);

				if (num != 0 && num <= n && !isDuplicate(num, x)) {// valid input not duplicated
					x[items] = num;// added to array
					items++;

				} else
					duplicate = 1;
			}
		}

		if (duplicate == 1)// duplicate detected
			return "Detected duplicated numbers. Currently " + (n - items) + " values missing.";

		if (items == n && items != 0) // all numbers are valid and not equal to 0
			return "Input Complete!";

		else// not enough valid inputs
			return "Input Incomplete: There are " + items + " values out of " + n;

	}

	/*
	 * this method prompts the user to choose a file, then read the content as the
	 * first number is the number of LEDs, then the next lines are the LEDs sequence
	 * separated by commas
	 *
	 */
	private String readTheFile() {

		try {

			// choosing file from desktop
			FileChooser fileChooser = new FileChooser();

			// Create a file filter for a specific extension
			ExtensionFilter filter = new ExtensionFilter("Text Files (*.txt)", "*.txt");

			// Set the file filter to the file chooser
			fileChooser.getExtensionFilters().add(filter);

			File selectedFile = fileChooser.showOpenDialog(new Stage());

			Scanner input;
			if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".txt"))
				input = new Scanner(selectedFile);// scanning file

			else
				throw new FileNotFoundException();

			String text = "";// to store the LEDs
			try {

				String j = "";
				if (input.hasNext())// storing n as number of LEDs
					j = input.nextLine();

				if (isPositiveInteger(j)) {// number entered is positive integer
					n = Integer.parseInt(j);// store in n
					LEDs = new int[n];// initialize LED array
				} else
					throw new NumberFormatException("Invalid input: N must be positive integer");

				while (input.hasNextLine()) {// reading line by line O(k)
					Scanner read = new Scanner(input.nextLine());// reading word by word
					read.useDelimiter(",");

					// filling String with values from the line
					while (read.hasNext())
						text = text + read.next() + ",";

				}

			} catch (NumberFormatException e) {
				return "Error Reading File: " + e.getMessage();
			}

			return fillArray(n, LEDs, text);

		} catch (NullPointerException | FileNotFoundException | NoSuchElementException e1) {
			return "Failed To Open File";
		}

	}

	// uses dynamic programming to calculate the longest common sequence
	private void calculateNumbers(int[] y, int[][] cost, int[][] arrow) {

		for (int i = 0; i <= y.length; i++)// first column is all zeros (initial point)
			cost[i][0] = 0;

		for (int j = 0; j <= y.length; j++)// first row is all zeros (initial point)
			cost[0][j] = 0;

		int[] x = new int[y.length];

		for (int i = 1; i <= x.length; i++) {

			x[i - 1] = i;

		}

		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y.length; j++) {

				if (x[i] == y[j]) {// found a match, takes number from diagonal and adds one

					cost[i + 1][j + 1] = cost[i][j] + 1;
					arrow[i][j] = 1;// diagonally

				} else if (cost[i][j + 1] >= cost[i + 1][j]) {// top is greater or equal to left

					cost[i + 1][j + 1] = cost[i][j + 1];
					arrow[i][j] = 2;// up

				} else {// left is greater than top

					cost[i + 1][j + 1] = cost[i + 1][j];
					arrow[i][j] = 0;// left

				}

			}
		}

	}

//	uses the arrow indicators given to trace the answer sequence (returned with commas as separation
	private String getSolution(int[][] arrows, int[] x, int i, int j) {

		if (i == -1 || j == -1)
			return "";// out of bounds

		else {
			if (arrows[i][j] == 1) {// diagonal arrow

				return getSolution(arrows, x, i - 1, j - 1) + x[j] + ",";

			} else if (arrows[i][j] == 2)// up arrow

				return getSolution(arrows, x, i - 1, j);

			else// left arrow
				return getSolution(arrows, x, i, j - 1);

		}

	}

	// generates a random order of integers ranging 1-n without duplicates
	private String RandomlyGenerateNumbers(int n) {

		int[] numbers = new int[n];

		for (int i = 1; i <= n; i++) {// filling array with numbers
			numbers[i - 1] = i;
		}

		Random random = new Random();

		// shuffling the list to get a random order of 1 to n
		for (int i = numbers.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);

			// Swap numbers[i] and numbers[j]
			int temp = numbers[i];
			numbers[i] = numbers[j];
			numbers[j] = temp;
		}

		String answer = "";

		for (int i = 0; i < n; i++)
			answer = answer + numbers[i] + ",";

		return answer.substring(0, answer.length() - 1);

	}

	// checks if number given already existed in the array, to prevent duplicates
	private boolean isDuplicate(int number, int[] x) {

		for (int i = 0; i < items; i++) // looping valid items in array
			if (x[i] == number)
				return true;// this number already exists

		return false;// duplicate not found

	}

	private boolean isPositiveInteger(String text) {

		if (text == null || text.isBlank())
			return false;// not a valid input

		try {
			int r = Integer.parseInt(text);
			if (r > 0)
				return true;// successfully parsed the string = is a valid number

		} catch (NumberFormatException e) {
			// invalid input since parsing failed
			return false;
		}
		return false;

	}

	private String checkValid(String s, int max) {

		StringBuilder st = new StringBuilder(s);

		if (s == null || s.isBlank())
			return "";

		if (st.charAt(0) == ',' || st.charAt(0) == '0') {// remove any comma at beginning
			st.deleteCharAt(0);
			return st.toString();
		}

		// nonDigit or non-comma or zero is an invalid input, must be erased.
		if (st.charAt(st.length() - 1) != ',' && !Character.isDigit(st.charAt(st.length() - 1)))
			st.deleteCharAt(st.length() - 1);

		// Two spaces is not accepted, one is put instead.
		else if (st.length() > 1 && st.charAt(st.length() - 1) == ',' && st.charAt(st.length() - 2) == ',')
			st.deleteCharAt(st.length() - 1);

		return st.toString();

	}

	// i is up down, j is left right, numbering is vertical, light bulb sequence is
	// horizantal

}
