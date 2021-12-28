/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package es42_tictactoe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author 4AKER
 */
public class Es42_tictactoe extends Application {
    
    TilePane tile = new TilePane();
    TextArea txtAr = new TextArea();
    
    @Override
    public void start(Stage primaryStage) {
    
        VBox box = new VBox();
        txtAr.setEditable(false);
        txtAr.setPrefSize(300, 100);
        txtAr.setFont(new Font(25));
        tile.setPrefColumns(3);
        TextField[][] arr = new TextField[3][3];
        
        boolean[] winner = new boolean[2];
        winner[0] = false;
        winner[1] = false;
        
        boolean[] turnX = new boolean[1];
        turnX[0] = true;
        
        int[] cord = new int[2];
        cord[0] = 0;
        cord[1] = 0;

        VBox box2 = new VBox();
        box2.getChildren().add(new Button("hello there"));
        Scene scene2 = new Scene(box2);
        for(int i = 0 ; i <3 ; i++){
            for(int j = 0 ; j < 3 ; j++){
                TextField txtFi = new TextField();
                arr[i][j] = txtFi;
                txtFi.setEditable(false);
                txtFi.setFont(new Font(50));
                txtFi.setAlignment(Pos.CENTER);
                txtFi.setPrefSize(100, 100);
                txtFi.setOnKeyPressed((e) ->{
                    switch(e.getCode()){
                        case RIGHT:
                            if( !( winner[0] || winner[1] ) ){
                                cord[1]++;
                                if(cord[1] == 3) cord[1] = 0;
                                arr[cord[0]][cord[1]].requestFocus();
                                arr[cord[0]][cord[1]].deselect();
                            }
                        break;
                        
                        case LEFT:
                            if( !( winner[0] || winner[1] ) ){
                                cord[1]--;
                                if(cord[1] < 0) cord[1] = 2;
                                arr[cord[0]][cord[1]].requestFocus();
                                arr[cord[0]][cord[1]].deselect();
                            }
                        break;
                            
                        case DOWN:
                            if( !( winner[0] || winner[1] ) ){
                                cord[0]++;
                                if(cord[0] == 3) cord[0] = 0;
                                arr[cord[0]][cord[1]].requestFocus();
                                arr[cord[0]][cord[1]].deselect();
                            }
                        break;
                        
                        case UP:
                            if( !( winner[0] || winner[1] ) ){
                                cord[0]--;
                                if(cord[0] < 0) cord[0] = 2;
                                arr[cord[0]][cord[1]].requestFocus();
                                arr[cord[0]][cord[1]].deselect();
                            }
                        break;
                        
                        case SPACE:
                            if( winner[0] || winner[1] || turnX[0]) break;
                            if(arr[cord[0]][cord[1]].getText().equals("")){
                                
                                arr[cord[0]][cord[1]].setText("o");
                                if(arr[cord[0]][0].getText().equals(arr[cord[0]][1].getText())
                                    && arr[cord[0]][0].getText().equals(arr[cord[0]][2].getText())){
                                    System.out.println("player 1 won horizontal!");
                                    txtAr.setText("player 1 won horizontal");
                                    winner[0] = true;
                                    
                                    primaryStage.setScene(scene2);
                                    primaryStage.show();
                                } 
                                else if(arr[0][cord[1]].getText().equals(arr[1][cord[1]].getText())
                                         && arr[0][cord[1]].getText().equals(arr[2][cord[1]].getText())){
                                    System.out.println("player 1 won vertical!");
                                    txtAr.setText("player 1 won vertical");
                                    winner[0] = true;
                                    primaryStage.setScene(scene2);
                                }
                                else if(cord[0] == cord[1]){
                                    if(arr[0][0].getText().equals(arr[1][1].getText())
                                            && arr[0][0].getText().equals(arr[2][2].getText())){
                                        System.out.println("player 1 won diagonally!");
                                        txtAr.setText("player 1 won diagonally");
                                        winner[0] = true;
                                        primaryStage.setScene(scene2);
                                    }
                                }
                                else if((cord[0] + cord[1]) == 2){
                                    if(arr[0][2].getText().equals(arr[1][1].getText())
                                            && arr[0][2].getText().equals(arr[2][0].getText())){
                                        System.out.println("player 1 won diagonally!");
                                        txtAr.setText("player 1 won diagonally");
                                        winner[0] = true;
                                        primaryStage.setScene(scene2);
                                    }
                                }
                                turnX[0] = true;
                            }
                        break;
                        
                        case ENTER:
                            if( winner[0] || winner[1] || (!turnX[0])) break;
                            if(arr[cord[0]][cord[1]].getText().equals("")){
                                arr[cord[0]][cord[1]].setText("x");
                                if(arr[cord[0]][0].getText().equals(arr[cord[0]][1].getText())
                                    && arr[cord[0]][0].getText().equals(arr[cord[0]][2].getText())){
                                    System.out.println("player 2 won horizontal!");
                                    txtAr.setText("player 2 won horizontal");
                                    winner[1] = true;
                                    primaryStage.setScene(scene2);
                                }
                                else if(arr[0][cord[1]].getText().equals(arr[1][cord[1]].getText())
                                            && arr[0][cord[1]].getText().equals(arr[2][cord[1]].getText())){
                                            System.out.println("player 2 won vertical!");
                                            txtAr.setText("player 2 won vertical");
                                            winner[1] = true;
                                            primaryStage.setScene(scene2);
                                }
                                else if(cord[0] == cord[1]){
                                    if(arr[0][0].getText().equals(arr[1][1].getText())
                                            && arr[0][0].getText().equals(arr[2][2].getText())){
                                        System.out.println("player 2 won diagonally!");
                                        txtAr.setText("player 2 won diagonally");
                                        winner[0] = true;
                                        primaryStage.setScene(scene2);
                                    }
                                }
                                else if((cord[0] + cord[1]) == 2){
                                    if(arr[0][2].getText().equals(arr[1][1].getText())
                                            && arr[0][2].getText().equals(arr[2][0].getText())){
                                        System.out.println("player 2 won diagonally!");
                                        txtAr.setText("player 2 won diagonally");
                                        winner[0] = true;
                                        primaryStage.setScene(scene2);
                                    }
                                }
                                turnX[0] = false;
                            }
                        break;
                        
                        case S:
                            primaryStage.setScene(scene2);
                        break;
                            
                    }
                });
                tile.getChildren().add(txtFi);
            }
        }
        
        box.getChildren().add(tile);
        box.getChildren().add(txtAr);
        
        Scene scene = new Scene(box);
        scene.setOnKeyPressed((e) -> {
            //System.out.println("key pressed");
        });
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
