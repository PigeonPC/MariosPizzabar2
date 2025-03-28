

import java.io.*;
import java.util.ArrayList;

//Class til at haandtere load og save ordrehistorik til fil
public class FileIO {

    //Constructor
    //throws goer at man ikke kan skabe nyt objekt af FileIO uden at tilfoeje try/catch blocks
    public FileIO() throws IOException {
        //Har bare vaeret brugt til debugging. Skal maaske fjernes
//        System.out.println("FileIO object til filhaandeting instantiated");
    }

    //Tilfoejer et ordre objekt til fil
    public void write(Ordre ordre) {


        try {
            //Stream til bytes til filobjekt skabes
            //Det angives med true at data tilfoejes til slutningen af fil frem for at overskrive
            FileOutputStream Fout = new FileOutputStream("testOrdreHistorik1", true);

            //Tjekker hvor mange bytes der allerede maatte vaere skrevet til denne fil
            //Hvis intet endnu er skrevet saa bruges standard ObjectOutputStream class
            //Hvis filen i forvejen har data saa bruges projektets AppendObjectOutputStream class
            //ObjectOutputStream class er kun lavet til overwrite
            //Den tilfoejer en header til de streamede data
            //Hvis man tvinger den til at udvide en fil med flere gange write saa kommer der flere headers og filen bliver ulaeselig
            //AppendObjectOutputStream extender/nedarver og overrider brugen af ny header
            ObjectOutputStream Oout = Fout.getChannel().position() == 0 ?
                    new ObjectOutputStream(Fout) :
                    new AppendObjectOutputStream(Fout);

            //Send hele ordre objektet som stream til filen
            Oout.writeObject(ordre);

            //Luk ObjectOutputStream
            Oout.close();

            //Luk FileOutputStream
            Fout.close();

        } catch (IOException e) {
            System.err.println("Kunne ikke skrive ordre til fil");
            e.printStackTrace();
        }


    }

    //Laeser et ordreobjekt fra fil
    public ArrayList<Ordre> read() {

        //Ny ArrayList som ender med at blive returneret
        ArrayList<Ordre> ordreList = new ArrayList<>();

        //Declare ObjectInputStream uden for loop
        ObjectInputStream Oout = null;
        FileInputStream Fin = null;

        try {
            //Aaben stream til fil for at modtage bytes

            try {
                Fin = new FileInputStream("testOrdreHistorik1");
            } catch (FileNotFoundException e) {
                System.err.println("Filen findes ikke");
            }
            //Aaben ObjectInputStream til at laese helt objekt af ordre
            Oout = new ObjectInputStream(Fin);

            //looper til break
            while (true) {
                try {
                    //Laes ordre objekt ind i ArrayList
                    ordreList.add((Ordre) Oout.readObject());

                } catch (EOFException e) {
                    //Exception opstaar naar sidste objektdata er laest og der forsoeges at laeses igen
                    //break ud af loop
                    break;
                }
            }

            //Close FileOutputStream
            Fin.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Laesefejl fra ordrehistorik-fil. Eller inkompatible objekter");
            e.printStackTrace();
        }


        try {
            //Tjek at ObjectInputStream stadig er forbundet til variablen Oout foer call close
            if (Oout != null) {
                Oout.close();
            }


        } catch (IOException e) {
            System.err.println("ObjectInputStream error efter laesning");
            e.printStackTrace();
        }

        //returner ordreliste med alle ordre fra filen
        return ordreList;
    }

    public void deleteHistorikOrdreFile() {
        File fileDeleter = new File("testOrdreHistorik1");
        if (fileDeleter.exists()) {
            if (!fileDeleter.delete()) {
                System.err.println("error deleting file historik ordre");
            }
        }
    }

    //Tilfoejer aktive ordre til egen backup fil
    public void writeActiveOrderArrayToFile(ArrayList<Ordre> aktiveOrdre) {



        try {
            //Stream til bytes til filobjekt skabes
            //Der aabnes en fil stream til overwrite (ingen true som ekstra parameter)
            FileOutputStream Fout = new FileOutputStream("aktiveOrdreArrayBackup");


            //ObjectOutputStream object som fungerer med overwrite
            ObjectOutputStream Oout = new ObjectOutputStream(Fout);


            //Send hele ordre objektet som stream til filen
            Oout.writeObject(aktiveOrdre);

            //Luk ObjectOutputStream
            Oout.close();
            Fout.close();

        } catch (IOException e) {
            System.err.println("Kunne ikke skrive aktive ordre til fil");
            e.printStackTrace();
        }


    }

    //Laeser aktive ordre array fra egen backup fil
    public ArrayList<Ordre> readAktiveOrdre() {

        //Ny ArrayList som ender med at blive returneret
        ArrayList<Ordre> aktiveOrdreList = new ArrayList<>();

        //Declare ObjectInputStream uden for loop
        ObjectInputStream Oout = null;
        try {
            //Aaben stream til fil for at modtage bytes
            FileInputStream Fin = new FileInputStream("aktiveOrdreArrayBackup");
            //Aaben ObjectInputStream til at laese hele objekter
            Oout = new ObjectInputStream(Fin);

            //looper til break
            while (true) {
                try {
                    //Laes ordre objekt ind i ArrayList

                    aktiveOrdreList = (ArrayList<Ordre>) Oout.readObject();

                } catch (EOFException e) {
                    //Exception opstaar naar sidste objektdata er laest og der forsoeges at laeses igen
                    //break ud af loop
                    break;
                }
            }

            //Close FileOutputStream
            Fin.close();

        } catch (IOException | ClassNotFoundException e) {
            //System.err.println("Laesefejl fra dagens uafsluttede ordre backup-fil. Manglende fil, eller inkompatible objekter");
            //e.printStackTrace();
        }

        try {
            //Tjek at ObjectInputStream stadig er forbundet til variablen Oout foer call close
            if (Oout != null) {
                Oout.close();
            }
        } catch (IOException e) {
            System.err.println("ObjectInputStream error efter laesning");
            e.printStackTrace();
        }



        //returner ordreliste med alle ordre fra filen
        return aktiveOrdreList;
    }

    public void deleteUafsluttedeOrdreFile() {
        File fileDeleter = new File("aktiveOrdreArrayBackup");

        if (fileDeleter.exists()) {
            if (!fileDeleter.delete()) {
                System.err.println("error deleting file uafsluttede ordre backup");
            }
        }
    }


    //Tilfoejer dagens afsluttede ordre til backup fil
    public void writeDagensAfsluttedeOrdre(ArrayList<Ordre> dagensAfsluttedeOrdre) {



        try {
            //Stream til bytes til filobjekt skabes
            //Der aabnes en fil stream til overwrite (ingen true som ekstra parameter)
            FileOutputStream Fout = new FileOutputStream("afsluttedeOrdreArrayBackup");


            //ObjectOutputStream object som fungerer med overwrite
            ObjectOutputStream Oout = new ObjectOutputStream(Fout);


            //Send hele ordre objektet som stream til filen
            Oout.writeObject(dagensAfsluttedeOrdre);

            //Luk ObjectOutputStream
            Oout.close();
            Fout.close();

        } catch (IOException e) {
            System.err.println("Kunne ikke skrive dagens afsluttede ordre til backup fil");
            e.printStackTrace();
        }


    }

    //Laeser dagens afsluttede ordre array fra egen fil
    public ArrayList<Ordre> readDagensAfsluttedeOrdre() {

        //Ny ArrayList som ender med at blive returneret
        ArrayList<Ordre> dagensAfsluttedeOrdre = new ArrayList<>();

        //Declare ObjectInputStream uden for loop
        ObjectInputStream Oout = null;
        try {
            //Aaben stream til fil for at modtage bytes
            FileInputStream Fin = new FileInputStream("afsluttedeOrdreArrayBackup");
            //Aaben ObjectInputStream til at laese hele objekter
            Oout = new ObjectInputStream(Fin);

            //looper til break
            while (true) {
                try {
                    //Laes ordre objekt ind i ArrayList

                    dagensAfsluttedeOrdre = (ArrayList<Ordre>) Oout.readObject();

                } catch (EOFException e) {
                    //Exception opstaar naar sidste objektdata er laest og der forsoeges at laeses igen
                    //break ud af loop
                    break;
                }
            }

            //Close FileOutputStream
            Fin.close();

        } catch (IOException | ClassNotFoundException e) {
            //System.err.println("Laesefejl fra dagens afsluttede ordre backup-fil. Manglende fil, eller inkompatible objekter");
            //e.printStackTrace();
        }

        try {
            //Tjek at ObjectInputStream stadig er forbundet til variablen Oout foer call close
            if (Oout != null) {
                Oout.close();
            }
        } catch (IOException e) {
            System.err.println("ObjectInputStream error efter laesning");
            e.printStackTrace();
        }



        //returner ordreliste med alle ordre fra filen
        return dagensAfsluttedeOrdre;
    }

    public void deleteafsluttedeOrdreFile() {
        File fileDeleter = new File("afsluttedeOrdreArrayBackup");
        if (fileDeleter.exists()) {
            if (!fileDeleter.delete()) {
                System.err.println("error deleting file afsluttede ordre backup");
            }
        }
    }





}
