import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
//TODO  lives on play again, word color match  home button, again button dots
public class Game extends Application {
	static double timeDoub = 5;
	static int livesInt;
	static int bestScoreMarathon = 0;
	static int scoreInt;
	static Stage primaryStage;
	static double timeWhole;
	static int mode;
	static int bestScoreTrial;
	static int redNum;
	static int blueNum;
	static final int MARATHON = 0;
	static final int TIME_TRIAL = 1;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Game.primaryStage = primaryStage;


		//homescreen
		VBox startPane = new VBox(10);
		buildHomeScreen(startPane);

		// borderpane
		BorderPane borderpane = new BorderPane();
		borderpane.setStyle("-fx-background-color: #505050;");


		//hbox
		HBox hbox = new HBox(20);
		borderpane.setTop(hbox);

		// pane
		Pane pane = new Pane();
		borderpane.setCenter(pane);

		
		// timebar
		Rectangle timebar = new Rectangle(0,20,Color.GREY);
		timebar.setOpacity(.75);
		timebar.setStroke(Color.rgb(70, 70, 70));

		borderpane.setBottom(timebar);

		GridPane loserGridPane = new GridPane();
		endScreenBuilder(loserGridPane);

		Scene scene0 = new Scene(startPane, 600, 600);
		Scene scene1 = new Scene(borderpane, 600, 600);
		Scene scene2 = new Scene(loserGridPane, 600, 600);


		// timeline

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {

			timeDoub = (Math.round((timeDoub - .1) * 100)) / 100.00;

			Text time = (Text) ((Pane) borderpane.getTop()).getChildren().get(1);
			time.setText("Time: " + (timeDoub));

			timebar.widthProperty().bind(primaryStage.widthProperty().multiply(timeDoub/timeWhole-.05));
			if (timeDoub < 0) {
				showCorrectHighscore(loserGridPane);


				Text scoreNumText = (Text) loserGridPane.getChildren().get(1);
				scoreNumText.setText("" + scoreInt);
				primaryStage.setScene(scene2);

			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		
		primaryStage.setScene(scene0);
		primaryStage.setTitle("Circles");
		primaryStage.setMinWidth(600);
		primaryStage.setMinHeight(600);
		primaryStage.show();

		//handles

		scene1.setOnMouseClicked(e -> {
			if (livesInt == 0) {
				timeline.stop();
				showCorrectHighscore(loserGridPane);



				Text scoreNumText = (Text) loserGridPane.getChildren().get(1);
				scoreNumText.setText("" + scoreInt);

				Text score = (Text) ((Pane) borderpane.getTop()).getChildren().get(0);
				score.setText("Score: " + scoreInt);
				primaryStage.setScene(scene2);



			}	else {
				timeline.play();
			}
		});

		//home button
		Button homeButton = ((Button) loserGridPane.getChildren().get(3));
		homeButton.setOnAction(e -> {
			primaryStage.setScene(scene0);

		});
		Button marathonButton = ((Button) startPane.getChildren().get(1));
		marathonButton.setOnAction(e->{
			primaryStage.setScene(scene1);
			mode=MARATHON;
			timeWhole = 20;
			redNum = 0;
			blueNum= 255;
			buildInfobar(borderpane);
			circles(borderpane);

		});


		//play again
		Button playAgainButton = ((Button) loserGridPane.getChildren().get(4));

		playAgainButton.setOnAction(e->{
			buildInfobar(borderpane);
			circles(borderpane);
			redNum = 0;
			blueNum= 255;
			timeWhole = 20;

			if(mode==TIME_TRIAL) {
				redNum = 100;
				blueNum= 100;
				timeWhole = 60;
			}
			primaryStage.setScene(scene1);

		});

		//timetrail
		Button timetrialButton = ((Button) startPane.getChildren().get(2));
		timetrialButton.setOnAction(e->{
			primaryStage.setScene(scene1);
			mode=TIME_TRIAL;
			timeWhole = 60;
			redNum = 100;
			blueNum= 100;
			buildInfobar(borderpane);
			circles(borderpane);

		});

		//quitbuttons
		EventHandler<ActionEvent> quitHandler=	(e -> {	primaryStage.close();});


		Button quitButtonSE = ((Button) loserGridPane.getChildren().get(5));
		quitButtonSE.setOnAction(quitHandler);

		Button quitButtonSH = ((Button) startPane.getChildren().get(3));
		quitButtonSH.setOnAction(quitHandler);

	}

	public static void circles(BorderPane borderpane) {
		final double bonusTime =1;
		Pane panecCircleField = (Pane) borderpane.getChildren().get(1);
		drawCircles(panecCircleField);
		for (int i = 0; i < panecCircleField.getChildren().size(); i++) {

			panecCircleField.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent e) {

					if (((Circle) e.getSource()).getFill() != Color.RED) {
						scoreInt++;
						if (mode==MARATHON) {
							timeDoub += bonusTime;
							timeWhole = timeDoub;
						}
						panecCircleField.getChildren().clear();
						HBox hbox = (HBox) borderpane.getChildren().get(0);
						Text score = (Text) hbox.getChildren().get(0);
						score.setText("Score: " + scoreInt);
						circles(borderpane);
					} else {
						livesInt--;
						panecCircleField.getChildren().clear();
						HBox hbox = (HBox) borderpane.getChildren().get(0);
						Text lives = (Text) hbox.getChildren().get(2);
						lives.setText("Lives: " + livesInt);
						if (livesInt > 0) {
							circles(borderpane);
						}
					}
				}

			});
		}
	}
	/**
	 * Create circles
	 */
	public static void drawCircles(Pane panePickCir) {
		double timebarhieght = 20;
		int circleRadius = 50;
		panePickCir.getChildren().clear();
		ArrayList<Circle> circles = new ArrayList<>();
		ArrayList<Double> xs = new ArrayList<>();
		ArrayList<Double> ys = new ArrayList<>();
		double circleX;
		double circleY;

		for (int i = 0; i < 10; i++) {

			do {
				circleX = (Math.random() * (primaryStage.getWidth()-circleRadius*3)+circleRadius);
				circleY = (Math.random() * (primaryStage.getHeight()-(circleRadius*3)-timebarhieght)+circleRadius);

			} while (locationfilled(circleX, circleY, xs, ys, circleRadius));

			Circle cir = new Circle(circleX, circleY, circleRadius, Color.RED);
			if (i == 9) {

				if(redNum<250) {
					redNum+=2;}			
				if(blueNum>30) {
					blueNum-=2;}
				//cir.setFill(Color.rgb(240,00,30));
				cir.setFill(Color.rgb(redNum,00,blueNum));
			}
			cir.setStroke(Color.BLACK);
			xs.add(circleX);
			ys.add(circleY);
			circles.add(cir);
		}
		panePickCir.getChildren().addAll(circles);

	}


	/**
	 * Creates top bar in starting state
	 */
	public static void buildInfobar(BorderPane borderpane) {
		HBox hbox = (HBox) borderpane.getChildren().get(0);

		hbox.getChildren().clear();

		hbox.setAlignment(Pos.CENTER);

		//score
		scoreInt = 0;
		Text score = new Text("Score: " + scoreInt);
		score.setFont(new Font(15));
		score.setFill(Color.LIGHTGRAY);

		//time
		if(mode==0) {
			timeDoub = 20;
		}else {
			timeDoub = 60;
		}
		Text time = new Text("Time: " + timeDoub);
		time.setFont(new Font(20));
		time.setFill(Color.LIGHTGRAY);

		// lives
		livesInt = 3;
		Text lives = new Text("Lives: " + livesInt);
		lives.setFont(new Font(15));
		lives.setFill(Color.LIGHTGRAY);

		hbox.getChildren().add(score);
		hbox.getChildren().add(time);
		hbox.getChildren().add(lives);

		hbox.setStyle("-fx-background-color: #303030;");
	}

	/**
	 * Shows correct Highscore for current gamemode
	 */
	public static void showCorrectHighscore(GridPane loserGridPane) {
		if(mode==MARATHON) {
			if (scoreInt > bestScoreMarathon) {
				bestScoreMarathon = scoreInt;
			}
			Text highscoreText = (Text) loserGridPane.getChildren().get(2);
			highscoreText.setText("Highscore: " + bestScoreMarathon);


		}
		if(mode==TIME_TRIAL) {
			if (scoreInt > bestScoreTrial) {
				bestScoreTrial = scoreInt;
			}
			Text highscoreText = (Text) loserGridPane.getChildren().get(2);
			highscoreText.setText("Highscore: " + bestScoreTrial);

		}
	}
	/**
	 * Creates the screen for when player loses
	 */
	public static void endScreenBuilder(GridPane loserGridPane) {

		loserGridPane.setAlignment(Pos.CENTER);

		//scorewords
		Text scoreWords = new Text(0, 0, "Score");
		scoreWords.setFill(Color.LIGHTGRAY);
		scoreWords.setFont(new Font(20));

		//scorenum
		Text scoreNum = new Text(0, 0, scoreInt + "");
		scoreNum.setFill(Color.LIGHTGRAY);
		scoreNum.setFont(new Font(40));

		//highscore
		Text highscore = new Text("Highscore: " + bestScoreMarathon);
		highscore.setFill(Color.LIGHTGRAY);


		Button homeButton = new Button("Home");
		Button playAgain = new Button("Again");
		Button quitButton = new Button("Quit");

		GridPane.setHalignment(scoreNum, HPos.CENTER);
		GridPane.setHalignment(scoreWords, HPos.CENTER);
		GridPane.setHalignment(highscore, HPos.CENTER);
		GridPane.setHalignment(playAgain, HPos.CENTER);

		loserGridPane.add(scoreWords, 1, 0);
		loserGridPane.add(scoreNum, 1, 1);
		loserGridPane.add(highscore, 1, 2);
		loserGridPane.add(homeButton, 0, 3);
		loserGridPane.add(playAgain, 1, 3);
		loserGridPane.add(quitButton, 2, 3);

		loserGridPane.setStyle("-fx-background-color: #303030;");

	}
	/**
	 * creates home screen
	 */
	public static void buildHomeScreen(VBox startPane) {
		startPane.setAlignment(Pos.CENTER);
		Text titleText = new Text("Circles");
		titleText.setFill(Color.LIGHTGRAY);
		titleText.setFont(new Font (40));

		Button marthonButton = new Button("Marathon");
		Button timeTrialButton = new Button("Time Trial");
		Button quitButton = new Button("Quit");

		startPane.getChildren().add(titleText);
		startPane.getChildren().add(marthonButton);
		startPane.getChildren().add(timeTrialButton);
		startPane.getChildren().add(quitButton);

		startPane.setStyle("-fx-background-color: #303030;");
	}

	/**
	 *  Determines whether a circle is too close to an another
	 */
	public static boolean locationfilled(double circleX, double circleY, ArrayList<Double> previousXs, ArrayList<Double> previousYs,
			int circleRadius) {
		for (int i = 0; i < previousXs.size(); i++) {
			if (circleX <= previousXs.get(i) + circleRadius * 2 && circleX >= previousXs.get(i) - circleRadius * 2
					&& circleY <= previousYs.get(i) + circleRadius * 2 && circleY >= previousYs.get(i) - circleRadius * 2) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		launch(args);
	}

}