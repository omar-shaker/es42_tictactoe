/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package javafxapplication12;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 *
 * @author 4AKER
 */
public class es42_tictactoe extends Application {

    public int sceneID = 0;
    public int gameMode = 0;
    public int xPos = 0;
    public int yPos = 0;
    public int xTurn = 1;
    public boolean userWin = false;
    public int[] playerScore = new int[2];
    public TextField xScore = new TextField();
    public TextField oScore = new TextField();
    public VBox scoreBox = new VBox();

    public Controller[] controllerList;
    public Controller gamePad1 = null;
    public Controller gamePad2 = null;
    public EventQueue queue1;
    public EventQueue queue2;
    public Event pressEvent = new Event();
    public Component stickComp;
    public float stickValue;

    public int currentMenuPos = 0;
    public int previousMenuPos = 0;
    public Button singlePlayerBtn = new Button();
    public Button multiPlayerBtn = new Button();
    public Button exitBtn = new Button();
    public Button backBtn = new Button();
    public Button saveBackBtn = new Button();
    public Button exitLabelBtn = new Button();
    public Button selectLabelBtn = new Button();
    public Button playAgainLabelBtn = new Button();
    public Button mainMenuLabelBtn = new Button();
    public Button[] menuBtns = new Button[3];
    public VBox btnsBox = new VBox();

    public TilePane gameTile = new TilePane();
    public TextField[][] gameBoxes = new TextField[3][3];
    public String defaultStyle;

    public static char minimaxBoard[][] = new char[3][3];
    public static char computer = 'o', user = 'x';

    public Label statusLabel = new Label("");
    public BackgroundSize size = new BackgroundSize(20, 20, false, false, true, false);
    public StackPane homeRoot = new StackPane();
    public StackPane gameRoot = new StackPane();
    public StackPane statusRoot = new StackPane();
    public Scene homeScene = new Scene(homeRoot);
    public Scene currentScene;
    public Stage currentStage;

    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoxes[i][j].setText("");
                gameBoxes[i][j].setStyle(defaultStyle);
            }
        }
        gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey");
    }

    public void initStick() {
        controllerList = ControllerEnvironment.getDefaultEnvironment().getControllers();
        int controllersCount = 0;
        for (int i = 0; i < controllerList.length; i++) {
            if (controllerList[i].getType() == Controller.Type.STICK) {
                if (controllerList[i].poll()) {
                    if (controllersCount == 0) {
                        gamePad1 = controllerList[i];
                        queue1 = gamePad1.getEventQueue();
                        controllersCount++;
                    } else {
                        gamePad2 = controllerList[i];
                        queue2 = gamePad2.getEventQueue();
                        controllersCount++;
                    }
                }
            }
        }
        if (controllersCount == 1) {
            gamePad2 = gamePad1;
            queue2 = gamePad1.getEventQueue();
        }
    }

    static class Move {

        int row, col;
    };

    static void clearMiniBoard() {
        minimaxBoard[0][0] = minimaxBoard[0][1] = minimaxBoard[0][2] = '_';
        minimaxBoard[1][0] = minimaxBoard[1][1] = minimaxBoard[1][2] = '_';
        minimaxBoard[2][0] = minimaxBoard[2][1] = minimaxBoard[2][2] = '_';
    }

    static Boolean isMovesLeft() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (minimaxBoard[i][j] == '_') {
                    return true;
                }
            }
        }
        return false;
    }

    static int evaluate() {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (minimaxBoard[row][0] == minimaxBoard[row][1] && minimaxBoard[row][1] == minimaxBoard[row][2]) {
                if (minimaxBoard[row][0] == computer) {
                    return +10;
                } else if (minimaxBoard[row][0] == user) {
                    return -10;
                }
            }
        }
        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (minimaxBoard[0][col] == minimaxBoard[1][col] && minimaxBoard[1][col] == minimaxBoard[2][col]) {
                if (minimaxBoard[0][col] == computer) {
                    return +10;
                } else if (minimaxBoard[0][col] == user) {
                    return -10;
                }
            }
        }
        // Checking for Diagonals for X or O victory.
        if (minimaxBoard[0][0] == minimaxBoard[1][1] && minimaxBoard[1][1] == minimaxBoard[2][2]) {
            if (minimaxBoard[0][0] == computer) {
                return +10;
            } else if (minimaxBoard[0][0] == user) {
                return -10;
            }
        }
        if (minimaxBoard[0][2] == minimaxBoard[1][1] && minimaxBoard[1][1] == minimaxBoard[2][0]) {
            if (minimaxBoard[0][2] == computer) {
                return +10;
            } else if (minimaxBoard[0][2] == user) {
                return -10;
            }
        }
        // Else if none of them have won then return 0
        return 0;
    }

    static int minimax(int depth, Boolean isMax) {
        int score = evaluate();
        /* If Maximizer has won the game return his/her evaluated score */
        if (score == 10) {
            return score;
        }
        /* If Minimizer has won the game return his/her evaluated score */
        if (score == -10) {
            return score;
        }
        /* If there are no more moves and no winner then it is a tie */
        if (isMovesLeft() == false) {
            return 0;
        }
        /* If this maximizer's move */
        if (isMax) {
            int best = -1000;
            /* Traverse all cells */
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    /* Check if cell is empty */
                    if (minimaxBoard[i][j] == '_') {
                        minimaxBoard[i][j] = computer; // Make the move
                        /* Call minimax recursively and choose the maximum value */
                        best = Math.max(best, minimax(depth + 1, !isMax));
                        /* Undo the move */
                        minimaxBoard[i][j] = '_';
                    }
                }
            }
            return best;
        } // If this minimizer's move
        else {
            int best = 1000;
            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (minimaxBoard[i][j] == '_') {
                        // Make the move
                        minimaxBoard[i][j] = user;
                        /* Call minimax recursively and choose the minimum value */
                        best = Math.min(best, minimax(depth + 1, !isMax));
                        // Undo the move
                        minimaxBoard[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    static Move findBestMove() {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;
        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if cell is empty
                if (minimaxBoard[i][j] == '_') {
                    // Make the move
                    minimaxBoard[i][j] = computer;
                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(0, false);
                    // Undo the move
                    minimaxBoard[i][j] = '_';
                    // If the value of the current move is
                    // more than the best value, then update
                    // best
                    if (moveVal > bestVal) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return (bestMove);
    }

    @Override
    public void init() {

        clearMiniBoard();

        playerScore[0] = 0;
        playerScore[1] = 0;
        xScore.setEditable(false);
        oScore.setEditable(false);
        xScore.setFont(new Font(25));
        oScore.setFont(new Font(25));
        xScore.setMaxWidth(150);
        oScore.setMaxWidth(150);
        xScore.setText("X Score: " + playerScore[0]);
        oScore.setText("O Score: " + playerScore[1]);
        scoreBox.getChildren().addAll(xScore, oScore);
        scoreBox.setSpacing(20);

        singlePlayerBtn.setText("Single player");
        multiPlayerBtn.setText("Multi player");
        exitBtn.setText("Exit");
        backBtn.setText("      Back");
        saveBackBtn.setText("      Save");
        exitLabelBtn.setText("      Exit");
        selectLabelBtn.setText("      Select");
        playAgainLabelBtn.setText("      Play Again");
        mainMenuLabelBtn.setText("      Main Menu");
        selectLabelBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/x.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        exitLabelBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/o.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        playAgainLabelBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/o.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        mainMenuLabelBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/square.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        backBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/o.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        saveBackBtn.setBackground(new Background(new BackgroundImage(new Image("file:images/square.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(20, 20, false, false, true, false))));
        singlePlayerBtn.setStyle("-fx-background-color: blue");
        multiPlayerBtn.setStyle("-fx-background-color: blue");
        exitBtn.setStyle("-fx-background-color: blue");
        backBtn.setStyle("-fx-text-fill: lime");
        saveBackBtn.setStyle("-fx-text-fill: lime");
        exitLabelBtn.setStyle("-fx-text-fill: lime");
        selectLabelBtn.setStyle("-fx-text-fill: lime");
        playAgainLabelBtn.setStyle("-fx-text-fill: lime");
        mainMenuLabelBtn.setStyle("-fx-text-fill: lime");
        singlePlayerBtn.setFont(new Font(defaultStyle, 30));
        multiPlayerBtn.setFont(new Font(defaultStyle, 30));
        exitBtn.setFont(new Font(defaultStyle, 30));
        backBtn.setFont(new Font(defaultStyle, 40));
        saveBackBtn.setFont(new Font(defaultStyle, 40));
        exitLabelBtn.setFont(new Font(defaultStyle, 40));
        selectLabelBtn.setFont(new Font(defaultStyle, 40));
        playAgainLabelBtn.setFont(new Font(defaultStyle, 40));
        mainMenuLabelBtn.setFont(new Font(defaultStyle, 40));

        menuBtns[0] = singlePlayerBtn;
        menuBtns[1] = multiPlayerBtn;
        menuBtns[2] = exitBtn;
        btnsBox.setSpacing(25);
        btnsBox.getChildren().addAll(singlePlayerBtn, multiPlayerBtn, exitBtn);
        btnsBox.setAlignment(Pos.CENTER);

        gameTile.setPrefColumns(3);
        gameTile.setMaxHeight(400);
        gameTile.setMaxWidth(400);
        gameTile.setHgap(25);
        gameTile.setVgap(25);
        gameTile.setOpacity(.9f);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoxes[i][j] = new TextField();
                gameBoxes[i][j].setPrefSize(100, 100);
                gameBoxes[i][j].setEditable(false);
                gameBoxes[i][j].setFont(new Font(50));
                gameBoxes[i][j].setAlignment(Pos.CENTER);
                gameTile.getChildren().add(gameBoxes[i][j]);
            }
        }
        defaultStyle = gameBoxes[0][0].getStyle();

        statusLabel.setFont(new Font(50));
        statusLabel.setTextFill(Color.LIME);
        homeRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/HoverSmall.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
        homeRoot.getChildren().addAll(btnsBox, exitLabelBtn, selectLabelBtn);
        StackPane.setAlignment(exitLabelBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(selectLabelBtn, Pos.BOTTOM_RIGHT);
        gameRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/Game.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
        gameRoot.getChildren().addAll(gameTile, backBtn, saveBackBtn, scoreBox);
        StackPane.setAlignment(backBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(saveBackBtn, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(scoreBox, Pos.TOP_LEFT);
        statusRoot.getChildren().addAll(statusLabel, mainMenuLabelBtn, playAgainLabelBtn);
        StackPane.setAlignment(statusLabel, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(mainMenuLabelBtn, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(playAgainLabelBtn, Pos.BOTTOM_RIGHT);

        singlePlayerBtn.setOnAction((ActionEvent event) -> {
            sceneID = 1;
            gameMode = 0;
            currentScene.setRoot(gameRoot);
            gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey; -fx-text-fill: white");
        });
        multiPlayerBtn.setOnAction((ActionEvent event) -> {
            sceneID = 1;
            gameMode = 1;
            currentScene.setRoot(gameRoot);
            gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey; -fx-text-fill: white");
        });
        exitBtn.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        backBtn.setOnAction((ActionEvent event) -> {
            sceneID = 0;
            playerScore[0] = 0;
            playerScore[1] = 0;
            userWin = false;
            clearMiniBoard();
            clearBoard();
            currentScene.setRoot(homeRoot);
        });
        saveBackBtn.setOnAction((ActionEvent event) -> {
            sceneID = 0;
            currentScene.setRoot(homeRoot);
        });

        new Thread(() -> {

            initStick();
            if (gamePad1 == null) {
                Platform.runLater(() -> {
                    homeRoot.getChildren().clear();
                    homeRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/Disconnected.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                });
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(x -> {
                    System.exit(0);
                });
                delay.play();
                while (true) {

                }
            }
            while (true) {

                if (!(gamePad1.poll() && gamePad2.poll())) {
                    statusRoot.getChildren().clear();
                    statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/Disconnected.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                    currentScene.setRoot(statusRoot);
                    PauseTransition delay = new PauseTransition(Duration.seconds(10));
                    delay.setOnFinished(x -> {
                        System.exit(0);
                    });
                    delay.play();
                    while (true) {

                    }
//                    gamePad1 = null;
//                    gamePad2 = null;
//                    controllerList = null;
//                    while (gamePad1 == null) {
//                        initStick();
//                    }
                }
                gamePad1.poll();
                gamePad2.poll();

                while ((queue1.getNextEvent(pressEvent) && xTurn == 1) || (queue2.getNextEvent(pressEvent) && xTurn == 0)) {
                    stickComp = pressEvent.getComponent();
                    stickValue = pressEvent.getValue();

                    switch (stickComp.getName()) {
                        case "Y Axis":
                            switch (sceneID) {
                                case 0://homeScene
                                    if (stickValue == 1.0f) {
                                        previousMenuPos = currentMenuPos;
                                        currentMenuPos++;
                                        if (currentMenuPos > 2) {
                                            currentMenuPos = 0;
                                        }
                                    } else if (stickValue == -1.0f) {
                                        previousMenuPos = currentMenuPos;
                                        currentMenuPos--;
                                        if (currentMenuPos < 0) {
                                            currentMenuPos = 2;
                                        }
                                    }
                                    Platform.runLater(() -> {
                                        menuBtns[previousMenuPos].setStyle("-fx-background-color: blue");
                                        menuBtns[currentMenuPos].requestFocus();
                                        menuBtns[currentMenuPos].setStyle("-fx-background-color: lime");
                                    });
                                    break;

                                case 1://gameScene
                                    if (stickValue == 1.0f) {
                                        switch (gameBoxes[yPos][xPos].getText()) {
                                            case "o":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: blue;-fx-background-color: white;");
                                                break;

                                            case "x":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: red;-fx-background-color: white;");
                                                break;

                                            default:
                                                gameBoxes[yPos][xPos].setStyle("-fx-background-color: white;");
                                                break;
                                        }
                                        yPos++;
                                        if (yPos == 3) {
                                            yPos = 0;
                                        }
                                    } else if (stickValue == -1.0f) {
                                        switch (gameBoxes[yPos][xPos].getText()) {
                                            case "o":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: blue;-fx-background-color: white;");
                                                break;

                                            case "x":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: red;-fx-background-color: white;");
                                                break;

                                            default:
                                                gameBoxes[yPos][xPos].setStyle("-fx-background-color: white;");
                                                break;
                                        }
                                        yPos--;
                                        if (yPos < 0) {
                                            yPos = 2;
                                        }
                                    }
                                    Platform.runLater(() -> {
                                        gameBoxes[yPos][xPos].requestFocus();
                                        gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey;-fx-text-fill: white");
                                        gameBoxes[yPos][xPos].deselect();
                                    });
                                    break;

                                case 2://win &lose scene
                                    break;
                            }

                            break;

                        case "X Axis":
                            switch (sceneID) {
                                case 0://homeScene
                                    break;

                                case 1://gameScene
                                    if (stickValue == 1.0f) {
                                        switch (gameBoxes[yPos][xPos].getText()) {
                                            case "o":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: blue;-fx-background-color: white;");
                                                break;

                                            case "x":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: red;-fx-background-color: white;");
                                                break;

                                            default:
                                                gameBoxes[yPos][xPos].setStyle("-fx-background-color: white;");
                                                break;
                                        }
                                        xPos++;
                                        if (xPos == 3) {
                                            xPos = 0;
                                        }
                                    } else if (stickValue == -1.0f) {
                                        switch (gameBoxes[yPos][xPos].getText()) {
                                            case "o":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: blue;-fx-background-color: white;");
                                                break;

                                            case "x":
                                                gameBoxes[yPos][xPos].setStyle("-fx-text-fill: red;-fx-background-color: white;");
                                                break;

                                            default:
                                                gameBoxes[yPos][xPos].setStyle("-fx-background-color: white;");
                                                break;
                                        }
                                        xPos--;
                                        if (xPos < 0) {
                                            xPos = 2;
                                        }
                                    }
                                    Platform.runLater(() -> {
                                        gameBoxes[yPos][xPos].requestFocus();
                                        gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey;-fx-text-fill: white");
                                        gameBoxes[yPos][xPos].deselect();
                                    });
                                    break;

                                case 2://win & lose scene
                                    break;
                            }
                            break;

                        case "Button 1"://joyStick O button
                            switch (sceneID) {
                                case 0://homeScene
                                    if (stickValue == 0) {
                                        break;
                                    }
                                    System.exit(0);
                                    break;

                                case 1://gameScene
                                    if (stickValue == 0) {
                                        break;
                                    }
                                    Platform.runLater(() -> {
                                        backBtn.fire();
                                    });
                                    break;

                                case 2://win & lose scene
                                    if (stickValue == 0) {
                                        break;
                                    }
                                    Platform.runLater(() -> {
                                        userWin = false;
                                        sceneID = 1;
                                        currentScene.setRoot(gameRoot);
                                    });
                                    break;
                            }
                            break;

                        case "Button 2":// joyStick X button
                            switch (sceneID) {
                                case 0://homeScene
                                    Platform.runLater(() -> {
                                        menuBtns[currentMenuPos].fire();
                                    });
                                    break;

                                case 1://gameScene
                                    switch (gameMode) {
                                        case 0://singlePlayer mode
                                            if (stickValue == 0) {
                                                break;
                                            }
                                            if (gameBoxes[yPos][xPos].getText().equals("")) {
                                                gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey; -fx-text-inner-color: red");
                                                gameBoxes[yPos][xPos].setText("x");
                                                if (gameBoxes[yPos][0].getText().equals(gameBoxes[yPos][1].getText()) && gameBoxes[yPos][0].getText().equals(gameBoxes[yPos][2].getText())) {
                                                    userWin = true;
                                                    playerScore[xTurn]++;//Player won
                                                    gameBoxes[yPos][0].setStyle("-fx-background-color: lime");
                                                    gameBoxes[yPos][1].setStyle("-fx-background-color: lime");
                                                    gameBoxes[yPos][2].setStyle("-fx-background-color: lime");
                                                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                    delay.setOnFinished(x -> {
                                                        clearMiniBoard();
                                                        clearBoard();
                                                        sceneID = 2;
                                                        statusLabel.setText("You Won");
                                                        statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/ph.jpeg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                        currentScene.setRoot(statusRoot);
                                                    });
                                                    delay.play();
                                                } else if (gameBoxes[0][xPos].getText().equals(gameBoxes[1][xPos].getText()) && gameBoxes[0][xPos].getText().equals(gameBoxes[2][xPos].getText())) {
                                                    userWin = true;
                                                    playerScore[xTurn]++;//Player Won
                                                    gameBoxes[0][xPos].setStyle("-fx-background-color: lime");
                                                    gameBoxes[1][xPos].setStyle("-fx-background-color: lime");
                                                    gameBoxes[2][xPos].setStyle("-fx-background-color: lime");
                                                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                    delay.setOnFinished(x -> {
                                                        clearMiniBoard();
                                                        clearBoard();
                                                        sceneID = 2;
                                                        statusLabel.setText("You Won");
                                                        statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/ph.jpeg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                        currentScene.setRoot(statusRoot);
                                                    });
                                                    delay.play();
                                                } else if (xPos == yPos) {
                                                    if (gameBoxes[0][0].getText().equals(gameBoxes[1][1].getText()) && gameBoxes[0][0].getText().equals(gameBoxes[2][2].getText())) {
                                                        userWin = true;
                                                        playerScore[xTurn]++;//Player Won
                                                        gameBoxes[0][0].setStyle("-fx-background-color: lime");
                                                        gameBoxes[1][1].setStyle("-fx-background-color: lime");
                                                        gameBoxes[2][2].setStyle("-fx-background-color: lime");
                                                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                        delay.setOnFinished(x -> {
                                                            clearMiniBoard();
                                                            clearBoard();
                                                            sceneID = 2;
                                                            statusLabel.setText("You Won");
                                                            statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/ph.jpeg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                            currentScene.setRoot(statusRoot);
                                                        });
                                                        delay.play();
                                                    }
                                                } else if ((xPos + yPos) == 2) {
                                                    if (gameBoxes[0][2].getText().equals(gameBoxes[1][1].getText()) && gameBoxes[0][2].getText().equals(gameBoxes[2][0].getText())) {
                                                        userWin = true;
                                                        playerScore[xTurn]++;//Player Won
                                                        gameBoxes[0][2].setStyle("-fx-background-color: lime");
                                                        gameBoxes[1][1].setStyle("-fx-background-color: lime");
                                                        gameBoxes[2][0].setStyle("-fx-background-color: lime");
                                                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                        delay.setOnFinished(x -> {
                                                            clearMiniBoard();
                                                            clearBoard();
                                                            sceneID = 2;
                                                            statusLabel.setText("You Won");
                                                            statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/ph.jpeg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                            currentScene.setRoot(statusRoot);
                                                        });
                                                        delay.play();
                                                    }
                                                }
                                                if (!userWin) {
                                                    minimaxBoard[yPos][xPos] = 'x';
                                                    if (isMovesLeft()) {
                                                        Move perfectMove = findBestMove();
                                                        minimaxBoard[perfectMove.row][perfectMove.col] = 'o';
                                                        gameBoxes[perfectMove.row][perfectMove.col].setStyle("-fx-text-inner-color: blue");
                                                        gameBoxes[perfectMove.row][perfectMove.col].setText("o");
                                                        if (evaluate() == 10) {//Computer won
                                                            PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                            delay.setOnFinished(x -> {
                                                                clearMiniBoard();
                                                                clearBoard();
                                                                sceneID = 2;
                                                                statusLabel.setText("Really! -_-");
                                                                statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/CompWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                currentScene.setRoot(statusRoot);
                                                            });
                                                            delay.play();
                                                        }
                                                    }
                                                    if (evaluate() == 0 && !isMovesLeft()) {
                                                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                        delay.setOnFinished(x -> {
                                                            clearMiniBoard();
                                                            clearBoard();
                                                            sceneID = 2;
                                                            statusLabel.setText("Draw");
                                                            statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/ph.jpeg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                            currentScene.setRoot(statusRoot);
                                                        });
                                                        delay.play();
                                                    }
                                                }
                                            }
                                            break;

                                        case 1://multiPlayer mode
                                            if (stickValue == 0) {
                                                break;
                                            }
                                            if (gameBoxes[yPos][xPos].getText().equals("")) {
                                                switch (xTurn) {
                                                    case 0:
                                                        gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey; -fx-text-inner-color: blue");
                                                        gameBoxes[yPos][xPos].setText("o");
                                                        xTurn = 1;
                                                        break;

                                                    case 1:
                                                        gameBoxes[yPos][xPos].setStyle("-fx-background-color: grey; -fx-text-inner-color: red");
                                                        gameBoxes[yPos][xPos].setText("x");
                                                        xTurn = 0;
                                                        break;
                                                }
                                                if (gameBoxes[yPos][0].getText().equals(gameBoxes[yPos][1].getText()) && gameBoxes[yPos][0].getText().equals(gameBoxes[yPos][2].getText())) {
                                                    playerScore[xTurn]++;//Player won
                                                    xScore.setText("X Score: " + playerScore[0]);
                                                    oScore.setText("O Score: " + playerScore[1]);
                                                    gameBoxes[yPos][0].setStyle("-fx-background-color: lime");
                                                    gameBoxes[yPos][1].setStyle("-fx-background-color: lime");
                                                    gameBoxes[yPos][2].setStyle("-fx-background-color: lime");
                                                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                    delay.setOnFinished(x -> {
                                                        clearBoard();
                                                        sceneID = 2;
                                                        switch (xTurn) {
                                                            case 0:
                                                                statusLabel.setText("X Won");
                                                                statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/XWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                currentScene.setRoot(statusRoot);
                                                                break;

                                                            case 1:
                                                                statusLabel.setText("O Won");
                                                                statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/OWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                currentScene.setRoot(statusRoot);
                                                                break;
                                                        }
                                                    });
                                                    delay.play();
                                                } else if (gameBoxes[0][xPos].getText().equals(gameBoxes[1][xPos].getText()) && gameBoxes[0][xPos].getText().equals(gameBoxes[2][xPos].getText())) {
                                                    playerScore[xTurn]++;//Player Won
                                                    xScore.setText("X Score: " + playerScore[0]);
                                                    oScore.setText("O Score: " + playerScore[1]);
                                                    gameBoxes[0][xPos].setStyle("-fx-background-color: lime");
                                                    gameBoxes[1][xPos].setStyle("-fx-background-color: lime");
                                                    gameBoxes[2][xPos].setStyle("-fx-background-color: lime");
                                                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                    delay.setOnFinished(x -> {
                                                        clearBoard();
                                                        sceneID = 2;
                                                        switch (xTurn) {
                                                            case 0:
                                                                statusLabel.setText("X Won");
                                                                statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/XWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                currentScene.setRoot(statusRoot);
                                                                break;

                                                            case 1:
                                                                statusLabel.setText("O Won");
                                                                statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/OWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                currentScene.setRoot(statusRoot);
                                                                break;
                                                        }
                                                    });
                                                    delay.play();
                                                } else if (xPos == yPos) {
                                                    if (gameBoxes[0][0].getText().equals(gameBoxes[1][1].getText()) && gameBoxes[0][0].getText().equals(gameBoxes[2][2].getText())) {
                                                        playerScore[xTurn]++;//Player Won
                                                        xScore.setText("X Score: " + playerScore[0]);
                                                        oScore.setText("O Score: " + playerScore[1]);
                                                        gameBoxes[0][0].setStyle("-fx-background-color: lime");
                                                        gameBoxes[1][1].setStyle("-fx-background-color: lime");
                                                        gameBoxes[2][2].setStyle("-fx-background-color: lime");
                                                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                        delay.setOnFinished(x -> {
                                                            clearBoard();
                                                            sceneID = 2;
                                                            switch (xTurn) {
                                                                case 0:
                                                                    statusLabel.setText("X Won");
                                                                    statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/XWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                    currentScene.setRoot(statusRoot);
                                                                    break;

                                                                case 1:
                                                                    statusLabel.setText("O Won");
                                                                    statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/OWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                    currentScene.setRoot(statusRoot);
                                                                    break;
                                                            }
                                                        });
                                                        delay.play();
                                                    }
                                                } else if ((xPos + yPos) == 2) {
                                                    if (gameBoxes[0][2].getText().equals(gameBoxes[1][1].getText()) && gameBoxes[0][2].getText().equals(gameBoxes[2][0].getText())) {
                                                        playerScore[xTurn]++;//Player Won
                                                        xScore.setText("X Score: " + playerScore[0]);
                                                        oScore.setText("O Score: " + playerScore[1]);
                                                        gameBoxes[0][2].setStyle("-fx-background-color: lime");
                                                        gameBoxes[1][1].setStyle("-fx-background-color: lime");
                                                        gameBoxes[2][0].setStyle("-fx-background-color: lime");
                                                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                                                        delay.setOnFinished(x -> {
                                                            clearBoard();
                                                            sceneID = 2;
                                                            switch (xTurn) {
                                                                case 0:
                                                                    statusLabel.setText("X Won");
                                                                    statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/XWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                    currentScene.setRoot(statusRoot);
                                                                    break;

                                                                case 1:
                                                                    statusLabel.setText("O Won");
                                                                    statusRoot.setBackground(new Background(new BackgroundImage(new Image("file:images/OWin.gif"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size)));
                                                                    currentScene.setRoot(statusRoot);
                                                                    break;
                                                            }
                                                        });
                                                        delay.play();
                                                    }
                                                }
                                            }

                                            break;
                                    }
                                    break;

                                case 2://win & lose scene
                                    break;
                            }
                            break;

                        case "Button 3"://square button
                            switch (sceneID) {
                                case 1://gameScene
                                    Platform.runLater(() -> {
                                        saveBackBtn.fire();
                                    });
                                    break;

                                case 2://win & lose scene
                                    Platform.runLater(() -> {
                                        sceneID = 0;
                                        playerScore[0] = 0;
                                        playerScore[1] = 0;
                                        currentScene.setRoot(homeRoot);
                                    });
                                    break;
                            }
                            break;
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(homeScene);
        primaryStage.setFullScreen(true);
        currentScene = primaryStage.getScene();
        currentStage = primaryStage;
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
