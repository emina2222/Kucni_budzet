package obavestenja;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {
    public static void show(String title,String message){
        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);
        Label lblMessage=new Label(message);
        Button btnOk=new Button("OK");
        btnOk.setOnAction(e->stage.close());
        VBox vBox=new VBox(15);
        vBox.getChildren().addAll(lblMessage,btnOk);
        vBox.setAlignment(Pos.CENTER);
        Scene scene=new Scene(vBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
