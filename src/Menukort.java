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

    public static void opdaterMenu(ArrayList<Pizza> menu, String[] ingredienser) {

        Pizza sofus = new Pizza(100, "Sofus", 100, ingredienser, "null");

        menu.add(sofus);

        try {

            FileWriter myWriter = new FileWriter("Menu.txt");
            for (int i = 0; i < menu.size(); i++) {
                myWriter.write(menu.get(i).getNummer() + ", " + menu.get(i).getNavn() + ", " + menu.get(i).getPris() + ", ");
                for (int j = 0; j < ingredienser.length-1; j++) {
                    myWriter.write(menu.get(i).getIngredient()[j] + ", ");
                }
                myWriter.write(menu.get(i).getKommentar() + "\n");

            }
            myWriter.close();
            System.out.println("Menuen er opdateret");
        } catch (IOException e) {
            System.out.println("En fejl er opstået.");
            e.printStackTrace();
            //23 min.
        }

    }




    //Method to add the ingredients to the ingredients ArrayList from the Ingredienser.txt file.
    public static ArrayList<String> getIngredienser() {

        //The ingredient ArrayList is declared
        ArrayList<String> ingredienser = new ArrayList<>();

        //The content of the Ingredienser.txt is read and added to the menu ArrayList
        try {
            //To make sure it reads from the Menu.txt file
            File myObj = new File("Ingredienser.txt");
            Scanner myReader = new Scanner(myObj);

            //The loop keeps running until there are no more lines
            while (myReader.hasNextLine()) {
                String ingrediens = myReader.nextLine();
                ingredienser.add(ingrediens);
            }
            myReader.close();
        }

        //If an error occurs the following code is run.
        catch (FileNotFoundException e) {
            System.out.println("Der er sket en fejl under læsning af ingredienser.");
            e.printStackTrace();
        }

        return ingredienser;
    }

    public static void visIngredienser(ArrayList<String> ingredienser) {

        System.out.println("Ingrediensliste:");
        for (String ingrediens : ingredienser) {
            // Udskriver ingredienser
            System.out.println(ingrediens);
        }
    }

    public static void opdaterIngredienser(ArrayList<String> ingredienser) {

        try {
            FileWriter myWriter = new FileWriter("Ingredienser.txt");

            for (int i = 0; i < ingredienser.size(); i++) {
                myWriter.write(ingredienser.get(i) + "\n");
            }

            myWriter.close();
            //System.out.println("\nIngredienslisten er opdateret");
        }
        catch (IOException e) {
            System.out.println("En fejl er opstået.");
            e.printStackTrace();
        }
    }

    public static void ingrediensBrugerMenu(Scanner scanner){
        ArrayList<String> ingredienser = getIngredienser();
        visIngredienser(ingredienser);

        boolean redigerVidere = false;

        while (!redigerVidere) {
            System.out.println("\n1. Tilføj ny ingrediens");
            System.out.println("2. Slet ingrediens fra listen");
            System.out.println("3. Vis ingrediensliste");
            System.out.println("4. Tilbage til hovedmenu");

            switch (HelpMethods.getValgInt(0, 8, false, scanner)) {
                case 0:
                    //0. Exit uden at gennemføre ændring
                    //juster flag til afslutte og returnere ordre
                    redigerVidere = true;
                    break;

                case 1:
                    //1. tilføj ny ingrediens til arrayList
                    tilfoejIngrediens(ingredienser, scanner);
                    break;

                case 2:
                    //2. slet ingrediens fra listen
                    sletIngrediens(ingredienser, scanner);
                    break;


                case 3:
                    //3. vis ingredienslisten
                    visIngredienser(ingredienser);
                    break;

                case 4:
                    //4. naviger til hovedmenu
                    Main.run();

            }
        }
    }


    //Tilføj ingrediens til ingrediens ArrayList
    public static void tilfoejIngrediens(ArrayList<String> ingredienser, Scanner scanner) {
        System.out.println("\nIndtast ny ingrediens: ");
        String ingrediens = scanner.nextLine();

        if (ingrediens.isEmpty()){
            System.out.println("Du indtastede ikke noget, ingrediens ikke tilføjet");
            return;
        }

        for (String i : ingredienser) {
            if (i.equals(ingrediens)) {
                System.out.println("\nIngrediens findes allerede på listen");
                return;
            }
        }

        ingredienser.add(ingrediens);

        opdaterIngredienser(ingredienser);
        System.out.println("\nIngrediensen '" + ingrediens + "' er blevet tilføjet til listen.");

    }

    public static void sletIngrediens(ArrayList<String> ingredienser, Scanner scanner){
        System.out.println("\nHvilken ingrediens vil du slette?");
        for (int i = 0; i < ingredienser.size(); i++){
            System.out.println((i + 1) + ". " + ingredienser.get(i));
        }

        System.out.println("\nIndtast nummeret på den ingrediens, som du ønsker at slette (0 for at fortryde): ");
        int valg;

        try {
            valg = Integer.parseInt(scanner.nextLine());

            if (valg == 0) {
                System.out.println("\nAnnulleret");
                return;
            }
            if (valg < 1 || valg > ingredienser.size()) {
                System.out.println("\nUgyldigt valg. Prøv igen");
                return;
            }

            String fjernet = ingredienser.remove(valg - 1);
            opdaterIngredienser(ingredienser); //Gem den nye liste i filen
            System.out.println("\nIngrediensen '" + fjernet + "' er blevet fjernet");
        }
        catch (NumberFormatException e) {
            System.out.println("\nDu skal skrive et tal");
        }
    }

    //4 timer, i hvert fald...
}