import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Menukort {

    public static void main(String[] args) {

        redigerMenu();

    }

    public static void redigerMenu() {

        Scanner scanner = new Scanner(System.in);
        boolean videre = false;
        boolean hovedmenu = false;

        while (true) {

            System.out.println("1: Rediger menukort");
            System.out.println("2: Rediger ingredienser");
            System.out.println("3: Vend tilbage til hovedmenu");
            System.out.println("Vælg en mulighed");

            try {

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:

                        while (true) {

                            System.out.println("1: Tilføj pizza");
                            System.out.println("2: Slet pizza");
                            System.out.println("3: Vis menukort");
                            System.out.println("4: Tilbage til rediger menu");
                            System.out.println("Vælg en mulighed");

                            try {

                                choice = scanner.nextInt();

                                switch (choice) {
                                    case 1:
                                        opretPizza(getIngredienser(), getMenu());
                                        videre = true;
                                        break;
                                    case 2:
                                        sletPizza(getMenu(), getIngredienser());
                                        videre = true;
                                        break;
                                    case 3:
                                        visSorteretMenu(getMenu());
                                        break;
                                    case 4:
                                        videre = true;
                                        break;
                                    default:
                                        System.err.println("Indtast venligst en gyldig mulighed");
                                        break;

                                }

                            } catch (Exception e) {
                                System.err.println("Indtast venligst en gyldig mulighed");
                                scanner.nextLine();
                            }

                            if (videre) {
                                break;
                            }

                        }
                        break;
                    case 2:
                        scanner.nextLine();
                        ingrediensBrugerMenu(scanner);
                        break;

                    case 3:
                        hovedmenu = true;
                        break;

                    default:
                        System.out.println("Indtast venligst en gyldig mulighed");
                        break;
                }



            } catch (Exception e) {
                System.err.println("Indtast venligst et tal");
                scanner.nextLine();
            }

            if (hovedmenu) {
                break;
            }

        }

    }

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

    public static void visSorteretMenu(ArrayList<Pizza> menu) {

        sorterMenu("nummer", getMenu());

    }

    public static void sorterMenu(String sortBy, ArrayList<Pizza> menu) {
        if (sortBy.equals("nummer")) {
            menu.sort(Comparator.comparingInt(Pizza::getNummer));

            for (Pizza s : menu) {
                System.out.println(s);
            }

        }
    }

    public static void opretPizza(ArrayList<String> ingredienser, ArrayList<Pizza> menu) {

        Scanner scanner = new Scanner(System.in);

        int nummer = 0;
        int pris = 0;
        int answer = 0;
        int input = 0;
        String navn = "";
        boolean ifNavn = true;
        boolean ifExists = true;
        boolean ifNegative = true;
        boolean ifNextLine = true;
        boolean ifBreak = true;
        ArrayList<String> ingredients = new ArrayList<>();

        System.out.println("Hvad er nummeret på den pizza du ønsker at tilføje?");

        while (true) {

            if (scanner.hasNextInt()) {

                nummer = scanner.nextInt();

                for (int i = 0; i < menu.size(); i++) {

                    if (nummer == (menu.get(i).getNummer())) {
                        System.out.println("Nummeret er allerede i brug. Vælg venligst et andet");
                        ifExists = false;
                        ifNextLine = false;

                        break;

                    } else if (nummer < 1) {
                        System.out.println("Tallet er negativt. Vælg venligst et andet");
                        ifNegative = false;
                        ifNextLine = false;

                        break;

                    } else {
                        ifExists = true;
                        ifNegative = true;

                    }

                }

                if (ifExists && ifNegative) {
                    break;
                }

            } else {

                if (!ifNextLine) {
                    scanner.nextLine();

                }

                String error = scanner.nextLine();
                System.out.println("\"" + error + "\"" + " er ikke et tal");
                ifNextLine = true;

            }
        }

        scanner.nextLine();

        System.out.println("Hvad er navnet på den pizza du ønsker at tilføje?");

        while (true) {

            navn = scanner.nextLine().toUpperCase();

            for (int i = 0; i < menu.size(); i++) {

                if (menu.get(i).getNavn().equals(navn)) {
                    System.out.println("Navnet er allerede i brug. Vælg venligst et andet.");
                    ifNavn = false;

                    break;
                } else {
                    ifNavn = true;
                }

            }

            if (ifNavn) {
                break;
            }

        }


        System.out.println("Hvad er prisen på den pizza du ønsker at tilføje?");

        while (true) {

            if (scanner.hasNextInt()) {

                pris = scanner.nextInt();

                for (int i = 0; i < menu.size(); i++) {

                    if (pris < 1) {
                        System.out.println("Tallet er negativt. Vælg venligst et andet");
                        ifNegative = false;
                        ifNextLine = false;
                        break;

                    } else {
                        ifNegative = true;

                    }

                }

                if (ifNegative) {
                    break;
                }

            } else {

                if (!ifNextLine) {
                    scanner.nextLine();

                }

                String error = scanner.nextLine();
                System.out.println("\"" + error + "\"" + " er ikke et tal");
                ifNextLine = true;

            }
        }

        while (true) {

            while (true) {

                try {

                    for (int i = 0; i < ingredienser.size(); i++) {

                        if (!ingredients.contains(ingredienser.get(i))) {
                            System.out.println((i + 1) + ": " + ingredienser.get(i));
                        }
                        if (ingredients.contains(ingredienser.get(i))) {
                            System.out.println((i + 1) + ": " + ingredienser.get(i) + " (Allerede tilføjet. Vælg igen for at fjerne)");
                        }

                    }

                    System.out.println("Hvilken ingrediens ønsker du at tilføje til pizzaen?");

                    input = scanner.nextInt();

                    if (input <= ingredienser.size() && input > 0) {
                        break;
                    } else {
                        System.out.println("Fejl. Det indtastede er ikke en mulighed");
                    }

                } catch (Exception e) {
                    System.out.println("Fejl. Det indtastede er ikke en mulighed");
                    scanner.nextLine();

                }

            }

            if (ingredients.contains(ingredienser.get(input - 1))) {
                ingredients.remove(ingredienser.get(input - 1));
                System.out.println(ingredienser.get(input - 1) + " fjernet fra pizza");
            } else if (!ingredients.contains(ingredienser.get(input - 1))) {
                ingredients.add(ingredienser.get(input - 1));
                System.out.println(ingredienser.get(input - 1) + " tilføjet til pizza");
            }

            while (true) {

                try {

                    System.out.println("Vil du tilføje flere ingredienser?");
                    System.out.println("1: Ja");
                    System.out.println("2: Nej");

                    answer = scanner.nextInt();

                    if (answer == 1) {

                        break;

                    } else if (answer == 2) {

                        ifBreak = false;
                        break;

                    } else {
                        System.out.println("Fejl. Det indtastede er ikke en mulighed");
                    }

                } catch (Exception e) {
                    System.out.println("Fejl. Det indtastede er ikke en mulighed");
                    scanner.nextLine();
                }

            }
            if (!ifBreak) {

                break;

            }

        }

        String[] array = new String[ingredients.size()];

        for (int i = 0; i < ingredients.size(); i++) {
            array[i] = ingredients.get(i);
        }

        Pizza newPizza = new Pizza(nummer, navn, pris, array, "null");
        menu.add(newPizza);

        try {

            FileWriter myWriter = new FileWriter("Menu.txt", true);

            myWriter.write("\n" + newPizza.getNummer() + ", " + newPizza.getNavn() + ", " + newPizza.getPris() + ", ");
            for (int j = 0; j < array.length; j++) {
                myWriter.write(array[j] + ", ");
            }
            myWriter.write(newPizza.getKommentar());

            myWriter.close();
            System.out.println("Menuen er opdateret");
        } catch (IOException e) {
            System.out.println("En fejl er opstået.");
            e.printStackTrace();

        }
    }

    public static void sletPizza(ArrayList<Pizza> menu, ArrayList<String> ingredienser) {

        Scanner scanner = new Scanner(System.in);
        boolean ifPizza = true;
        int answer = 0;

        for (Pizza s : menu) {
            System.out.println(s);
        }

        System.out.println("Vælg den pizza du ønsker at slette");

        while (true) {

            try {

                answer = scanner.nextInt();

                for (int i = 0; i < menu.size(); i++) {

                    if (answer == menu.get(i).getNummer()) {
                        System.out.println(menu.get(i).getNavn() + " er blevet slettet fra menukortet");
                        menu.remove(i);
                        ifPizza = false;
                    }

                }

                if (!ifPizza) {
                    break;
                }

                System.out.println("Der findes ikke en pizza med det indtastede nummer. Prøv igen.");

            } catch (Exception e) {
                System.out.println("Fejl. Det indtastede er ikke en mulighed");
                scanner.nextLine();
            }

        }

        try {

            FileWriter myWriter = new FileWriter("Menu.txt");
            for (int i = 0; i < menu.size(); i++) {

                myWriter.write(menu.get(i).getNummer() + ", " + menu.get(i).getNavn() + ", " + menu.get(i).getPris() + ", ");

                for (int j = 0; j < menu.get(i).getIngredient().length; j++) {

                    myWriter.write(menu.get(i).getIngredient()[j] + ", ");

                }
                myWriter.write(menu.get(i).getKommentar() + "\n");

            }
            myWriter.close();
            System.out.println("Menuen er opdateret");
        } catch (IOException e) {
            System.out.println("En fejl er opstået.");
            e.printStackTrace();

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
        } catch (IOException e) {
            System.out.println("En fejl er opstået.");
            e.printStackTrace();
        }
    }

    public static void ingrediensBrugerMenu(Scanner scanner) {
        ArrayList<String> ingredienser = getIngredienser();
        visIngredienser(ingredienser);

        boolean redigerVidere = false;

        while (!redigerVidere) {
            System.out.println("\n1. Tilføj ny ingrediens");
            System.out.println("2. Slet ingrediens fra listen");
            System.out.println("3. Vis ingrediensliste");
            System.out.println("4. Tilbage til Rediger menu");
            System.out.println("5. Tilbage til Hovedmenu");

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
                    //4. naviger til rediger menu


                case 5:
                    //5. naviger til hovedmenu
                    Main.run();

            }
        }
    }


    //Tilføj ingrediens til ingrediens ArrayList
    public static void tilfoejIngrediens(ArrayList<String> ingredienser, Scanner scanner) {
        System.out.println("\nIndtast ny ingrediens: ");
        String ingrediens = scanner.nextLine();

        if (ingrediens.isEmpty()) {
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

    public static void sletIngrediens(ArrayList<String> ingredienser, Scanner scanner) {
        System.out.println("\nHvilken ingrediens vil du slette?");
        for (int i = 0; i < ingredienser.size(); i++) {
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
        } catch (NumberFormatException e) {
            System.out.println("\nDu skal skrive et tal");
        }
    }

    //4 timer, i hvert fald...
}