

import java.io.*;
import java.util.ArrayList;

public class FileIO {


    public FileIO() throws IOException {
        System.out.println("FileIO object til filhaandeting instantiated");
    }

    public void write(Ordre ordre) {

        //ObjectOutputStream Oout = null;
        try {
            FileOutputStream Fout = new FileOutputStream("testOrdreHistorik1", true);
            //ObjectOutputStream Oout;
            //Oout = new AppendObjectOutputStream(Fout);
            ObjectOutputStream Oout = Fout.getChannel().position() == 0 ?
                    new ObjectOutputStream(Fout) :
                    new AppendObjectOutputStream(Fout);
            Oout.writeObject(ordre);
            Oout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public ArrayList<Ordre> read() {

        //izza tempPizza1 = new Pizza();
        //Pizza tempPizza2 = new Pizza();
        ArrayList<Ordre> ordreList = new ArrayList<>();

        ObjectInputStream Oout = null;
        try {
            FileInputStream Fin = new FileInputStream("testOrdreHistorik1");
            Oout = new ObjectInputStream(Fin);

            while (true) {
                try {
                    ordreList.add((Ordre) Oout.readObject());
                    //System.out.println();
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            assert Oout != null;
            Oout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ordreList;
    }


}
