/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package provajavafx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * FXML Controller class
 *
 * @author LS_Fisso
 */
public class FXMLController implements Initializable {

    
    public Button BuSend;
    public TextArea TxtSendMessage;
    public TextArea MessageTxt;
    public VBox ContactContainer;
    public VBox MessageContainer;
    public BorderPane MainContainer;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //BuSend.addEventHandler(, eventHandler);
      
        
    }    
    
    void Test(){  
        BuSend.setText("DUCK");
    }
    
}
