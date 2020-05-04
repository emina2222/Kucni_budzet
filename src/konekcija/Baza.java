package konekcija;

import javafx.event.ActionEvent;
import obavestenja.MessageBox;

import java.sql.*;

public class Baza {
    public Connection konekcija;

    public Connection getKonekcija(){
        String imeBaze="kucni_budzet";
        String user="root";
        String password="eminam0202";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            konekcija= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+imeBaze,user,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return konekcija;
    }



    public boolean login(String userName,String password){
        Baza baza=new Baza();
        Connection connection=baza.getKonekcija();
        int provera=0;
        try {
            Statement statement=connection.createStatement();
            String sql="SELECT * FROM korisnici WHERE korisnickoime = '"+userName+"' AND lozinka = '"+password+"';";
            ResultSet resultSet=statement.executeQuery(sql);

            if (resultSet.next()){
                MessageBox.show("Konekcija","Uspešno ste se ulogovali!");
                provera++;
            }else {
                MessageBox.show("Konekcija","Ne postoji korisnik sa ovim podacima");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(provera==1)
            return true;
        return false;
    }
}
