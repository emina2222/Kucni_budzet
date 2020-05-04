package transakcije;

import konekcija.Baza;
import obavestenja.MessageBox;

import java.sql.*;
import java.util.ArrayList;

public class TransakcijeBaza {

    int id;
    private String namena;
    private String datum;
    private double iznos;
    private String poruka;

    public TransakcijeBaza(){

    }

    public TransakcijeBaza(String namena,String datum,double iznos,String poruka){
        this.namena=namena;
        this.datum=datum;
        this.iznos=iznos;
        this.poruka=poruka;
    }

    public TransakcijeBaza(int id, String namena, String datum, double iznos, String poruka) {
        this.id = id;
        this.namena = namena;
        this.datum = datum;
        this.iznos = iznos;
        this.poruka = poruka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamena() {
        return namena;
    }

    public void setNamena(String namena) {
        this.namena = namena;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public void unesiTransakciju(String namena, String datum, double iznos, String tekst){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        try{
            Statement statement=connection.createStatement();
            String upit="INSERT INTO transakcije (namena,datum,iznos,poruka) VALUES(?,?,?,?)";
            String sql=upit;
            PreparedStatement pstmt=connection.prepareStatement(sql);

            pstmt.setString(1, namena);
            pstmt.setString(2, datum);
            pstmt.setDouble(3, iznos);
            pstmt.setString(4,tekst);

            int rowAffected=pstmt.executeUpdate();

            if(rowAffected==1){
                MessageBox.show("Transakcija","Uspešno ste izvršili transakciju.");
            }
            else{
                MessageBox.show("Transakcija","Došlo je do greške.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    public ArrayList<TransakcijeBaza> prikaziTransakcijuZaDatum(String datum, String namena){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        TransakcijeBaza trIzBaze=new TransakcijeBaza("","",0.00,"");
        ArrayList<TransakcijeBaza> sveTransakcije=new ArrayList<>();

        try{
            String upit="SELECT * FROM transakcije WHERE datum='" + datum + "' AND namena='" + namena + "'";
            String sql=upit;
            PreparedStatement pstmt=connection.prepareStatement(sql);

            ResultSet red =pstmt.executeQuery();

            while(red.next()){
                int id=red.getInt("id");
                double iznosB=red.getDouble("iznos");
                String datumBaza=red.getString("datum");
                String namenaB=red.getString("namena");
                String porukaB=red.getString("poruka");
                trIzBaze=new TransakcijeBaza(id,namenaB,datumBaza,iznosB,porukaB);
                sveTransakcije.add(trIzBaze);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }

        return sveTransakcije;
    }


    public ArrayList<TransakcijeBaza> prikaziSveTransakcijeZaDatum(ArrayList<String> datumi,String namena){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        TransakcijeBaza trIzBaze=new TransakcijeBaza("","",0.00,"");
        ArrayList<TransakcijeBaza> sveTransakcije=new ArrayList<>();

        if(namena.contains("sve")) {
            try {
                String upit = "SELECT * FROM transakcije";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }
        }
        else{
            try {
                String upit="SELECT * FROM transakcije WHERE namena='" + namena+"'";
                //String upit = "SELECT * FROM transakcije";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }
        }

        return sveTransakcije;
    }


    public double prikaziPotrosnjuUPeriodu(ArrayList<String> datumi,String namena){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        TransakcijeBaza trIzBaze=new TransakcijeBaza("","",0.00,"");
        ArrayList<TransakcijeBaza> sveTransakcije=new ArrayList<>();
        String odgovor="";
        double ukupnoPotroseno=0;

        if(namena.contains("sve")) {
            try {
                String upit = "SELECT * FROM transakcije WHERE namena IN('stan','hrana','putovanja','skola','ostalo','računi')";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }

            for(TransakcijeBaza t:sveTransakcije){
                ukupnoPotroseno+=t.getIznos();
            }

        }
        else{
            try {
                String upit="SELECT * FROM transakcije WHERE namena='" + namena+"'";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

                for(TransakcijeBaza t:sveTransakcije){
                    ukupnoPotroseno+=t.getIznos();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }
        }
        return ukupnoPotroseno;
    }

    public double prikaziZaraduUPeriodu(ArrayList<String> datumi,String namena){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        TransakcijeBaza trIzBaze=new TransakcijeBaza("","",0.00,"");
        ArrayList<TransakcijeBaza> sveTransakcije=new ArrayList<>();
        String odgovor="";
        double ukupnaZarada=0;

        if(namena.contains("sve")) {
            try {
                String upit = "SELECT * FROM transakcije WHERE namena IN('plata','stipendija')";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }

            for(TransakcijeBaza t:sveTransakcije){
                    ukupnaZarada+=t.getIznos();
            }
        }
        else{
            try {
                String upit="SELECT * FROM transakcije WHERE namena='" + namena+"'";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

                for(TransakcijeBaza t:sveTransakcije){
                    ukupnaZarada+=t.getIznos();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }
        }

        return ukupnaZarada;
    }

    public double[] prikaziStednjuUPeriodu(ArrayList<String> datumi){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        TransakcijeBaza trIzBaze=new TransakcijeBaza("","",0.00,"");
        ArrayList<TransakcijeBaza> sveTransakcije=new ArrayList<>();
        double ukupnaIsplacenaStednja=0;
        double ukupnaUplacenaStednja=0;
        double niz[]={0,0};

            try {
                String upit = "SELECT * FROM transakcije WHERE namena IN('štednja')";
                String sql = upit;
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet red = pstmt.executeQuery();

                while (red.next()) {
                    int id=red.getInt("id");
                    String datumBaza = red.getString("datum");
                    double iznosB = red.getDouble("iznos");
                    String namenaB = red.getString("namena");
                    String porukaB = red.getString("poruka");
                    trIzBaze = new TransakcijeBaza(id,namenaB, datumBaza, iznosB, porukaB);
                    for (String datum : datumi) {
                        if (datum.contains(datumBaza)) {
                            sveTransakcije.add(trIzBaze);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }

            for(TransakcijeBaza t:sveTransakcije){
                if(t.getPoruka().equals("uplata"))
                    ukupnaUplacenaStednja+=t.getIznos();
                else{
                    ukupnaIsplacenaStednja+=t.getIznos();
                }
            }
        niz[0]=ukupnaUplacenaStednja;
        niz[1]=ukupnaIsplacenaStednja;

        return niz;
    }

    public void storniraj(int id){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        try{
            Statement statement=connection.createStatement();
            String upit="DELETE FROM transakcije WHERE id='" + id+"'";
            String sql=upit;
            PreparedStatement pstmt=connection.prepareStatement(sql);

            int rowAffected=pstmt.executeUpdate();

            if(rowAffected==1){
                MessageBox.show("Transakcija","Uspešno ste stornirali transakciju.");
            }
            else{
                MessageBox.show("Transakcija","Došlo je do greške.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }


    @Override
    public String toString() {
        return "Transakcija{" +
                "namena=" + namena +
                ", datum='" + datum + '\'' +
                ", iznos='" + iznos + '\'' +
                ", poruka='" + poruka + '\'' +
                '}';
    }
}
