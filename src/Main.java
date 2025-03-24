import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;


public class Main {


    public static void main(String [] args) {

        run();

    }



    public static void run() {

        ArrayList<Ordre> aktiveOrdre;
        ArrayList<Ordre> dagensAfsluttedeOrdre;
        ArrayList<Pizza> menuPizzaUdvalg;
        menuPizzaUdvalg = new ArrayList<Pizza>();

        Scanner scanner = new Scanner(System.in);
        aktiveOrdre = new ArrayList<>();

        // Lav instance af FileIO class til filhaandtering
        FileIO myFileHandler = null;
        try {
            myFileHandler = new FileIO();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // fyldt pizzamenu op til stoerrelse 10
        while (menuPizzaUdvalg.size() < 11) {
            menuPizzaUdvalg.add(null);
        }


        // smid pizza dummies i pizzamenu
        createPizzaDummies(menuPizzaUdvalg);


        // menu dummy til test oprette og save ordrer
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("\n1. Opret ordre");
            System.out.println("2. Se ordre i liste");
            System.out.println("3. Toem liste for ordre");
            System.out.println("4. Test at aendre anden pizza i foerste ordre til klar");
            System.out.println("5. Tilfoej ordre fra liste til fil");
            System.out.println("6. Hent alle ordre fra fil til liste");
            System.out.println();
            System.out.println("0. Exit");

            switch (HelpMethods.getValgInt(0, 6, false, scanner)) {
                case 0:
                    exitApp = true;
                    break;
                case 1:
                    aktiveOrdre.add(menuOpretOrdre(menuPizzaUdvalg, scanner));
                    break;
                case 2:
                    if (!aktiveOrdre.isEmpty()) {
                        aktiveOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen ordre at printe");
                    }
                    break;
                case 3:
                    aktiveOrdre.clear();
                    break;
                case 4:
                    if (!aktiveOrdre.isEmpty() && aktiveOrdre.get(0).getOrdrePizzaListe().size() >= 2) {
                        aktiveOrdre.get(0).getOrdrePizzaListe().get(1).setPizzaFaerdig(true);
                    } else {
                        System.err.println("Den pizza og/eller ordre findes ikke");
                    }
                    break;



                case 5:
                    if (!aktiveOrdre.isEmpty()) {

                        for (Ordre ordre : aktiveOrdre) {
                            myFileHandler.write(ordre);
                        }

                        aktiveOrdre.clear();

                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }
                    break;
                case 6:
                    try {
                        aktiveOrdre = myFileHandler.read();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
                    }

                    break;

            }

        }







    }

    public static Ordre menuOpretOrdre(ArrayList<Pizza> menuPizzaUdvalg, Scanner scanner) {
        Ordre tempOrdre = new Ordre();
        boolean orderVidere = false;
        int[][] brugerValgPizza;
        int antalSammePizza;
        String kommentarPizza;
        String kommentarOrdre;
        String tlfnummer;
        Pizza tempPizza;
        Pizza valgtPizza;
        int tempValg;
        LocalDateTime afhentningsTidspunktValidated;
        DateTimeFormatter formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
        DateTimeFormatter formatDKKlokken = DateTimeFormatter.ofPattern("HH:mm");


        while (!orderVidere) {

            System.out.println("\n1. Tilføj pizza til ordre");
            System.out.println("2. Fjern en pizza fra ordre");
            System.out.println("3. Fjern samtlige pizza fra ordre");
            System.out.println();
            System.out.println("4. Tilføj/ændre kommentar til enkelte pizzaer");

            System.out.println("5. Tilføj tlf nummer til afhentning");
            System.out.println("6. Tilføj/ret afhentningstidspunkt");
            System.out.println("7. Tilføj kommentar til samlet ordre");
            System.out.println();
            System.out.println("8. Sæt ordren i gang");
            System.out.println();
            System.out.println();
            System.out.println("0. Exit uden at gennemføre ordre");


            switch (HelpMethods.getValgInt(0, 8, false, scanner)) {
                case 0:
                    orderVidere = true;
                    break;

                case 1:
                    udskrivPizzaUdvalg(menuPizzaUdvalg);

                    brugerValgPizza = HelpMethods.getArrayValgPizzaSomFindes(menuPizzaUdvalg, scanner);

                    if (brugerValgPizza.length == 1 && brugerValgPizza[0][1] == 1) {
                        valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[0][0]).copyOf();

                        System.out.println("Tilføj kommentar til pizza. Max 40 karakterer (Enter, hvis ingen kommentarer:");

                        kommentarPizza = HelpMethods.getValidatedTekstLaengde(40, true, scanner);

                        if (!kommentarPizza.isEmpty()) {
                            valgtPizza.setComment(kommentarPizza);
                        }

                        tempOrdre.tilfoejPizzaTilOrdre(valgtPizza);
                    } else {

                        for (int y = 0; y < brugerValgPizza.length; y++) {
                            valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[y][0]).copyOf();

                            for (int i = 0; i < brugerValgPizza[y][1]; i++) {
                                tempOrdre.tilfoejPizzaTilOrdre(new Pizza(valgtPizza));
                            }
                        }

                    }

                    tempOrdre.sorterOrdrePizzaListe();

                    tempOrdre.opdaterTotalPrisOrdre();

                    break;



                case 2:
                    if (tempOrdre.ordrePizzaListe.isEmpty()) {
                        System.out.println("Du har ikke tilføjet pizza til ordren");
                    } else {
                        tempOrdre.udskrivOrdrePizzaListeMedId();
                        System.out.println("Enter [numnmer] på pizza som du vil slette fra ordre (0 for exit)");
                        tempValg = HelpMethods.getValgInt(0, tempOrdre.ordrePizzaListe.size(), false, scanner);
                        if (tempValg != 0) {
                            tempOrdre.ordrePizzaListe.remove(tempValg - 1);


                        }



                    }

                    break;

                case 3:
                    System.out.println("Er du sikker på at du vil fjerne alle pizza fra denne ordre?  (1)NEJ (2)JA");

                    if (HelpMethods.getValgInt(1, 2, false, scanner) == 2) {
                        tempOrdre.toemOrdrePizzaListe();
                    }

                    tempOrdre.opdaterTotalPrisOrdre();
                    break;


                case 4:
                    if (tempOrdre.ordrePizzaListe.isEmpty()) {
                        System.out.println("Du har ikke tilføjet pizza til ordren");
                    } else {
                        tempOrdre.udskrivOrdrePizzaListeMedId();
                        System.out.println("Enter [numnmer] på pizza som du vil ændre kommentar på (0 for exit)");
                        tempValg = HelpMethods.getValgInt(0, tempOrdre.ordrePizzaListe.size(), false, scanner);
                        if (tempValg != 0) {
                            System.out.println("Tilføj kommentar til pizza. Max 40 karakterer (Enter, hvis ingen kommentarer:");

                            kommentarPizza = HelpMethods.getValidatedTekstLaengde(40, true, scanner);

                            tempOrdre.ordrePizzaListe.get(tempValg - 1).setKommentar(kommentarPizza);


                        }


                    }


                    break;

                case 5:
                    System.out.printf("Dette er det nuværende registrerede tlf nummer:\n%s\n", tempOrdre.getTlfNummerKunde());

                    System.out.println("Indtast et dansk telefonnummer (Enter for exit):");
                    tlfnummer = HelpMethods.getValidatedTlfDK(true, scanner);

                    if (!tlfnummer.isEmpty()) {
                        tempOrdre.setTlfNummerKunde(tlfnummer);
                    }

                    break;


                case 6:
                    if (tempOrdre.getTidspunktForAfhentning() != null) {
                        System.out.printf("Nuværende afhentning er angivet til: %s \n", tempOrdre.getTidspunktForAfhentning().format(formatDKKlokken));
                    }
                    afhentningsTidspunktValidated = HelpMethods.getValidatedTidspunktSenere(true, scanner);
                    tempOrdre.setTidspunktForAfhentning(afhentningsTidspunktValidated);
                    break;

                case 7:
                    if (!tempOrdre.getKommentar().isEmpty()) {
                        System.out.printf("Nuværende kommentar til ordren er:\n%s\n\n", tempOrdre.getKommentar());
                    }

                    System.out.println("Indtast en kommentar til den samlede ordre (max 80 karakterer. Enter for exit ændringer): ");
                    kommentarOrdre = HelpMethods.getValidatedTekstLaengde(80, true, scanner);
                    if (!kommentarOrdre.isEmpty()) {
                        tempOrdre.setKommentar(kommentarOrdre);
                    }

                    break;

                case 8:
                    if (tempOrdre.getOrdrePizzaListe().isEmpty()) {
                        System.err.println("Du mangler at tilføje pizza. Enter for at komme tilbage");
                        scanner.nextLine();
                        continue;
                    } else if (tempOrdre.getTlfNummerKunde().equals("Endnu ikke opgivet")) {
                        System.err.println("Du har ikke registreret tlf nummer. Enter for at komme tilbage");
                        scanner.nextLine();
                        continue;
                    } else if (tempOrdre.getTidspunktForAfhentning() == null) {
                        System.out.println("Du har ikke angivet afhentningstidspunkt");
                        System.out.println("(1) for hurtigst muligt (2) hvis det er en fejl");

                        if (HelpMethods.getValgInt(1, 2, false, scanner) == 1) {
                            tempOrdre.setTidspunktForAfhentning(LocalDateTime.now());
                            orderVidere = true;
                        } else {
                            continue;
                        }
                    }

                    orderVidere = true;

                    break;


                default:
                    System.out.println("not done or out of range");
                    break;
            }

            if (!orderVidere) {
                System.out.println(tempOrdre);
            }
        }




        return tempOrdre;

    }

    public static void udskrivPizzaUdvalg(ArrayList<Pizza> menuPizzaUdvalg) {
        for (int i = 0; i < menuPizzaUdvalg.size(); i++) {
            if (menuPizzaUdvalg.get(i) != null) {
                System.out.println(menuPizzaUdvalg.get(i));
            }
        }




    }

    public static void timeTest() {

        Scanner scanner = new Scanner(System.in);

        ArrayList<LocalDateTime> tiderTilComp = new ArrayList<LocalDateTime>();

        LocalDateTime nowTimeNoFormat = LocalDateTime.now();
        LocalDateTime nowTimeNoFormat30 = nowTimeNoFormat.plusMinutes(30);
        LocalDateTime nowTimeNoFormat45 = nowTimeNoFormat.plusMinutes(45);
        LocalDateTime nowTimeNoFormat55 = nowTimeNoFormat.plusMinutes(55);

        tiderTilComp.add(nowTimeNoFormat45);
        tiderTilComp.add(nowTimeNoFormat);
        tiderTilComp.add(nowTimeNoFormat55);
        tiderTilComp.add(nowTimeNoFormat30);

        System.out.println(tiderTilComp);


        LocalDateTime basis = LocalDateTime.now();
        System.out.println(basis);

        int inputHour = scanner.nextInt();

        LocalDateTime basisNyHour = basis.withHour(inputHour);
        int inputMinute = scanner.nextInt();

        LocalDateTime basisNyMinute = basisNyHour.withMinute(inputMinute);

        System.out.println(basisNyHour);

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HH:mm");


//        LocalDateTime inputConv = LocalDateTime.parse(inputTime, inputFormat);
//        System.out.println(inputConv);


        System.out.println(nowTimeNoFormat);
        System.out.println(nowTimeNoFormat30);



        DateTimeFormatter formatDKTimeDate = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
        DateTimeFormatter formatDKTime = DateTimeFormatter.ofPattern("HH:mm");

        String nowTimeFormatted = nowTimeNoFormat.format(formatDKTime);
        String nowTimeFormatted30 = nowTimeNoFormat30.format(formatDKTimeDate);
        String nowTimeFormatted45 = nowTimeNoFormat45.format(formatDKTime);
        String nowTimeFormatted55 = nowTimeNoFormat55.format(formatDKTimeDate);
        String nowTimeFormattedNy = basisNyMinute.format(formatDKTimeDate);




        System.out.println(nowTimeFormatted);
        System.out.println(nowTimeFormatted30);
        System.out.println(nowTimeFormatted45);
        System.out.println(nowTimeFormatted55);
        System.out.println(nowTimeFormattedNy);


        System.out.println(nowTimeFormatted.compareTo(nowTimeFormatted30));


//        DateTimeFormatter nowTimeFromString =



//        System.out.println(nowTimeNoFormat);


    }

    public static void createPizzaDummies(ArrayList<Pizza> menuPizzaUdvalg) {
//        Pizza pizza3 = new Pizza("Margerita", 3, 25,
//                new ArrayList<String>(Arrays.asList("Champignon",
//                        "Tomat",
//                        "Ost")),
//                "");
//        Pizza pizza6 = new Pizza("Vesuvio", 6, 35,
//                new ArrayList<String>(Arrays.asList("Champignon",
//                        "Tomat")),
//                "");
//
//        Pizza pizza8 = new Pizza("Matador", 8, 39,
//                new ArrayList<String>(Arrays.asList("Oksefilet",
//                        "Bearnaise",
//                        "Jaka bov",
//                        "salami")),
//                "");
//        menuPizzaUdvalg.set(3, pizza3);
//        menuPizzaUdvalg.set(6, pizza6);
//        menuPizzaUdvalg.set(8, pizza8);
    }



}


