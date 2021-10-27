package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Dynamicallocation {
    public void todynamicmemomenuf(ActionEvent event) throws IOException {
        Dynamic.choice=3;
        Parent menuparent= FXMLLoader.load(getClass().getResource("dynamicmemorysize.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void todynamicmemomenub(ActionEvent event) throws IOException {
        Dynamic.choice=4;
        Parent menuparent= FXMLLoader.load(getClass().getResource("dynamicmemorysize.fxml"));
        Scene menuscene=new Scene(menuparent,1500,800);
        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuscene);
        window.show();
    }
    public void todynamicmemomenuw(ActionEvent event) throws IOException {
        Dynamic.choice=5;
        Parent menuparent= FXMLLoader.load(getClass().getResource("dynamicmemorysize.fxml"));
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
