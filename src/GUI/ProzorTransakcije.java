package GUI;

import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import konekcija.Baza;
import obavestenja.MessageBox;
import transakcije.*;


import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProzorTransakcije extends Stage {
    Transakcija t=new Transakcija();
    TabPane tabPane=new TabPane();
    TransakcijeBaza transakcijeBaza=new TransakcijeBaza();
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProzorTransakcije() throws IOException {
        this.setTitle("Transakcije");

        MenuBar menuBar=new MenuBar();

        tabPane.setStyle("-fx-background-color: #8fffe7;");

        Label lblStednja=new Label("Štednja");
        Label lblIzvestaji=new Label("Izveštaji");
        Label lblStatistika=new Label("Statistika potrošnje");

        Menu prihodi=new Menu("Prihodi");
        Menu rashodi=new Menu("Rashodi");
        Menu stednja=new Menu("",lblStednja);
        Menu izvestaji=new Menu("",lblIzvestaji);
        Menu statistika=new Menu("Statistika");
        MenuItem stan=new MenuItem("Stan");
        MenuItem skola=new MenuItem("Skola");
        MenuItem hrana=new MenuItem("Hrana");
        MenuItem putovanja=new MenuItem("Putovanja");
        MenuItem racuni=new MenuItem("Računi");
        MenuItem plata=new MenuItem("Plata");
        MenuItem stipendija=new MenuItem("Stipendija");
        MenuItem potrosnja=new MenuItem("Potrosnja");
        MenuItem zarada=new MenuItem("Zarada");
        MenuItem stanje=new MenuItem("Stanje");
        MenuItem ostalo=new MenuItem("Ostalo");
        MenuItem stednjaStat=new MenuItem("Štednja");

        rashodi.getItems().addAll(stan,skola,hrana,putovanja,racuni,ostalo);
        prihodi.getItems().addAll(plata,stipendija);
        statistika.getItems().addAll(potrosnja,zarada,stednjaStat,stanje);
        menuBar.getMenus().addAll(prihodi,rashodi,stednja,izvestaji,statistika);
        VBox vBox=new VBox(menuBar);


        ComboBox<String> kategorijePotrosnja=new ComboBox<>();
        ObservableList<String> potrosnjaK=FXCollections.observableArrayList("Sve","Stan","Hrana","Putovanja","Skola","Računi","Ostalo");
        kategorijePotrosnja.setItems(potrosnjaK);

        ComboBox<String> kategorijeZarada=new ComboBox<>();
        kategorijeZarada.getItems().addAll("Sve","Plata","Stipendija");

        ComboBox<String> sveKategorije=new ComboBox<>();
        sveKategorije.getItems().addAll("Sve","Stan","Hrana","Putovanja","Skola","Računi","Plata","Stipendija","Stednja","Ostalo");

        EventHandler<ActionEvent> action=primeni();

        EventHandler<ActionEvent> akcijaPrihodi=uradi();


        stan.setOnAction(action);
        skola.setOnAction(action);
        putovanja.setOnAction(action);
        hrana.setOnAction(action);
        racuni.setOnAction(action);
        ostalo.setOnAction(action);

        plata.setOnAction(akcijaPrihodi);
        stipendija.setOnAction(akcijaPrihodi);


        lblStednja.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Tab tab=new Tab("Stednja");
                VBox vBox=new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");

                DatePicker datePicker=new DatePicker();
                datePicker.setValue(LocalDate.now());

                TableView table=new TableView();

                TableColumn colNamena = new TableColumn("Namena");
                TableColumn colDatum = new TableColumn("Datum");
                TableColumn colIznos = new TableColumn("Iznos");
                TableColumn colPoruka = new TableColumn("Poruka");

                table.getColumns().addAll(colNamena,colDatum,colIznos,colPoruka);

                colNamena.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("namena")
                );
                colDatum.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("datum")
                );
                colIznos.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,Double>("iznos")
                );
                colPoruka.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("poruka")
                );

                TextArea textArea=new TextArea();
                textArea.setPrefHeight(300);
                textArea.setPrefWidth(300);
                textArea.setText("");

                HBox hBox=new HBox(30);
                VBox vIznos=new VBox(5);

                Label lblIznos=new Label("Unesi iznos:");
                TextField txtIznos=new TextField();
                vIznos.getChildren().addAll(lblIznos,txtIznos);

                hBox.getChildren().addAll(vIznos);
                CheckBox chIsplata=new CheckBox("Isplati sa štednje");
                Button btnTransakcija=new Button("Izvrši transakciju");

                final ObservableList<TransakcijeBaza> data=FXCollections.observableArrayList();

                datePicker.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(datePicker.getValue()==null){
                            MessageBox.show("Upozorenje","Unesite datum.");
                        }
                        else {
                            table.getItems().clear();
                            LocalDate datumD = datePicker.getValue();
                            String datum = formatter.format(datumD);
                            String namena = lblStednja.getText().toLowerCase();

                            ArrayList<TransakcijeBaza> tIzBaze = transakcijeBaza.prikaziTransakcijuZaDatum(datum, namena);
                            for (TransakcijeBaza t : tIzBaze) {
                                data.add(t);
                            }
                            if (tIzBaze.size() == 0) {
                                MessageBox.show("Prikaz", "Nema podataka za ovu kategoriju.");
                            } else {
                                table.setItems(data);
                            }
                        }
                    }
                });


                btnTransakcija.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(txtIznos.getText()==null){
                            MessageBox.show("Upozorenje","Unesite iznos.");
                        }
                        else {
                            try{
                                double iznos = Double.parseDouble(txtIznos.getText());
                                String namena = lblStednja.getText().toLowerCase();
                                if (chIsplata.isSelected()) {
                                    if(iznos>t.prikazUkupneStednje()){
                                        MessageBox.show("Upozorenje","Nemate dovoljno sredstava za isplatu!");
                                    }
                                    else{
                                        try {
                                            if(iznos>t.prikazUkupneStednje()){
                                                MessageBox.show("Upozorenje","Nema dovoljno sredstava na štednji!");
                                            }
                                            else{
                                                t.isplataSaStednje(iznos);
                                                LocalDate datumD = datePicker.getValue();
                                                String datum = formatter.format(datumD);
                                                transakcijeBaza.unesiTransakciju(namena, datum, iznos, "isplata");
                                                TransakcijeBaza tB = new TransakcijeBaza(namena, datum, iznos, "isplata");
                                                data.add(tB);
                                                table.setItems(data);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    try {
                                        if(iznos>t.prikazUkupnogSalda()){
                                            MessageBox.show("Upozorenje","Nema dovoljno sredstava na računu!");
                                        }
                                        else{
                                            t.uplataNaStednju(iznos);
                                            LocalDate datumD = datePicker.getValue();
                                            String datum = formatter.format(datumD);
                                            transakcijeBaza.unesiTransakciju(namena, datum, iznos, "uplata");
                                            TransakcijeBaza tB = new TransakcijeBaza(namena, datum, iznos, "uplata");
                                            data.add(tB);
                                            table.setItems(data);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }catch (NumberFormatException e){
                                MessageBox.show("Upozorenje","Unesite broj.");
                            }
                        }
                    }
                });

                /*btnStorno.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(table.getSelectionModel().getSelectedItem()==null){
                            MessageBox.show("Upozorenje","Izaberite transakciju koju želite da stornirate.");
                        }
                        else {
                            TransakcijeBaza izabran = (TransakcijeBaza) table.getSelectionModel().getSelectedItem();

                            int idIzabranog = izabran.getId();

                            transakcijeBaza.storniraj(idIzabranog);
                            table.getSelectionModel().clearSelection();
                            data.remove(izabran);
                            table.setItems(data);
                            if(izabran.getPoruka().equals("uplata")){
                                try {
                                    t.isplataSaStednje(izabran.getIznos());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(izabran.getPoruka().equals("isplata")){
                                try {
                                    t.uplataNaStednju(izabran.getIznos());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });*/

                vBox.getChildren().addAll(datePicker,table,hBox,chIsplata,btnTransakcija);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }
        });

        lblIzvestaji.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Tab tab = new Tab("Izvestaji");
                VBox vBox = new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");


                Label lblKat=new Label("Izaberi kategoriju:");

                HBox hBox=new HBox(30);
                Label lblDatumOd=new Label("Datum od:");
                DatePicker datePicker1 = new DatePicker();
                datePicker1.setValue(LocalDate.now());
                Label lblDatumDo=new Label("Datum do:");
                DatePicker datePicker2=new DatePicker();
                hBox.getChildren().addAll(lblDatumOd,datePicker1,lblDatumDo,datePicker2);

                Button btnPretrazi=new Button("Traži");
                Button btnStorno=new Button("Storniraj");

                TableView table=new TableView();

                TableColumn colNamena = new TableColumn("Namena");
                TableColumn colDatum = new TableColumn("Datum");
                TableColumn colIznos = new TableColumn("Iznos");
                TableColumn colPoruka = new TableColumn("Poruka");

                table.getColumns().addAll(colNamena,colDatum,colIznos,colPoruka);

                colNamena.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("namena")
                );
                colDatum.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("datum")
                );
                colIznos.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,Double>("iznos")
                );
                colPoruka.setCellValueFactory(
                        new PropertyValueFactory<TransakcijeBaza,String>("poruka")
                );


                final ObservableList<TransakcijeBaza> data=FXCollections.observableArrayList();

                btnPretrazi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        table.getItems().clear();
                        if(sveKategorije.getSelectionModel().getSelectedItem()==null || datePicker1.getValue()==null || datePicker2.getValue()==null){
                            MessageBox.show("Upozorenje","Unesite potrebne parametre.");
                        }
                        else {
                            String namena = sveKategorije.getSelectionModel().getSelectedItem().toLowerCase();
                            LocalDate d1 = datePicker1.getValue();
                            LocalDate d2 = datePicker2.getValue();

                            LocalDate start = d1;
                            LocalDate end = d2;
                            List<LocalDate> totalDates = new ArrayList<>();
                            ArrayList<String> datumi = new ArrayList<>();

                            while (!start.isAfter(end)) {
                                totalDates.add(start);
                                start = start.plusDays(1);
                            }

                            for (LocalDate l : totalDates) {
                                String d = "";
                                d = formatter.format(l);
                                datumi.add(d);
                            }

                            ArrayList<TransakcijeBaza> prikazSvih = transakcijeBaza.prikaziSveTransakcijeZaDatum(datumi, namena);
                            for (TransakcijeBaza t : prikazSvih) {
                                data.add(t);
                            }
                            if (prikazSvih.size() == 0) {
                                MessageBox.show("Prikaz", "Nema podataka za ovu kategoriju.");
                            } else {
                                table.setItems(data);
                            }
                        }
                    }
                });

                btnStorno.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(table.getSelectionModel().getSelectedItem()==null){
                            MessageBox.show("Upozorenje","Izaberite transakciju koju želite da stornirate.");
                        }
                        else {
                            TransakcijeBaza izabran = (TransakcijeBaza) table.getSelectionModel().getSelectedItem();

                            int idIzabranog = izabran.getId();
                            if(izabran.getNamena().equals("štednja")){
                                MessageBox.show("Upozorenje","Ne možete stornirati štednju.");
                            }
                            else{
                                transakcijeBaza.storniraj(idIzabranog);
                                table.getSelectionModel().clearSelection();
                                data.remove(izabran);
                                table.setItems(data);
                            }

                            if(izabran.getNamena().equals("plata") || izabran.getNamena().equals("stipendija")){
                                try {
                                    t.isplata(izabran.getIznos());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(izabran.getNamena().equals("štednja") && izabran.getPoruka().equals("uplata")) {
                                try {
                                    t.uplata(0.00);
                                    t.isplataSaStednje(0.00);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if(izabran.getNamena().equals("štednja") && izabran.getPoruka().equals("isplata")){
                                    try {
                                        t.uplata(0.00);
                                        t.uplataNaStednju(0.00);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                            }
                            else{
                                try {
                                    t.uplata(izabran.getIznos());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

                vBox.getChildren().addAll(lblKat,sveKategorije,hBox,btnPretrazi,table,btnStorno);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }
        });



        potrosnja.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tab tab = new Tab("Potrosnja");
                VBox vBox = new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");

                Label lblKat=new Label("Izaberi kategoriju:");
                ComboBox<String> kategorije=new ComboBox<>();
                kategorije.getItems().addAll("Sve","Stan","Hrana","Putovanja","Skola");

                HBox hBox=new HBox(30);
                Label lblDatumOd=new Label("Datum od:");
                DatePicker datePicker1 = new DatePicker();
                datePicker1.setValue(LocalDate.now());
                Label lblDatumDo=new Label("Datum do:");
                DatePicker datePicker2=new DatePicker();
                hBox.getChildren().addAll(lblDatumOd,datePicker1,lblDatumDo,datePicker2);

                Button btnPretrazi=new Button("Traži");

                HBox hBox1=new HBox(20);
                Label lblPrikaz=new Label("Ukupno potrošeno");
                TextField txtUkupno=new TextField();
                txtUkupno.setStyle("-fx-font-size: 24 arial;");
                lblPrikaz.setStyle("-fx-font-size: 24 arial;");
                Text txtEvri=new Text();
                txtEvri.setStyle("-fx-font: 24 arial;");
                hBox1.getChildren().addAll(lblPrikaz,txtUkupno,txtEvri);

                btnPretrazi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (kategorijePotrosnja.getSelectionModel().getSelectedItem() == null || datePicker1.getValue() == null || datePicker2.getValue() == null) {
                            MessageBox.show("Upozorenje", "Niste uneli potrebne parametre za pretragu!");
                        } else {
                            String namena = kategorijePotrosnja.getSelectionModel().getSelectedItem().toLowerCase();
                            double ukupno = 0;
                            LocalDate d1 = datePicker1.getValue();
                            LocalDate d2 = datePicker2.getValue();
                            //String pocetniDatum = formatter.format(d1);
                            //String krajnjiDatum = formatter.format(d2);

                            LocalDate start = d1;
                            LocalDate end = d2;
                            List<LocalDate> totalDates = new ArrayList<>();
                            ArrayList<String> datumi = new ArrayList<>();

                            while (!start.isAfter(end)) {
                                totalDates.add(start);
                                start = start.plusDays(1);
                            }

                            for (LocalDate l : totalDates) {
                                String d = "";
                                d = formatter.format(l);
                                datumi.add(d);
                            }

                            ukupno = transakcijeBaza.prikaziPotrosnjuUPeriodu(datumi, namena);

                            txtUkupno.setText(ukupno + "");
                            txtEvri.setText(t.prikazUEvrima(ukupno) + " €");
                        }
                    }
                });

                vBox.getChildren().addAll(lblKat,kategorijePotrosnja,hBox,btnPretrazi,hBox1);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }

        });

        zarada.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tab tab = new Tab("Zarada");
                VBox vBox = new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");

                Label lblKat=new Label("Izaberi kategoriju:");
                ComboBox<String> kategorije=new ComboBox<>();
                kategorije.getItems().addAll("Sve","Plata","Stipendija");

                HBox hBox=new HBox(30);
                Label lblDatumOd=new Label("Datum od:");
                DatePicker datePicker1 = new DatePicker();
                datePicker1.setValue(LocalDate.now());
                Label lblDatumDo=new Label("Datum do:");
                DatePicker datePicker2=new DatePicker();
                hBox.getChildren().addAll(lblDatumOd,datePicker1,lblDatumDo,datePicker2);

                Button btnPretrazi=new Button("Traži");

                HBox hBox1=new HBox(20);
                Label lblPrikaz=new Label("Ukupno zarađeno:");
                TextField txtUkupno=new TextField();
                txtUkupno.setStyle("-fx-font-size: 24 arial;");
                lblPrikaz.setStyle("-fx-font-size: 24 arial;");
                Text txtEvri=new Text();
                txtEvri.setStyle("-fx-font: 24 arial;");
                hBox1.getChildren().addAll(lblPrikaz,txtUkupno,txtEvri);



                btnPretrazi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (kategorijeZarada.getSelectionModel().getSelectedItem() == null || datePicker1.getValue() == null || datePicker2.getValue() == null) {
                            MessageBox.show("Upozorenje", "Niste uneli potrebne parametre za pretragu!");
                        } else {
                            String namena = kategorijeZarada.getSelectionModel().getSelectedItem().toLowerCase();
                            double ukupno = 0;
                            LocalDate d1 = datePicker1.getValue();
                            LocalDate d2 = datePicker2.getValue();
                            //String pocetniDatum = formatter.format(d1);
                            //String krajnjiDatum = formatter.format(d2);

                            LocalDate start = d1;
                            LocalDate end = d2;
                            List<LocalDate> totalDates = new ArrayList<>();
                            ArrayList<String> datumi = new ArrayList<>();

                            while (!start.isAfter(end)) {
                                totalDates.add(start);
                                start = start.plusDays(1);
                            }

                            for (LocalDate l : totalDates) {
                                String d = "";
                                d = formatter.format(l);
                                datumi.add(d);
                            }

                            ukupno = transakcijeBaza.prikaziZaraduUPeriodu(datumi, namena);

                            txtUkupno.setText(ukupno + "");
                            txtEvri.setText(t.prikazUEvrima(ukupno) + " €");
                        }
                    }
                });

                vBox.getChildren().addAll(lblKat,kategorijeZarada,hBox,btnPretrazi,hBox1);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }
        });

        stednjaStat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tab tab = new Tab("Štednja");
                VBox vBox = new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");

                HBox hBox=new HBox(30);
                Label lblDatumOd=new Label("Datum od:");
                DatePicker datePicker1 = new DatePicker();
                datePicker1.setValue(LocalDate.now());
                Label lblDatumDo=new Label("Datum do:");
                DatePicker datePicker2=new DatePicker();
                hBox.getChildren().addAll(lblDatumOd,datePicker1,lblDatumDo,datePicker2);

                Button btnPretrazi=new Button("Traži");

                TextArea textArea=new TextArea();

                btnPretrazi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        textArea.setText("");
                        if (datePicker1.getValue() == null || datePicker2.getValue() == null) {
                            MessageBox.show("Upozorenje", "Niste uneli potrebne parametre za pretragu!");
                        } else {
                            double niz[]=new double[2];
                            double ukupnoUplata = 0;
                            double ukupnoIsplata=0;
                            LocalDate d1 = datePicker1.getValue();
                            LocalDate d2 = datePicker2.getValue();

                            LocalDate start = d1;
                            LocalDate end = d2;
                            List<LocalDate> totalDates = new ArrayList<>();
                            ArrayList<String> datumi = new ArrayList<>();

                            while (!start.isAfter(end)) {
                                totalDates.add(start);
                                start = start.plusDays(1);
                            }

                            for (LocalDate l : totalDates) {
                                String d = "";
                                d = formatter.format(l);
                                datumi.add(d);
                            }

                            niz=transakcijeBaza.prikaziStednjuUPeriodu(datumi);
                            ukupnoUplata=niz[0];
                            ukupnoIsplata=niz[1];

                            textArea.appendText("Ukupno uplaćeno na štednju za ovaj period: "+ukupnoUplata+"\n");
                            textArea.appendText("Ukupno isplaćeno sa štednje (vraćeno u tok) za ovaj period: "+ukupnoIsplata+"\n");
                            textArea.appendText("Ukupan iznos na štednji: "+t.prikazUkupneStednje());
                        }
                    }
                });

                vBox.getChildren().addAll(hBox,btnPretrazi,textArea);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }
        });

        stanje.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tab tab = new Tab("Stanje");
                VBox vBox = new VBox(20);
                tab.setStyle("-fx-font-size: 14 arial;");


                HBox hBox=new HBox(30);
                Label lblDatumOd=new Label("Datum od:");
                DatePicker datePicker1 = new DatePicker();
                datePicker1.setValue(LocalDate.now());
                Label lblDatumDo=new Label("Datum do:");
                DatePicker datePicker2=new DatePicker();
                hBox.getChildren().addAll(lblDatumOd,datePicker1,lblDatumDo,datePicker2);

                Button btnPretrazi=new Button("Prikaži");

                TextArea textArea = new TextArea();
                textArea.setPrefHeight(300);
                textArea.setPrefWidth(300);
                textArea.setText("");


                textArea.setText("");


                btnPretrazi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (datePicker1.getValue() == null || datePicker2.getValue() == null) {
                            MessageBox.show("Upozorenje", "Niste uneli potrebne parametre za pretragu!");
                        }
                        else {
                            textArea.setText("");
                            String namena = "sve";
                            double ukupnoPotroseno = 0;
                            double ukupnaZarada = 0;
                            LocalDate d1 = datePicker1.getValue();
                            LocalDate d2 = datePicker2.getValue();

                            LocalDate start = d1;
                            LocalDate end = d2;
                            List<LocalDate> totalDates = new ArrayList<>();
                            ArrayList<String> datumi = new ArrayList<>();

                            while (!start.isAfter(end)) {
                                totalDates.add(start);
                                start = start.plusDays(1);
                            }

                            for (LocalDate l : totalDates) {
                                String d = "";
                                d = formatter.format(l);
                                datumi.add(d);
                            }

                            double niz[]=new double[2];
                            niz=transakcijeBaza.prikaziStednjuUPeriodu(datumi);
                            double ukupnaUplacenaStednja=niz[0];
                            double ukupnaIsplacenaStednja=niz[1];

                            ukupnaZarada = transakcijeBaza.prikaziZaraduUPeriodu(datumi, namena);
                            ukupnaZarada-=ukupnaUplacenaStednja;
                            ukupnaZarada+=ukupnaIsplacenaStednja;
                            ukupnoPotroseno = transakcijeBaza.prikaziPotrosnjuUPeriodu(datumi, namena);

                            double razlika = ukupnaZarada - ukupnoPotroseno;

                            textArea.appendText("Ukupno stanje za ovaj period: "+razlika + "\n");
                            textArea.appendText("Ukupno stanje trenutno: "+t.prikazUkupnogSalda()+"\n\n");

                            textArea.appendText("Uplaćeno na štednju za ovaj period: "+ukupnaUplacenaStednja+"\n"+"Vraćeno u tok za ovaj period: "+ukupnaIsplacenaStednja+"\n");
                            textArea.appendText("Ukupan iznos na štednji: "+t.prikazUkupneStednje()+"\n");

                            if (razlika > 0) {
                                textArea.appendText("Stanje je pozitivno, prihodi su bili veći od rashoda.");
                            } else if (razlika == 0) {
                                textArea.appendText("Stanje je na nuli.");
                            } else {
                                textArea.appendText("Stanje je negativno, rashodi su bili veći od prihoda.");
                            }
                        }
                    }
                });

                vBox.getChildren().addAll(hBox,btnPretrazi,textArea);
                tab.setContent(vBox);
                tabPane.getTabs().add(tab);
            }
        });

        vBox.getChildren().addAll(tabPane);

        this.setScene(new Scene(vBox,800,700));
        this.show();
    }

    private EventHandler<ActionEvent> uradi() {
        return new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String nazivKategorije = mItem.getText();
                prikazTabovaPrihodi(nazivKategorije);
            }
        };
    }


    private EventHandler<ActionEvent> primeni() {
        return new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String nazivKategorije = mItem.getText();
                prikazTabova(nazivKategorije);
            }
        };
    }



    public void prikazTabova(String nazivTaba){
        Tab tab=new Tab(nazivTaba);
        VBox vBox=new VBox(20);
        tab.setStyle("-fx-font-size: 14 arial;");

        Label lblDatum=new Label("Izaberite datum za transakciju:");
        DatePicker datePicker=new DatePicker();
        datePicker.setValue(LocalDate.now());

        TableView table=new TableView();

        TableColumn colNamena = new TableColumn("Namena");
        TableColumn colDatum = new TableColumn("Datum");
        TableColumn colIznos = new TableColumn("Iznos");
        TableColumn colPoruka = new TableColumn("Poruka");

        table.getColumns().addAll(colNamena,colDatum,colIznos,colPoruka);

        colNamena.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("namena")
        );
        colDatum.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("datum")
        );
        colIznos.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,Double>("iznos")
        );
        colPoruka.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("poruka")
        );


        HBox hBox=new HBox(30);
        VBox vIznos=new VBox(5);
        VBox vTekst=new VBox(5);

        Label lblIznos=new Label("Unesi iznos");
        TextField txtIznos=new TextField();
        vIznos.getChildren().addAll(lblIznos,txtIznos);

        Label lblPoruka=new Label("Unesi tekst namene");
        TextField txtTekst=new TextField();
        vTekst.getChildren().addAll(lblPoruka,txtTekst);

        hBox.getChildren().addAll(vIznos,vTekst);
        Button btnTransakcija=new Button("Izvrsi transakciju");
        Button btnStorno=new Button("Storniraj");



        final ObservableList<TransakcijeBaza> data=FXCollections.observableArrayList();

        datePicker.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    table.getItems().clear();
                    LocalDate datumD=datePicker.getValue();
                    String datum=formatter.format(datumD);
                    String namena=nazivTaba.toLowerCase();

                    ArrayList<TransakcijeBaza> izBaze = transakcijeBaza.prikaziTransakcijuZaDatum(datum,namena);
                    for(TransakcijeBaza t:izBaze){
                        data.add(t);
                    }
                    if(izBaze.size()==0){
                        MessageBox.show("Prikaz","Nema podataka za ovu kategoriju.");
                    }
                    else{
                        table.setItems(data);
                    }
                }
        });



        btnTransakcija.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double iznos=0.0;
                if(txtIznos.getText()==null || txtTekst.getText()==null  || datePicker.getValue()==null){
                    MessageBox.show("Upozorenje","Unesite potrebne parametre.");
                }
                else {
                    try {
                        iznos = Double.parseDouble(txtIznos.getText());
                        if(iznos>t.prikazUkupnogSalda()){
                            MessageBox.show("Upozorenje","Nemate dovoljno sredstava za isplatu!");
                        }
                        else{
                            String poruka = txtTekst.getText();
                            try {
                                t.isplata(iznos);
                                LocalDate datumD = datePicker.getValue();
                                String datum = formatter.format(datumD);
                                String namena = nazivTaba.toLowerCase();
                                TransakcijeBaza tB = new TransakcijeBaza(namena, datum, iznos, poruka);
                                transakcijeBaza.unesiTransakciju(namena, datum, iznos, poruka);
                                data.add(tB);
                                table.setItems(data);
                            } catch (IOException| NumberFormatException e) {
                                e.printStackTrace();
                                MessageBox.show("Upozorenje","Greska");
                            }
                        }
                    }catch (NumberFormatException e){
                        MessageBox.show("Upozorenje","Unesite broj.");
                    }

                }
            }
        });

        btnStorno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(table.getSelectionModel().getSelectedItem()==null){
                    MessageBox.show("Upozorenje","Izaberite transakciju koju želite da stornirate.");
                }
                else {
                    TransakcijeBaza izabran = (TransakcijeBaza) table.getSelectionModel().getSelectedItem();
                    table.getSelectionModel().clearSelection();
                    data.remove(izabran);
                    table.setItems(data);
                    int idIzabranog = izabran.getId();
                    try {
                        t.uplata(izabran.getIznos());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    transakcijeBaza.storniraj(idIzabranog);
                }
            }
        });

        vBox.getChildren().addAll(lblDatum,datePicker,table,hBox,btnTransakcija,btnStorno);
        tab.setContent(vBox);
        tabPane.getTabs().add(tab);
    }

    public void prikazTabovaPrihodi(String kategorijaP){
        VBox vBoxPrihodi=new VBox(20);
        Tab tab=new Tab(kategorijaP);

        tab.setStyle("-fx-font-size: 14 arial;");

        Label lblDatum=new Label("Izaberite datum za transakciju:");
        DatePicker datePicker=new DatePicker();
        datePicker.setValue(LocalDate.now());

        TableView table=new TableView();

        TableColumn colNamena = new TableColumn("Namena");
        TableColumn colDatum = new TableColumn("Datum");
        TableColumn colIznos = new TableColumn("Iznos");
        TableColumn colPoruka = new TableColumn("Poruka");

        table.getColumns().addAll(colNamena,colDatum,colIznos,colPoruka);

        colNamena.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("namena")
        );
        colDatum.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("datum")
        );
        colIznos.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,Double>("iznos")
        );
        colPoruka.setCellValueFactory(
                new PropertyValueFactory<TransakcijeBaza,String>("poruka")
        );

        Label lblIznos=new Label("Unesi iznos:");
        TextField txtIznos=new TextField();
        Button btnIzvrsi=new Button("Unesi u bazu");
        Button btnStorno=new Button("Storniraj");

        /*Label lblSaldo=new Label("Ukupan saldo");
        HBox hBox1=new HBox(20);
        hBox1.setAlignment(Pos.CENTER);
        Text txtSaldo=new Text();
        Text txtEvri=new Text();
        txtSaldo.setStyle("-fx-font: 20 arial;");
        lblSaldo.setStyle("-fx-font: 20 arial;");
        txtSaldo.setText(t.prikazUkupnogSalda()+"");
        txtEvri.setStyle("-fx-font: 20 arial;");
        txtEvri.setText("("+t.prikazSaldaUEvrima()+" € )");*/


        final ObservableList<TransakcijeBaza> data=FXCollections.observableArrayList();

        datePicker.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    table.getItems().clear();
                    LocalDate datumD=datePicker.getValue();
                    String datum=formatter.format(datumD);
                    String namena=kategorijaP.toLowerCase();

                    ArrayList<TransakcijeBaza> izBaze = transakcijeBaza.prikaziTransakcijuZaDatum(datum,namena);
                    for(TransakcijeBaza t:izBaze){
                        data.add(t);
                    }
                    if(izBaze.size()==0){
                        MessageBox.show("Prikaz","Nema podataka za ovu kategoriju.");
                    }
                    else{
                        table.setItems(data);
                    }
                }
        });

        btnIzvrsi.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(txtIznos.getText()==null || datePicker.getValue()==null){
                        MessageBox.show("Upozorenje","Unesite potrebne parametre.");
                    }
                    else {
                        try{
                            double iznos = Double.parseDouble(txtIznos.getText());
                            try {
                                t.uplata(iznos);
                                LocalDate datumD = datePicker.getValue();
                                String datum = formatter.format(datumD);
                                String namena = kategorijaP.toLowerCase();
                                TransakcijeBaza tB = new TransakcijeBaza(namena, datum, iznos, "uplata");
                                transakcijeBaza.unesiTransakciju(namena, datum, iznos, "uplata");
                                data.add(tB);
                                table.setItems(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }catch (NumberFormatException e){
                            MessageBox.show("Upozorenje","Unesite broj.");
                        }
                    }
                }
            });

        btnStorno.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(table.getSelectionModel().getSelectedItem()==null){
                    MessageBox.show("Upozorenje","Izaberite transakciju koju želite da stornirate.");
                }
                else {
                    TransakcijeBaza izabran = (TransakcijeBaza) table.getSelectionModel().getSelectedItem();
                    table.getSelectionModel().clearSelection();
                    data.remove(izabran);
                    table.setItems(data);
                    int idIzabranog = izabran.getId();

                    try {
                        t.isplata(izabran.getIznos());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    transakcijeBaza.storniraj(idIzabranog);
                }
            }
        });

            vBoxPrihodi.getChildren().addAll(lblDatum,datePicker,table,lblIznos,txtIznos,btnIzvrsi,btnStorno);
            tab.setContent(vBoxPrihodi);
            tabPane.getTabs().add(tab);
    }


}
