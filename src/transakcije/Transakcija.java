package transakcije;

import konekcija.Baza;
import obavestenja.MessageBox;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Transakcija {
    private double ukupniSaldo;
    private double ukupnaStednja;
    private double ukupnoStan;
    private double ukupnoPutovanja;
    private double ukupnoHrana;
    private double ukupnoSkola;

    private double iznos;
    private String datum;
    private String namena;
    private String poruka;

    public Transakcija(double iznos,String datum,String namena,String poruka){
        this.iznos=iznos;
        this.datum=datum;
        this.namena=namena;
        this.poruka=poruka;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

   /* public LocalDate getDatum(){ return datum;}

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }*/

    public String getNamena() {
        return namena;
    }

    public void setNamena(String namena) {
        this.namena = namena;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    Datoteka dat=new Datoteka();

    public Transakcija() throws IOException {
        this.ukupniSaldo=Double.parseDouble(dat.vratiSaldo());
        this.ukupnaStednja=Double.parseDouble(dat.vratiStednju());
    }

    public void uplata(double iznos) throws IOException {
        this.ukupniSaldo+=iznos;
        dat.upisiNoviSaldo(this.ukupniSaldo);
    }

    public void isplata(double iznos) throws IOException {
        this.ukupniSaldo-=iznos;
        dat.upisiNoviSaldo(this.ukupniSaldo);
    }

    public void uplataNaStednju(double iznos) throws IOException {
        this.ukupnaStednja+=iznos;
        this.ukupniSaldo-=iznos;
        dat.upisiNovuStednju(this.ukupnaStednja);
        dat.upisiNoviSaldo(this.ukupniSaldo);
    }

    public void isplataSaStednje(double iznos) throws IOException {
        this.ukupnaStednja-=iznos;
        this.ukupniSaldo+=iznos;
        dat.upisiNovuStednju(this.ukupnaStednja);
        dat.upisiNoviSaldo(this.ukupniSaldo);
    }

    public double prikazUkupnogSalda(){
        return this.ukupniSaldo;
    }

    //public double ukupniIznosStanja() throws IOException {this.ukupniSaldo=Double.parseDouble(dat.vratiSaldo());}

    public double prikazUkupneStednje(){
        return this.ukupnaStednja;
    }

    public double prikazUEvrima(double iznos){
        return iznos*0.008522;
    }

    public double prikazStednjeUEvrima(){
        return this.ukupnaStednja*0.008522;
    }

    public double prikazIznosaUEvrima(double iznos){
        return iznos*0.008522;
    }


    public void unesiTransakciju(double iznos,String datum,String namena,String tekst){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        try{
            Statement statement=connection.createStatement();
            String upit="INSERT INTO rashodi (iznos,datum,namena,poruka) VALUES(?,?,?,?)";
            String sql=upit;
            PreparedStatement pstmt=connection.prepareStatement(sql);

            pstmt.setDouble(1, iznos);
            pstmt.setString(2, datum);
            pstmt.setString(3, namena);
            pstmt.setString(4,tekst);

            int rowAffected=pstmt.executeUpdate();

            if(rowAffected==1){
                MessageBox.show("Isplata","Isplatili ste "+iznos+" din.");
            }
            else{
                MessageBox.show("Isplata","Doslo je do greske.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Transakcija> prikaziTransakcijuZaDatum(String datum, String namena){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        Transakcija trIzBaze=new Transakcija(0.00,"","","");
        ArrayList<Transakcija> sveTransakcije=new ArrayList<>();

            try{
                String upit="SELECT * FROM rashodi WHERE datum='" + datum + "' AND namena='" + namena + "'";
                String sql=upit;
                PreparedStatement pstmt=connection.prepareStatement(sql);

                ResultSet red =pstmt.executeQuery();

                while(red.next()){
                    double iznosB=red.getDouble("iznos");
                    String datumBaza=red.getString("datum");
                    String namenaB=red.getString("namena");
                    String porukaB=red.getString("poruka");
                    trIzBaze=new Transakcija(iznosB,datumBaza,namenaB,porukaB);
                    sveTransakcije.add(trIzBaze);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return sveTransakcije;
    }




    @Override
    public String toString() {
        return "Transakcija{" +
                "iznos=" + iznos +
                ", datum='" + datum + '\'' +
                ", namena='" + namena + '\'' +
                ", poruka='" + poruka + '\'' +
                '}';
    }
}
