package es42_tictactoe;

import java.net.URL;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
        
public class Es42_tictactoe extends Application{
    private Scene scene;
    private Parent root;
    
    @Override
    public void start(Stage primaryStage){
        Image icon = new Image(Es42_tictactoe.class.getResourceAsStream("ITI.png"));
        try{
            URL fxml = this.getClass().getResource("HomeScene.fxml");
            root = FXMLLoader.load(fxml); 
        }//try
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }//catch
        String css = this.getClass().getResource("Home.css").toExternalForm();
        scene = new Scene(root);
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Tic_Tac_Toe");
        primaryStage.show();
    }//start
    public static void main(String[] args){
        Application.launch(args);
    }//main
}//class Main