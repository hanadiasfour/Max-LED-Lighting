package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ConnectionPane extends BorderPane {

	private VBox battery, LEDLights;// presenting sequences
	private Pane middle;// to draw the lines
	private Image on, off;// light bulbs

	public ConnectionPane() {

		// initialization
		Label heading = new Label("LED Connections");
		battery = new VBox(2);
		LEDLights = new VBox(2);
		middle = new Pane();
		on = new Image("yellowBulb.png");
		off = new Image("blackBulb.png");

		// properties of the heading
		heading.setPadding(new Insets(0, 0, 10, 0));
		heading.setAlignment(Pos.CENTER);
		heading.setEffect(new Glow(1));
		heading.setTextFill(Color.ALICEBLUE);
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 30));

		// setting properties
		middle.setPrefWidth(300);
		middle.setStyle("-fx-background-color: rgb(213, 241, 245);");
		battery.setAlignment(Pos.TOP_LEFT);
		LEDLights.setAlignment(Pos.TOP_LEFT);

		// holds the two VBoxes and the Pane
		HBox content = new HBox();
		content.getChildren().addAll(LEDLights, middle, battery);
		content.setAlignment(Pos.CENTER);
		content.setStyle("-fx-background-color: rgb(213, 241, 245);");

		// adding scrollPane to allow the user to scroll around
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(content);
		scroll.setStyle("-fx-background-color: rgb(213, 241, 245);");
		scroll.setMaxHeight(690);
		scroll.setMaxWidth(615);

		// setting properties of pane
		setCenter(scroll);
		setTop(heading);
		setAlignment(heading, Pos.CENTER);
		setAlignment(scroll, Pos.CENTER);
		setPadding(new Insets(10, 10, 10, 10));
		setStyle("-fx-background-color: rgb(36, 160, 179);");

	}

	// this method adds the LED and numbering labels to the vboxes
	public void fillLEDLights(int[] LEDs) {

		// clearing contents of VBoxes
		battery.getChildren().clear();
		LEDLights.getChildren().clear();

		// adding label at the top of the vbox
		battery.getChildren().add(getMyLabel("Battery Source:", 0));
		LEDLights.getChildren().add(getMyLabel("  LED Lights:  ", 0));

		for (int i = 0; i < LEDs.length; i++) {
			if (LEDs.length < 80) {
				battery.setAlignment(Pos.TOP_LEFT);
				LEDLights.setAlignment(Pos.TOP_LEFT);
				middle.setPrefWidth(150);
				battery.getChildren().add(getMyLabel((i + 1) + "", 2));// adding numbering from index
				LEDLights.getChildren().add(getMyLabel(LEDs[i] + "", 1));// adding LED from array content

			} else {
				battery.setAlignment(Pos.CENTER);
				LEDLights.setAlignment(Pos.CENTER);
				middle.setPrefWidth(100);
				battery.getChildren().add(getMyCircle(Color.RED, (i + 1) + ""));// adding dot from index
				LEDLights.getChildren().add(getMyCircle(Color.BLACK, LEDs[i] + ""));// adding LED dot from array content

			}
		}

	}

	private Circle getMyCircle(Color c, String context) {

		Circle circle = new Circle(3);
		circle.setFill(c);

		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItem = new MenuItem(context);
		menuItem.setStyle("-fx-font-size: 17;");
		contextMenu.getItems().add(menuItem);

		circle.setOnMouseClicked(e -> {

			contextMenu.show(circle, e.getScreenX() + 30, e.getScreenY() - 15);

		});

		return circle;

	}

	public void drawLines(int[] LEDs, int[] answers) {
		// clearing all previous lines
		middle.getChildren().clear();

		int index = 0;// tracks index of answer LED
		Line line;
		for (int i = 0; i < LEDs.length; i++) {// loops to add lines between LED and battery numbering

			if (index < answers.length && LEDs[i] == answers[index]) {

				if (LEDs.length < 80) {// good GUI
					line = new Line(225, getPosition(answers[index], true), -67, getPosition(i + 1, true) - 3);
					line.setStrokeWidth(3);
					line.setEffect(new Glow(.4));
					line.setStroke(Color.MEDIUMVIOLETRED);
					middle.getChildren().add(line);
				} else {// less good GUI

					line = new Line(170, 37 + getPosition(answers[index] - 1, false), -68, 36 + getPosition(i, false));
					line.setEffect(new Glow(.5));
					middle.getChildren().add(line);

				}
				index++;
			}

		}

	}

	// gives the result of the equation
	private double getPosition(int n, boolean isGoodGUI) {

		if (isGoodGUI)
			return (n * 36) + 20;// calculates the right position of y points depending on the index given

		else
			return (n * 8.8) + 3;// calculates the right position of y points depending on the index given

	}

	// loops the whole LED sequence, when an answers is found, the node of that
	// index is obtained to change the light color

	public void lightUpAnswers(int[] LEDs, int[] answers) {
		int index = 0;

		for (int i = 0; i < LEDs.length; i++) {// looping the the whole entered sequence

			if (LEDs.length < 80) {
				if (index < answers.length && LEDs[i] == answers[index]) {// if the number is an answer
					Label l = (Label) LEDLights.getChildren().get(i + 1);// get that label from the VBox
					l.setEffect(new Glow(.6));
					ImageView img = new ImageView(on);
					img.setEffect(new Glow(.6));
					l.setGraphic(img);// changing the off lights to on lights

					Label k = (Label) battery.getChildren().get(LEDs[i]);
					k.setEffect(new Glow(.5));
					k.setGraphic(new ImageView("battery.png"));
					k.setPadding(new Insets(5, 7, 5, 7));
					k.setContentDisplay(ContentDisplay.RIGHT);
					index++;// next answer index
				}
			} else {

				if (index < answers.length && LEDs[i] == answers[index]) {// if the number is an answer
					Circle l = (Circle) LEDLights.getChildren().get(i + 1);// get that Circle from the VBox
					l.setFill(Color.GOLD);// changing the off lights to on lights
					Circle k = (Circle) battery.getChildren().get(LEDs[i]);
					k.setFill(Color.GREEN);
					index++;// next answer index
				}

			}
		}

	}

	// returns a custom label with the text given as its content
	private Label getMyLabel(String text, int type) {

		Label l = new Label("");

		if (type == 0) {// just text
			l.setText(text);
			l.setPadding(new Insets(7, 7, 7, 7));

		} else if (type == 1) {// LED
			l.setText("   " + text);
			l.setPadding(new Insets(5, 5, 5, 5));

			ImageView img = new ImageView(off);
			l.setGraphic(img);
			l.setContentDisplay(ContentDisplay.LEFT);

		} else if (type == 2) {// battery
			l.setText("        " + text);
			l.setPadding(new Insets(7, 7, 7, 7));

		}

		// setting properties
		l.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
		return l;

	}

}
