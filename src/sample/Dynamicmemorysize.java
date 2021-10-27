package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Dynamicmemorysize {
    public void toevent2(ActionEvent event) throws IOException {
        Dynamic.memorySize=20000;
        Parent menuparent= FXMLLoader.load(getClass().getResource("event.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent3(ActionEvent event) throws IOException {
        Dynamic.memorySize=30000;
        Parent menuparent= FXMLLoader.load(getClass().getResource("event.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent4(ActionEvent event) throws IOException {
        Dynamic.memorySize=40000;
        Parent menuparent= FXMLLoader.load(getClass().getResource("event.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent5(ActionEvent event) throws IOException {
        Dynamic.memorySize=50000;
        Parent menuparent= FXMLLoader.load(getClass().getResource("event.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void tomainmenu(ActionEvent event) throws IOException {
        Parent menuparent= FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
}
