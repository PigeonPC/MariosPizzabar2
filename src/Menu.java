import java.util.*;

public class Menu {

    Scanner scanner;
    String marginStringLevel1 = "%-35s %-35s\n";


    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void level1 (ArrayList<Pizza> menuPizzaUdvalg ) {

        boolean exitApp = false;

        while (!exitApp) {


            System.out.println("\n******************** Hovedmenu ********************");
            System.out.printf(marginStringLevel1, "1. Vis menukort", "5. Rediger menu");
            System.out.printf(marginStringLevel1, "2. Opret ordre", "6. Se statistik");
            System.out.printf(marginStringLevel1, "3. Find ordre", "7. Afslut dag");
            System.out.printf(marginStringLevel1, "4. Ændre pizza status", "0. Exit");

            switch(getValgInt(0, 8, false, scanner)) {
                case 0:
                    //0. Exit
                    exitApp = true;
                    break;

                case 1:
                    //1. Vis menukort
                    Menukort.visSorteretMenu(menuPizzaUdvalg);
                    break;

                case 2:

                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:

                    break;

                case 8:

                    break;

                default:
                    System.err.println("switch fejl");
                    break;


            }
        }

    }




    public static int getValgInt(int min, int max, boolean useEmpty, Scanner in) {

        String tempInput;
        int number;

        while (true) {
            tempInput = in.nextLine();

            if (tempInput.isEmpty()) {
                if (useEmpty) {
                    return Integer.MIN_VALUE;
                } else {
                    System.err.println("Du bliver nødt til at vaelge noget her");
                }
                //Test om String bestaar af en raekke cifre
            } else if (tempInput.matches("-?\\d+")) {
                //Omdan cifrene i String til en int
                number = Integer.parseInt(tempInput);

                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.err.printf("Tallet du har indtastet er uden for mulighederne.\nTallet skal være mellem %d og %d.\nPrøv igen\n", min, max);
                }

            } else {
                System.err.println("Det er ikke et tal du har indtastet. Prøv igen");
            }
        }
    }




}
