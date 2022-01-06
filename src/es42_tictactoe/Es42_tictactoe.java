package es42_tictactoe;

import java.awt.Shape;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.util.Duration;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class Es42_tictactoe extends Application implements Initializable {

    static class Move {

        int row, col;
    };
    public Stage stage;
    public Scene homeScene;
    public Scene multiScene;
    public Scene singleScene;
    public Scene winScene;
    public Parent homeRoot;
    public Parent winRoot;
    public Parent loseRoot;
    /* static attributes */
    public FileInputStream input = null;
    public Image homeImage;
    public ImageView homeView;
    public static char computer = 'o', user = 'x';
    public static char minimaxBoard[][] = new char[3][3];
    public static Button[] menuBtns = new Button[3];
    public static Button[] retBtns = new Button[3];
    public static TextField[][] gameBoxes;
    public static boolean[] isWinner;
    public static int[] turn;
    public static int[] cord;
    public static int[] playerScore;
    public static Controller[] controllerList = ControllerEnvironment.getDefaultEnvironment().getControllers();
    public static Controller gamepad1;
    public static Controller gamepad2;
    public static EventQueue queue1;
    public static EventQueue queue2;
    public static Event event = new Event();
    public static int sceneID;

    @FXML
    public Button btn1;

    @FXML
    public Button btn2;

    @FXML
    public Button btn3;

    /* ************************ static methods ****************************** */
 /* This function returns true if there are moves remaining on the board. It returns false if there are no moves left to play. */
    static Boolean isMovesLeft() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (minimaxBoard[i][j] == '_') {
                    return true;
                }
            }
        }
        return false;
    }//isMovesLeft

    /* This is the evaluation function as discussed */
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
    }//evaluate method    

    /* This is the minimax function. It considers all the possible ways the game 
        can go and returns the value of the board */
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
    }//minimax

    /* This will return the best possible move for the player */
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
    }//findBestMove    

    /* event handling methods */
    public void singlePlayer(ActionEvent event) {
        sceneID = 1;
        minimaxBoard[0][0] = minimaxBoard[0][1] = minimaxBoard[0][2] = '_';
        minimaxBoard[1][0] = minimaxBoard[1][1] = minimaxBoard[1][2] = '_';
        minimaxBoard[2][0] = minimaxBoard[2][1] = minimaxBoard[2][2] = '_';
        try {
            input = new FileInputStream("A:\\00_SHIELD\\02_Codes\\12_Java\\Java_ITI_Course\\Java_Project\\src\\es42_tictactoe\\Game.jpg");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        homeImage = new Image(input);
        homeView = new ImageView(homeImage);
        StackPane stack = new StackPane();
        BorderPane pane = new BorderPane();
        TilePane gameTile = new TilePane();
        gameTile.setPrefColumns(3);
        gameTile.setMaxSize(320, 320);
        gameTile.setVgap(10);
        gameTile.setHgap(10);
        FlowPane score = new FlowPane();
        Label userScore = new Label("User ");
        Label computerScore = new Label("Computer ");
        score.getChildren().addAll(userScore, computerScore);
        gameBoxes = new TextField[3][3];//The boxes of the game
        playerScore = new int[2];//0 --> playerX && 1 --> playerO
        playerScore[0] = 0;//Default begin : playerX score
        playerScore[1] = 0;//Default begin : playerO score
        isWinner = new boolean[1];
        isWinner[0] = false; // defines whether there is a winner or not
        turn = new int[1]; //0 --> 'O' && 1 --> 'X'
        turn[0] = 1; // To begin playing with 'X'
        cord = new int[2];//The "X,Y" Coordinates ... 0 --> 'Y' && 1 --> 'X'
        cord[0] = 0;//set default 'Y' value
        cord[1] = 0;//set default 'X' value
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TextField gameBox = new TextField();
                gameBoxes[i][j] = gameBox;
                gameBox.setEditable(false);
                gameBox.setFont(new Font(50));
                gameBox.setAlignment(Pos.CENTER);
                gameBox.setPrefSize(100, 100);
                gameTile.getChildren().add(gameBox);
            }//for ... embedded for
        }//for
        pane.setTop(score);
        pane.setCenter(gameTile);
        stack.getChildren().addAll(homeView, pane);
        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        singleScene = new Scene(stack);
        stage.setScene(singleScene);
        stage.setFullScreen(true);
        stage.show();
    }//singlePlayer    

    public void multiPlayer(ActionEvent event) {
        sceneID = 2;
        try {
            input = new FileInputStream("A:\\00_SHIELD\\02_Codes\\12_Java\\Java_ITI_Course\\Java_Project\\src\\es42_tictactoe\\Game.jpg");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        homeImage = new Image(input);
        homeView = new ImageView(homeImage);
        StackPane stack = new StackPane();
        BorderPane pane = new BorderPane();
        TilePane gameTile = new TilePane();
        gameTile.setPrefColumns(3);
        gameTile.setMaxSize(320, 320);
        gameTile.setVgap(10);
        gameTile.setHgap(10);
        gameBoxes = new TextField[3][3];//The boxes of the game
        playerScore = new int[2];//0 --> playerX && 1 --> playerO
        playerScore[0] = 0;//Default begin : playerX score
        playerScore[1] = 0;//Default begin : playerO score
        isWinner = new boolean[1];
        isWinner[0] = false; // defines whether there is a winner or not
        turn = new int[1]; //0 --> 'O' && 1 --> 'X'
        turn[0] = 1; // To begin playing with 'X'
        cord = new int[2];//The "X,Y" Coordinates ... 0 --> 'Y' && 1 --> 'X'
        cord[0] = 0;//set default 'Y' value
        cord[1] = 0;//set default 'X' value
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TextField gameBox = new TextField();
                gameBoxes[i][j] = gameBox;
                gameBox.setEditable(false);
                gameBox.setFont(new Font(50));
                gameBox.setAlignment(Pos.CENTER);
                gameBox.setPrefSize(100, 100);
                gameTile.getChildren().add(gameBox);
            }//for ... embedded for
        }//for
        pane.setCenter(gameTile);
        stack.getChildren().addAll(homeView, pane);
        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        String css = this.getClass().getResource("Game.css").toExternalForm();
        multiScene = new Scene(stack);
        multiScene.getStylesheets().add(css);
        stage.setScene(multiScene);
        stage.setFullScreen(true);
        stage.show();
    }//multiPlayer

    public void exit(ActionEvent event) {
        System.exit(0);
    }//exit

    /* overriden methods */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBtns[0] = btn1;
        menuBtns[1] = btn2;
        menuBtns[2] = btn3;
    }//initialize

}