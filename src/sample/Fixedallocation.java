package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Fixedallocation {
    public void tofixedmemomenuf(ActionEvent event) throws IOException {
        Fixed.choice=1;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixedmemorysize.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void tofixedmemomenub(ActionEvent event) throws IOException {
        Fixed.choice=2;
        Parent menuparent= FXMLLoader.load(getClass().getResource("fixedmemorysize.fxml"));
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
