package GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import konekcija.Baza;

import java.awt.*;
import java.io.IOException;

public class KucniBudzet extends Application {
    Baza baza=new Baza();
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Kucni budzet E&M");
        VBox hBox=new VBox(20);

        hBox.setStyle("-fx-background-color: lightblue;-fx-font-size: 24 arial;");
        Label lblUser=new Label("Korisničko ime:");
        TextField txtKorIme=new TextField();
        Label lblpass=new Label("Lozinka:");
        PasswordField txtPass=new PasswordField();
        Button btnPotvrdi=new Button("Potvrdi");

        btnPotvrdi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(baza.login(txtKorIme.getText(),txtPass.getText())){
                    try {
                        new ProzorTransakcije();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    primaryStage.close();
                }
            }
        });

        hBox.getChildren().addAll(lblUser,txtKorIme,lblpass,txtPass,btnPotvrdi);
        hBox.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(hBox,500,500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
