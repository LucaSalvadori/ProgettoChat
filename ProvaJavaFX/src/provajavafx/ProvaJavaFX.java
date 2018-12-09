/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package provajavafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

/**
 *
 * @author LS_Fisso
 */
public class ProvaJavaFX extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
//        
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        
//        Scene scene = new Scene(root, 300, 250);
        
       // primaryStage.setTitle("Prova");
        
        
//        primaryStage.setScene(scene);
//        primaryStage.show();
      //  primaryStage.setScene();
      
      
      Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
      primaryStage.setTitle("ProvaChat");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }

   
    
}
