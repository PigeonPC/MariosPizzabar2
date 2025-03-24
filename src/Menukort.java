import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class Menukort {

    //Method to add the pizzas to the menu ArrayList from the Menu.txt file.
    public static ArrayList getMenu() {

        //The Menu ArrayList is declared
        ArrayList<Pizza> menu = new ArrayList<>();

        //The content of the Menu.txt is read and added to the menu ArrrayList
        try {
            //To make sure it reads from the Menu.txt file
            File myObj = new File("Menu.txt");
            Scanner myReader = new Scanner(myObj);

            //The loop keeps running until there are no more lines
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                //Adding a data.split() to split up the content of Menu.txt and assign them to the values of pizza
                String[] split = data.split(", ");

                String nummer = split[0].trim();
                String navn = split[1].trim();
                String pris = split[2].trim();

                //The array loops through the rest of the data.split and adds everything to the array apart from the last split as this is the comment.
                String[] ingredienser = new String[split.length - 4];
                for (int i = 0; i < split.length - 4; i++) {
                    ingredienser[i] = split[3 + i];
                }

                String kommentar = split[split.length - 1].trim();

                //All the variables are used in creating a new pizza object.
                menu.add(new Pizza(parseInt(nummer), navn, parseInt(pris), ingredienser, kommentar));


            }

            //If an error occurs the following code is run.
        } catch (FileNotFoundException e) {
            System.out.println("Der er sket en fejl.");
            e.printStackTrace();
        }
        return menu;
    }

    //All the pizzas are now printed to the console, so Alfonso can view them while creating a new order.

    public static void visMenu(ArrayList<Pizza> menu) {

        for (Pizza s : menu) {
            System.out.println(s);
        }

    }

}