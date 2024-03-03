package application;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainPane extends BorderPane {

	private Label warning;
	private TextField number, result;
	private Button random, calculate, view, enter, seeConnection, readFromFile;
	private TextArea text, answer;

	public MainPane() {

		ImageView img = new ImageView("enter.png");
		img.setScaleX(1.5);
		img.setScaleY(2);

		// initializing nodes
		random = new Button("Randomly Generate N LED Ordering");
		calculate = new Button("Calculate Optimal Solution");
		view = new Button("View DP Tables");
		readFromFile = new Button("Read From a file");
		enter = new Button("", img);
		seeConnection = new Button("See LED Connections");
		number = new TextField("");
		result = new TextField("");// no editing
		text = new TextArea("");
		answer = new TextArea("");// no editing
		warning = new Label("");

		setProperties();

	}

	private void setProperties() {

		Label heading = new Label("	  MAX LED Connections Calculator");
		Label title0 = new Label("Number of LEDs (N):");
		Label title1 = new Label("INPUT: LED Sequence On Connection Board");
		Label title2 = new Label("ANSWER: Best Connection Achieved With LEDs Numbered: ");
		Label title3 = new Label("Max # of lighted LEDs:");

		enter.setContentDisplay(ContentDisplay.TOP);

		// setting width and heights
		random.setPrefSize(310, 40);
		readFromFile.setPrefSize(200, 40);
		calculate.setPrefSize(210, 40);
		seeConnection.setPrefSize(210, 40);
		view.setPrefSize(200, 40);
		number.setPrefSize(200, 40);
		result.setPrefSize(200, 150);
		answer.setPrefSize(610, 150);
		text.setPrefSize(600, 150);

		// to vertically separate two buttons
		Separator separator0 = new Separator(Orientation.VERTICAL);
		separator0.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		// creating the layout of the interface using Vertical and Horizontal Boxes
		HBox top = new HBox(10);
		top.getChildren().addAll(number, enter, separator0, random, readFromFile);
		top.setAlignment(Pos.CENTER_LEFT);

		HBox enterButtons = new HBox(20);
		enterButtons.getChildren().addAll(calculate, view);
		enterButtons.setPadding(new Insets(10, 10, 50, 10));
		enterButtons.setAlignment(Pos.CENTER_LEFT);

		HBox titles = new HBox(100);
		titles.getChildren().addAll(title2, title3);
		titles.setPadding(new Insets(20, 0, 10, 0));
		titles.setAlignment(Pos.CENTER_LEFT);

		HBox bottom = new HBox(30);
		bottom.getChildren().addAll(answer, result);
		bottom.setPadding(new Insets(0, 0, 20, 0));
		bottom.setAlignment(Pos.CENTER_LEFT);

		// to separate inputs from outputs
		Separator separator = new Separator(Orientation.HORIZONTAL);
		separator.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

		VBox middle = new VBox();
		middle.getChildren().addAll(heading, title0, top, title1, text, enterButtons, warning, separator, titles,
				bottom, seeConnection);

		// adding nodes to the current extended border pane
		setCenter(middle);
		setPadding(new Insets(20, 20, 20, 20));
		setStyle("-fx-background-color: rgb(151, 201, 207);");

		// setting initial state of the buttons and text areas
		random.setDisable(true);
		calculate.setDisable(true);
		view.setDisable(true);
		seeConnection.setDisable(true);
		answer.setEditable(false);
		result.setEditable(false);
		text.setWrapText(true);

		// setting the colors of buttons, text areas, and text fields
		random.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 1;");
		readFromFile.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 1;");
		seeConnection.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 1;");
		enter.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 0;");
		view.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 1;");
		calculate.setStyle("-fx-background-color: rgb(36, 160, 179);-fx-border-color:black;-fx-border-width: 1;");
		text.setStyle("-fx-control-inner-background: rgb(213, 241, 245);-fx-border-color:black;-fx-border-width:3;");
		result.setStyle("-fx-background-color: rgb(213, 241, 245);-fx-border-color:black;-fx-border-width:3;");
		answer.setStyle("-fx-control-inner-background: rgb(213, 241, 245);-fx-border-color:black;-fx-border-width:3;");
		number.setStyle("-fx-background-color: rgb(213, 241, 245);-fx-border-color:black;-fx-border-width:3;");

		// setting fonts for the buttons and text fields
		readFromFile.setFont(Font.font("Times New Roman", 17));
		text.setFont(Font.font("Times New Roman", 22));
		result.setFont(Font.font("Times New Roman", 22));
		answer.setFont(Font.font("Times New Roman", 22));
		number.setFont(Font.font("Times New Roman", 20));
		random.setFont(Font.font("Times New Roman", 17));
		view.setFont(Font.font("Times New Roman", 17));
		calculate.setFont(Font.font("Times New Roman", 17));
		seeConnection.setFont(Font.font("Times New Roman", 17));

		// setting warning features
		warning.setStyle("-fx-text-fill:red;");
		warning.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		warning.setPadding(new Insets(15));

		heading.setPadding(new Insets(0, 0, 20, 0));
		heading.setEffect(new Glow(0.5));
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
		heading.setTextFill(Color.RED);
		title0.setFont(Font.font("Courier New", FontWeight.BOLD, 17));
		title1.setFont(Font.font("Courier New", FontWeight.BOLD, 17));
		title2.setFont(Font.font("Courier New", FontWeight.BOLD, 17));
		title3.setFont(Font.font("Courier New", FontWeight.BOLD, 17));

		// setting some water texts in the text areas
		number.setPromptText("Number of LEDs");
		text.setPromptText("Enter sequence of N LEDs seperated by commas (i.e: 1,2,3,4, ... N)");
		result.setPromptText("RESULT");
		answer.setPromptText("Optimal Solution . . . . .");

	}

	public TextField getNumber() {
		return number;
	}

	public TextField getResult() {
		return result;
	}

	public Button getRandom() {
		return random;
	}

	public Button getCalculate() {
		return calculate;
	}

	public Button getView() {
		return view;
	}

	public TextArea getText() {
		return text;
	}

	public TextArea getAnswer() {
		return answer;
	}

	public Button getEnter() {
		return enter;
	}

	public Button getSeeConnection() {
		return seeConnection;
	}

	public Label getWarning() {
		return warning;
	}

	public Button getReadFromFile() {
		return readFromFile;
	}

}
