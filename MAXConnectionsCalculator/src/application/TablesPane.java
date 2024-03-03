package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TablesPane extends BorderPane {

	private GridPane DP1;
	private Image img0, img1, img2;

	public TablesPane() {

		// initialization
		Label heading = new Label("Dynamic Programming Table");
		ScrollPane scroll = new ScrollPane();
		DP1 = new GridPane();
		img0 = new Image("blackLeft.png");
		img1 = new Image("blackDiagonal.png");
		img2 = new Image("blackUp.png");

		// properties of the heading
		heading.setPadding(new Insets(0, 0, 10, 0));
		heading.setAlignment(Pos.CENTER);
		heading.setEffect(new Glow(0.5));
		heading.setTextFill(Color.RED);
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 20));

		// setting Grid properties
		DP1.setHgap(5);
		DP1.setVgap(5);
		DP1.setPadding(new Insets(10, 10, 10, 10));
		DP1.setAlignment(Pos.CENTER);

		// setting background colors
		DP1.setStyle("-fx-background-color:POWDERBLUE;");
		scroll.setStyle("-fx-background-color:POWDERBLUE;");
		setStyle("-fx-background-color:POWDERBLUE;");

		// fixed size
		scroll.setMaxHeight(700);
		scroll.setMaxWidth(1500);

		// setting the contents
		scroll.setContent(DP1);
		setCenter(scroll);
		setTop(heading);
		setAlignment(heading, Pos.CENTER);
		setAlignment(scroll, Pos.CENTER);
		setMargin(scroll, new Insets(20, 20, 20, 20));

	}

	// this method fills the grid pane with the correct values of the DP
	// calculations, and also colors the grid squares
	public void fillGrid(int n, int[] x, int[][] cost, int[][] directions, boolean isGoodGUI) {

		if (isGoodGUI) {// n is less than 50
			DP1.setHgap(5);
			DP1.setVgap(5);
		} else {
			DP1.setHgap(2);
			DP1.setVgap(2);
		}

		DP1.getChildren().clear();// clear previous content

		// filling and coloring the unnecessary squares
		DP1.add(getMyNumberLabel("-", isGoodGUI), 0, 0);
		DP1.add(getMyNumberLabel("-", isGoodGUI), 1, 0);
		DP1.add(getMyNumberLabel("-", isGoodGUI), 0, 1);

		fillCellWithColor(0, 0, Color.LIGHTSKYBLUE);
		fillCellWithColor(1, 0, Color.LIGHTSKYBLUE);
		fillCellWithColor(0, 1, Color.LIGHTSKYBLUE);

		for (int i = 2; i <= n + 1; i++) {// top numbers (LED sequence)
			DP1.add(getMyNumberLabel(x[i - 2] + "", isGoodGUI), i, 0);
			fillCellWithColor(i, 0, Color.LIGHTSKYBLUE);

		}

		for (int i = 2; i <= n + 1; i++) {// side numbers (Numbering sequence)
			DP1.add(getMyNumberLabel((i - 1) + "", isGoodGUI), 0, i);
			fillCellWithColor(0, i, Color.LIGHTSKYBLUE);

		}

		for (int i = 2; i <= n + 1; i++) {// top zeros used in calculations
			DP1.add(getMyNumberLabel(0 + "", isGoodGUI), i, 1);
			fillCellWithColor(i, 1, Color.ALICEBLUE);

		}

		for (int i = 1; i <= n + 1; i++) {// side zeros used in calculations
			DP1.add(getMyNumberLabel(0 + "", isGoodGUI), 1, i);
			fillCellWithColor(1, i, Color.ALICEBLUE);

		}

		for (int i = 2; i <= x.length + 1; i++) { // filling the grid with calculated DP numbers
			for (int j = 2; j <= x.length + 1; j++) {

				DP1.add(getMyArrowLabel(cost[i - 1][j - 1] + "", directions[i - 2][j - 2], isGoodGUI), j, i);
				fillCellWithColor(j, i, Color.ALICEBLUE);

			}
		}

		fillArrowsPathWithColor(directions, n + 1, n + 1);// adding the arrows

	}

	// fills in the followed path of answer sequences with color to show the
	// important boxes (recursion)
	private void fillArrowsPathWithColor(int[][] directions, int i, int j) {

		if (i == 1 || j == 1)// base case
			return;

		if (directions[i - 2][j - 2] == 1) {// direction is diagonal
			fillCellWithColor(j, i, Color.AQUAMARINE);

			fillCellWithColor(0, i, Color.VIOLET);
			fillCellWithColor(j, 0, Color.VIOLET);

			fillArrowsPathWithColor(directions, i - 1, j - 1);

		} else if (directions[i - 2][j - 2] == 2) {// direction is up
			fillCellWithColor(j, i, Color.AQUAMARINE);
			fillArrowsPathWithColor(directions, i - 1, j);

		} else {// direction is left
			fillCellWithColor(j, i, Color.AQUAMARINE);
			fillArrowsPathWithColor(directions, i, j - 1);
		}

	}

	// creates a custom label with the correct arrow picture as its content
	private Label getMyNumberLabel(String type, boolean isGoodGUI) {

		Label l = new Label("");

		if (isGoodGUI) {// normal representation
			l.setText(" " + type + " ");
			l.setPadding(new Insets(10, 0, 10, 5));
			l.setFont(Font.font("Courier New", FontWeight.BOLD, 17));

		} else {// minimized representation
			l.setText(type);
			l.setPadding(new Insets(10, 10, 5, 8));
			l.setFont(Font.font("Courier New", FontWeight.BOLD, 10));
		}

		return l;

	}

	// returns a custom label with the number given as its content
	private Label getMyArrowLabel(String number, int type, boolean isGoodGUI) {

		Label l = new Label("  " + number);

		ImageView diagonal = new ImageView(img1);
		ImageView up = new ImageView(img2);
		ImageView left = new ImageView(img0);
		if (isGoodGUI) {

			up.setScaleX(.7);
			up.setScaleY(.7);
			left.setScaleX(.7);
			left.setScaleY(.7);
			diagonal.setScaleX(.7);
			diagonal.setScaleY(.7);

			// setting properties
			l.setPadding(new Insets(0, 5, 0, 0));
			l.setGraphicTextGap(-10);
			l.setContentDisplay(ContentDisplay.TOP);
			l.setFont(Font.font("Courier New", FontWeight.BOLD, 17));

		} else {
			up.setScaleX(.4);
			up.setScaleY(.4);
			left.setScaleX(.4);
			left.setScaleY(.4);
			diagonal.setScaleX(.4);
			diagonal.setScaleY(.4);

			// setting properties
			l.setPadding(new Insets(-2.5, -2.5, -2.5, -2.5));
			l.setGraphicTextGap(-11);
			l.setContentDisplay(ContentDisplay.TOP);
			l.setFont(Font.font("Courier New", FontWeight.BOLD, 11));

		}

		if (type == 0) // left
			l.setGraphic(left);

		else if (type == 1)// diagonal
			l.setGraphic(diagonal);

		else// up
			l.setGraphic(up);

		return l;

	}

	// receives column and row indexes to change the color of that position in the
	// grid pane
	private void fillCellWithColor(int column, int row, Color color) {
		// find the node at the specified position
		Label cell = (Label) DP1.getChildren().stream()
				.filter(node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row)
				.findFirst().orElse(null);

		// apply background color as long as it's not null
		if (cell != null) {
			cell.setStyle("-fx-background-color: #" + color.toString().substring(2));

		}

	}

}
