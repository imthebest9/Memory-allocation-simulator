package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Fixedmemorysize {
    public void toevent3(ActionEvent event) throws IOException {
        Fixed.optblock=3;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixed.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent5(ActionEvent event) throws IOException {
        Fixed.optblock=5;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixed.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent7(ActionEvent event) throws IOException {
        Fixed.optblock=7;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixed.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void toevent10(ActionEvent event) throws IOException {
        Fixed.optblock=10;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixed.fxml"));
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
