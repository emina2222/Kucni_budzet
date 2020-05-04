package transakcije;

import com.sun.prism.Mesh;
import obavestenja.MessageBox;

import java.io.*;
import java.util.ArrayList;

public class Datoteka {
    File datoteka;


    public String vratiSaldo() throws IOException {
        String sadrzaj="";
        datoteka=new File("evidencija\\ukupniSaldo");
        if(datoteka.exists()){
            FileReader fr=new FileReader(datoteka);
            BufferedReader br=new BufferedReader(fr);
            sadrzaj=br.readLine();
            br.close();
            fr.close();
        }
        else{
            MessageBox.show("Upozorenje","Datoteka ne postoji!");
        }
        return sadrzaj;
    }

    public String vratiStednju() throws IOException {
        String sadrzaj="";
        datoteka=new File("evidencija\\stednja");
        if(datoteka.exists()){
            FileReader fr=new FileReader(datoteka);
            BufferedReader br=new BufferedReader(fr);
            sadrzaj=br.readLine();
            br.close();
            fr.close();
        }
        else{
            MessageBox.show("Upozorenje","Datoteka ne postoji!");
        }
        return sadrzaj;
    }



    public void upisiNoviSaldo(double noviSaldo) throws IOException {
        datoteka=new File("evidencija\\ukupniSaldo");
        if(datoteka.exists()){
            FileWriter fw=new FileWriter(datoteka);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(noviSaldo+"");
            bw.close();
            fw.close();
        }
        else{
            MessageBox.show("Upozorenje","Datoteka ne postoji!");
        }
    }

    public void upisiNovuStednju(double novaStednja) throws IOException {
        datoteka=new File("evidencija\\stednja");
        if(datoteka.exists()){
            FileWriter fw=new FileWriter(datoteka);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write(novaStednja+"");
            bw.close();
            fw.close();
        }
        else{
            MessageBox.show("Upozorenje","Datoteka ne postoji!");
        }
    }



}
