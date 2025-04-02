import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Menu {

    Scanner scanner;
    String marginStringLevel1 = "%-35s %-35s\n";


    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void level1 (ArrayList<Pizza> menuPizzaUdvalg, ArrayList<Pizza> marioPizzaListe, ArrayList<Ordre> aktiveOrdre, ArrayList<Ordre> dagensAfsluttedeOrdre, ArrayList<Ordre> ordreHistorikListe, FileIO myFileHandler) {

        boolean exitApp = false;

        while (!exitApp) {

            Main.opdaterMarioListe(aktiveOrdre, marioPizzaListe, true, true);

            System.out.println("\n******************** Hovedmenu ********************");
            System.out.printf(marginStringLevel1, "1. Vis menukort", "5. Rediger menu");
            System.out.printf(marginStringLevel1, "2. Opret ordre", "6. Se statistik");
            System.out.printf(marginStringLevel1, "3. Find/vis ordre", "7. Afslut dagen");
            System.out.printf(marginStringLevel1, "4. Ændre pizza status", "8. Udviklermenu");
            System.out.println("0. Exit");

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
                //2. Opret ordre
                    aktiveOrdre.add(menuOpretOrdre(menuPizzaUdvalg, scanner));
                    try {
                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }
                    } catch (NullPointerException e) {
                        //System.err.println("error save file uafsluttede ordre");
                    }
                    break;

                case 3:
                //3. Find ordre
                    menuFindOrdre(aktiveOrdre, dagensAfsluttedeOrdre, ordreHistorikListe, marioPizzaListe, myFileHandler);

                    break;

                case 4:
                    //4. Ændre pizza status
                    Main.markerPizzaSomFaerdig(aktiveOrdre, marioPizzaListe, scanner);
                    try {
                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }
                    } catch (NullPointerException e) {
                        System.err.println("error write uafsluttede");
                    }
                    break;

                case 5:
                    //5. Rediger menu
                    menuRedigerMenukort(scanner);
                    break;

                case 6:
                    //6. Se statistik
                    menuStatistik(aktiveOrdre, dagensAfsluttedeOrdre, ordreHistorikListe, scanner, myFileHandler);

                    break;

                case 7:
                    //7. Afslut dagen
                    if (!aktiveOrdre.isEmpty()) {
                        System.out.println("Du har stadig uafsluttede ordrer som først skal behandles");
                        break;
                    } else if (!dagensAfsluttedeOrdre.isEmpty()) {

                        for (Ordre ordre : dagensAfsluttedeOrdre) {
                            myFileHandler.write(ordre);
                        }

                        dagensAfsluttedeOrdre.clear();
                        try {
                            myFileHandler.deleteafsluttedeOrdreFile();
                        } catch (NullPointerException e) {
                            System.err.println("error delete file afsluttede ordre backup");
                        }

                        System.out.println("Dagens afsluttede ordrer blev overført til historik\nAlt er klart til at starte forfra i morgen");
                        exitApp = true;

                    } else {
                        exitApp = true;
                    }

                    break;

                case 8:
                    //8. Udviklermenu
                    menuUdvikler(aktiveOrdre, dagensAfsluttedeOrdre, ordreHistorikListe, scanner, myFileHandler);
                    break;

                default:
                    System.err.println("switch fejl");
                    break;


            }
        }

    }

    public void menuFindOrdre(ArrayList<Ordre> aktiveOrdre, ArrayList<Ordre> dagensAfsluttedeOrdre, ArrayList<Ordre> ordreHistorikListe, ArrayList<Pizza> marioPizzaListe, FileIO myFileHandler) {
        boolean goBack = false;

        while (!goBack) {

            System.out.println("\n1. Udlever ordre");
            System.out.println("2. Vis telefonnumre på ordre der er klar til afhentning");
            System.out.println("3. Søg uafsluttet ordre frem ud fra telefonnummer");
            System.out.println("4. Se alle uafsluttede ordre for dagen");
            System.out.println("5. Se alle afsluttede ordre for dagen som endnu ikke er foert til historik");
            System.out.println("6. Se alle ordre i historik");
            System.out.println("0. Exit");


            switch (getValgInt(0, 6, false, scanner)) {
                case 0:
                    //0. Exit
                    goBack = true;
                    break;


                case 1:
                    //1. Udlever ordre
                    Ordre.udleverOrdre(aktiveOrdre, dagensAfsluttedeOrdre, myFileHandler);



                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }

                    } else {
                        myFileHandler.deleteUafsluttedeOrdreFile();
                        System.err.println("Ingen ordre at gemme til fil for dagens uafsluttede ordre   ");
                    }
                    if (!dagensAfsluttedeOrdre.isEmpty()) {

                        for (Ordre ordre : dagensAfsluttedeOrdre) {

                            if (myFileHandler != null) {
                                myFileHandler.writeDagensAfsluttedeOrdre(dagensAfsluttedeOrdre);
                            } else {
                                System.err.println("Filehandler error");
                            }
                        }
                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }



                    break;
                case 2:
                    //2. Vis telefonnumre på ordre der er klar til afhentning
                    Main.opdaterMarioListe(aktiveOrdre, marioPizzaListe, false, true);

                    break;
                case 3:
                    //3. Søg uafsluttet ordre frem ud fra telefonnummer
                    if (!aktiveOrdre.isEmpty()) {
                        Ordre.visAktiveOrdre(aktiveOrdre);

                    } else {
                        System.err.println("Der er ingen uafsluttede ordre");
                    }

                    break;


                case 4:
                    //4. Se alle uafsluttede ordre for dagen
                    if (!aktiveOrdre.isEmpty()) {
                        //svarer til System.out.println paa samtlige ordre i aktive ordre liste
                        Collections.sort(aktiveOrdre, new ComparatorOrdreDato());
                        aktiveOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen uafsluttede ordre for dagen at printe");
                    }

                    break;

                case 5:
                    //5. Se alle afsluttede ordre for dagen som endnu ikke er foert til historik
                    if (!dagensAfsluttedeOrdre.isEmpty()) {
                        //svarer til System.out.println paa samtlige ordre i dagens afsluttede ordre liste
                        Collections.sort(dagensAfsluttedeOrdre, new ComparatorOrdreDato());
                        dagensAfsluttedeOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen afsluttede ordre for dagen at printe");
                    }

                    break;

                case 6:
                    //6. Se alle ordre i historik


                    try {
                        //Hent alle gamle ordre objekter fra fil til ordreHistorik liste ved hjaelp af method i instance af FileIO class
                        ordreHistorikListe = myFileHandler.read();


                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen historik fil at læse");
                    }

                    if (!ordreHistorikListe.isEmpty()) {

                        Collections.sort(ordreHistorikListe, new ComparatorOrdreDato());
                        System.out.println(ordreHistorikListe);

                    } else {
                        System.err.println("Der er ingen ordre i historikken at printe");
                    }

                    break;


                default:
                    System.err.println("switch fejl");
                    break;

            }
        }

    }


    public Ordre menuOpretOrdre(ArrayList<Pizza> menuPizzaUdvalg, Scanner scanner) {

        //Create nyt ordre objekt som ender med at blive returneret
        Ordre tempOrdre = new Ordre();

        boolean orderVidere = false;

        //two dimensional array til at modtage valgte pizzaer og deres antal fra helper method
        int[][] brugerValgPizza;

        int antalSammePizza;

        String kommentarPizza;
        String kommentarOrdre;
        String tlfnummer;

        Pizza tempPizza;
        Pizza valgtPizza;

        int tempValg;

        LocalDateTime afhentningsTidspunktValidated;

        //Objekter til at bruge som parameter i forbindelse med formatering af print af LocalDateTime
        //fx 230425 15:17
        //fx 15:17
        DateTimeFormatter formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
        DateTimeFormatter formatDKKlokken = DateTimeFormatter.ofPattern("HH:mm");

        //Menu som nok boer flyttes til fx Menu objekt og kaldes til print herfra
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

            //Brug helper method til at faa brugervalg mellem 0 og 8 hvor Enter ikke er tilladt
            switch (getValgInt(0, 8, false, scanner)) {
                case 0:
                    //0. Exit uden at gennemføre ordre
                    //juster flag til afslutte og returnere ordre
                    orderVidere = true;
                    break;

                case 1:
                    //1. Tilføj pizza til ordre

                    //Denne method til at vise pizza menu skal muligvis udskiftes med den fra Menukort class efter merge
                    Main.udskrivPizzaUdvalg(menuPizzaUdvalg);

                    //Brug helper method til at modtage brugers multivalg af pizzaer. Modtages i two dimensional array med pizza nr og antal paa hvert index
                    brugerValgPizza = HelpMethods.getArrayValgPizzaSomFindes(menuPizzaUdvalg, scanner);

                    //Tjek om der kun er valgt en type pizza og kun 1 stk af denne
                    if (brugerValgPizza.length == 1 && brugerValgPizza[0][1] == 1) {
                        //Create nyt selvstaendigt pizza objekt som har samme values som den valgte pizza fra menuen
                        //Basically kopier en pizza
                        //Hvis blot bruge reference/alias saa bliver pizza i ordre ikke selvstaendige
                        //det ville medfoere at en kommentar til en Margarita i en ordre ville blive en kommentar til alle Margarita i alle ordre og i menu
//                        valgtPizza = HelpMethods.getMenuIndexUdFraPizzaNummer(menuPizzaUdvalg, brugerValgPizza[0][0] - 1).copyOf();
                        valgtPizza = menuPizzaUdvalg.get(HelpMethods.getMenuIndexUdFraPizzaNummer(menuPizzaUdvalg, brugerValgPizza[0][0])).copyOf();

                        //Fordi der kun er tale om en enkelt pizza saa tilbydes mulighed for kommentar med det samme (fx uden loeg)
                        System.out.println("Tilføj kommentar til pizza. Max 40 karakterer (Enter, hvis ingen kommentarer:");

                        //Brug helper method til at modtage String paa max 40 characters hvor tom String (Enter) ogsaa er tilladt
                        kommentarPizza = HelpMethods.getValidatedTekstLaengde(40, true, scanner);

                        //Hvis bruger ikke blot tastede Enter saa gem kommentar til pizzaen
                        if (!kommentarPizza.isEmpty()) {
                            valgtPizza.setKommentar(kommentarPizza);
                        }

                        //Tilfoej denne nye pizza til ordrens liste af pizzaer
                        tempOrdre.tilfoejPizzaTilOrdre(valgtPizza);

                        //Hvis brugere indtastede flere pizzavalg paa en gang
                    } else {
                        //Loop til at komme hele two dimensional array af pizzavalg igennem
                        for (int y = 0; y < brugerValgPizza.length; y++) {
                            //Create nyt selvstaendigt pizza objekt som har samme values som den valgte pizza fra menuen
                            //Basically kopier en pizza
                            //Hvis blot bruge reference/alias saa bliver pizza i ordre ikke selvstaendige
                            //det ville medfoere at en kommentar til en Margarita i en ordre ville blive en kommentar til alle Margarita i alle ordre og i menu
//                            valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[y][0]).copyOf();
                            valgtPizza = menuPizzaUdvalg.get(HelpMethods.getMenuIndexUdFraPizzaNummer(menuPizzaUdvalg, brugerValgPizza[y][0])).copyOf();

                            //Loop det antal gange denne pizza er valgt (fx med 3*6 )
                            for (int i = 0; i < brugerValgPizza[y][1]; i++) {
                                //Tilfoej nyt selvstaendigt pizza objekt til ordrens pizza liste (se ovenstaaende)
                                //Her bruges overloaded constructor i Pizza som kan tage en eksisterende pizza som parameter og snuppe values fra den
                                tempOrdre.tilfoejPizzaTilOrdre(new Pizza(valgtPizza));
                            }
                        }

                    }

                    //Sorter ordrens pizzaliste ud fra pizza nr for overblik
                    tempOrdre.sorterOrdrePizzaListe();

                    //Opdater ordrens totale pris ud fra alle pizzaer i ordrens pizzaliste
                    tempOrdre.opdaterTotalPrisOrdre();

                    break;


                case 2:
                    //2. Fjern en pizza fra ordre

                    //Tjek om ordre liste er tom
                    if (tempOrdre.ordrePizzaListe.isEmpty()) {
                        System.out.println("Du har ikke tilføjet pizza til ordren");
                    } else {
                        //Kald metode til at udskrive ordrens pizza liste med et id tal ude til venstre saa der er noget for bruger at vaelge ud fra
                        tempOrdre.udskrivOrdrePizzaListeMedId();

                        System.out.println("Enter [nummer] på pizza som du vil slette fra ordre (0 for exit)");

                        //brug helper method til at modtage valg fra bruger ud fra id 0 til antal pizzaer i listen. 0 er til exit.
                        tempValg = HelpMethods.getValgInt(0, tempOrdre.ordrePizzaListe.size(), false, scanner);

                        //Fjern pizza fra ordrens pizza liste med remove
                        //Hvis ikke tastet 0
                        //Index justeret da foerste pizza er index 0 og ikke index 1
                        if (tempValg != 0) {

                            tempOrdre.ordrePizzaListe.remove(tempValg - 1);
                            tempOrdre.opdaterTotalPrisOrdre();
                        }
                    }

                    break;

                case 3:
                    //3. Fjern samtlige pizza fra ordre
                    System.out.println("Er du sikker på at du vil fjerne alle pizzaerne fra denne ordre?  (1)NEJ (2)JA");

                    //Brug helper method til at modtage 1 eller 2 uden at ren Enter er tilladt
                    //Toem ordrens pizza liste hvis bekraefte. Det er basically clear paa listen
                    if (HelpMethods.getValgInt(1, 2, false, scanner) == 2) {
                        tempOrdre.toemOrdrePizzaListe();
                    }

                    //Opdater ordrens totalpris ud fra pizzaerne i ordren
                    //Det vil sige 0 kr. Men hvis andet end pizza tilfoejes senere saa godt at der ikke er literal 0 her
                    tempOrdre.opdaterTotalPrisOrdre();
                    break;


                case 4:
                    //4. Tilføj/ændre kommentar til enkelte pizzaer

                    //Tjek om der er pizza i ordren
                    if (tempOrdre.ordrePizzaListe.isEmpty()) {
                        System.out.println("Du har ikke tilføjet pizza til ordren");
                    } else {
                        //Kald metode til at udskrive ordrens pizza liste med et id tal ude til venstre saa der er noget for bruger at vaelge ud fra
                        tempOrdre.udskrivOrdrePizzaListeMedId();

                        System.out.println("Enter [nummer] på pizza som du vil ændre kommentar på (0 for exit)");

                        //Modtag valg fra bruger via helper method uden at ren Enter er tilladt.
                        tempValg = HelpMethods.getValgInt(0, tempOrdre.ordrePizzaListe.size(), false, scanner);

                        //Hvis valgt en pizza og ikke exit
                        if (tempValg != 0) {
                            System.out.println("Tilføj kommentar til pizza. Max 40 karakterer (Enter, hvis ingen kommentarer:");

                            //Modtag String fra bruger paa max 40 characters hvor ren Enter er tilladt
                            kommentarPizza = HelpMethods.getValidatedTekstLaengde(40, true, scanner);

                            //tag ordrens pizzaliste og tag relevante pizza og brug dennes getter
                            //index justeret for at pizza id 1 har index 0
                            tempOrdre.ordrePizzaListe.get(tempValg - 1).setKommentar(kommentarPizza);


                        }


                    }


                    break;

                case 5:
                    //5. Tilføj tlf nummer til afhentning

                    //Vis nuvaerende indtastede tlf nummer via getter
                    //Hvis endnu ikke indtastet et tlf nummer saa er value "Endnu ikke opgivet" pga constructor
                    System.out.printf("Dette er det nuværende registrerede tlf nummer:\n%s\n", tempOrdre.getTlfNummerKunde());

                    System.out.println("Indtast et dansk telefonnummer (Enter for exit):");

                    //Modtag validated DK tlf nummer via helper method hvor true angiver at ogsaa ren Enter er tilladt
                    tlfnummer = HelpMethods.getValidatedTlfDK(true, scanner);

                    //Hvis bruger ikke indtastet ren Enter saa aendre tlf nummer
                    if (!tlfnummer.isEmpty()) {
                        tempOrdre.setTlfNummerKunde(tlfnummer);
                    }

                    break;


                case 6:
                    //6. Tilføj/ret afhentningstidspunkt

                    //Tjek at value afhentningstidspunkt ikke er null
                    if (tempOrdre.getTidspunktForAfhentning() != null) {
                        //Print nuvaerende afhentningstidspunkt via getter og formatering af helper method
                        System.out.printf("Nuværende afhentning er angivet til: %s \n", tempOrdre.getTidspunktForAfhentning().format(formatDKKlokken));
                    }
                    //Modtag tidspunkt fra bruger i format LocalDateTime og validated som vaerende senere end nu og med acceptabel tidsforskel
                    afhentningsTidspunktValidated = HelpMethods.getValidatedTidspunktSenere(true, scanner);
                    //Aendre afhentningstidspunkt
                    tempOrdre.setTidspunktForAfhentning(afhentningsTidspunktValidated);
                    break;

                case 7:
                    //7. Tilføj kommentar til samlet ordre

                    //Vis nuvaerende kommentar hvis ikke null
                    if (!tempOrdre.getKommentar().isEmpty()) {
                        System.out.printf("Nuværende kommentar til ordren er:\n%s\n\n", tempOrdre.getKommentar());
                    }


                    System.out.println("Indtast en kommentar til den samlede ordre (max 80 karakterer. Enter for exit ændringer): ");

                    //Modtag text fra bruger af max 80 character laengde hvor ogsaa ren Enter er tilladt
                    kommentarOrdre = HelpMethods.getValidatedTekstLaengde(80, true, scanner);

                    //Hvis bruger ikke indtastede ren Enter, saa aendre kommentar paa ordren
                    if (!kommentarOrdre.isEmpty()) {
                        tempOrdre.setKommentar(kommentarOrdre);
                    }

                    break;

                case 8:
                    //8. Sæt ordren i gang
                    //Dvs accepter den saa den bliver aktuel for Mario

                    //Hvis ingen pizzaer i ordren saa send bruger tilbage til opret ordre menu
                    if (tempOrdre.getOrdrePizzaListe().isEmpty()) {
                        System.err.println("Du mangler at tilføje pizza. Enter for at komme tilbage");

                        //Afventer Enter eller anden indtastning for at bruger naar at se besked
                        scanner.nextLine();
                        continue;
                        //Hvis ingen tlf nummer angivet saa send bruger tilbage til opret ordre menu
                    } else if (tempOrdre.getTlfNummerKunde().equals("Endnu ikke opgivet")) {
                        System.err.println("Du har ikke registreret tlf nummer. Enter for at komme tilbage");

                        //Afventer Enter eller anden indtastning for at bruger naar at se besked
                        scanner.nextLine();
                        continue;
                        //Tjek om der er angivet tidspunkt for afhentning
                    } else if (tempOrdre.getTidspunktForAfhentning() == null) {
                        //Hvis nej, saa giv mulighed for tilbage eller blot hurtigst muligt
                        //Hurtigst mugligt saetter afhentningstidspunkt til nuvaerende tidspunkt
                        System.out.println("Du har ikke angivet afhentningstidspunkt");
                        System.out.println("(1) for hurtigst muligt (2) hvis det er en fejl");

                        //Modtag valg fra bruger via helper method uden mulighed for ren Enter
                        if (HelpMethods.getValgInt(1, 2, false, scanner) == 1) {
                            //Hvis 1: Saet afhentning til nuvaerende tidspunkt og afslut opret ordre
                            tempOrdre.setTidspunktForAfhentning(LocalDateTime.now());
                            orderVidere = true;
                        } else {
                            //Hvis 2: tilbage til opret ordre menu
                            continue;
                        }
                    }

                    //juster flag til at ordren er klar til at blive aktiveret
                    orderVidere = true;

                    break;


                default:
                    //Hvis fejl med switch
                    System.out.println("not done or out of range");
                    break;
            }

            //Print ordren til skaermen for overblik for brugeren foer bruge menuen for aendre values i ordren
            //Men goer ikke dette hvis ordren klar til at blive aktiveret og brugeren paa vej til hovedmenuen i programmet
            if (!orderVidere) {
                System.out.println(tempOrdre);
            }
        }


        //returner den udfyldte ordre
        return tempOrdre;

    }

    public void menuRedigerMenukort(Scanner scanner) {


        boolean videre = false;
        boolean hovedmenu = false;

        while (true) {

            System.out.println("1: Rediger menukort");
            System.out.println("2: Rediger ingredienser");
            System.out.println("0: Vend tilbage til hovedmenu");
            System.out.println("Vælg en mulighed");

            try {

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:

                        while (true) {

                            System.out.println("1: Tilføj pizza");
                            System.out.println("2: Slet pizza");
                            System.out.println("3: Vis menukort");
                            System.out.println("0: Tilbage til rediger menu");
                            System.out.println("Vælg en mulighed");

                            try {

                                choice = scanner.nextInt();

                                switch (choice) {
                                    case 1:
                                        Menukort.opretPizza(Menukort.getIngredienser(), Menukort.getMenu());
                                        videre = true;
                                        break;
                                    case 2:
                                        Menukort.sletPizza(Menukort.getMenu(), Menukort.getIngredienser());
                                        videre = true;
                                        break;
                                    case 3:
                                        Menukort.visSorteretMenu(Menukort.getMenu());
                                        break;
                                    case 0:
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

                    case 0:
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
                scanner.nextLine();
                break;
            }

        }

    }

    public void ingrediensBrugerMenu(Scanner scanner) {
        ArrayList<String> ingredienser = Menukort.getIngredienser();
        Menukort.visIngredienser(ingredienser);

        boolean redigerVidere = false;

        while (!redigerVidere) {
            System.out.println("\n1. Tilføj ny ingrediens");
            System.out.println("2. Slet ingrediens fra listen");
            System.out.println("3. Vis ingrediensliste");
            System.out.println("0. Exit");


            switch (getValgInt(0, 3, false, scanner)) {
                case 0:
                    //0. Exit uden at gennemføre ændring
                    //juster flag til afslutte og returnere ordre
                    redigerVidere = true;
                    break;

                case 1:
                    //1. tilføj ny ingrediens til arrayList
                    Menukort.tilfoejIngrediens(ingredienser, scanner);
                    break;

                case 2:
                    //2. slet ingrediens fra listen
                    Menukort.sletIngrediens(ingredienser, scanner);
                    break;

                case 3:
                    //3. vis ingredienslisten
                    Menukort.visIngredienser(ingredienser);
                    break;



            }
        }
    }

    public void menuStatistik(ArrayList<Ordre> aktiveOrdre, ArrayList<Ordre> dagensAfsluttedeOrdre, ArrayList<Ordre> ordreHistorikListe, Scanner scanner, FileIO myFileHandler) {

        boolean redigerVidere = false;

        while (!redigerVidere) {
            System.out.println("1. Se omsætning for dagens afsluttede ordre og ");
            System.out.println("   dagens salgstal for pizzaer ud fra afsluttede ordre");
            System.out.println();
            System.out.println("2. Se totale omsætning per dato i historik over tid inklusiv dagens afsluttede ordrer");
            System.out.println("   og salgstal for pizzaer i hele perioden i historik inklusiv dagens afsluttede ordrer");
            System.out.println("0. Exit");

            switch(getValgInt(0, 2, false, scanner)) {
                case 1:
                    if (!dagensAfsluttedeOrdre.isEmpty()) {

                        System.out.printf("Dagens omsætning: %.2f kr.\n", Main.dagensOmsaetning(dagensAfsluttedeOrdre));

                        Main.mestPopulaerePizzaer(dagensAfsluttedeOrdre);
                    } else {
                        System.out.println("Der er endnu ingen afsluttede ordrer i dag, så ingen omsætning");
                    }
                    break;
                case 2:

                    try {
                        //Hent alle gamle ordre objekter fra fil til ordreHistorik liste ved hjaelp af method i instance af FileIO class
                        if (myFileHandler != null) {
                            ordreHistorikListe = myFileHandler.read();
                        }

                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at læse");
                    }

                    //Tilfoej dagens afsluttede ordre til beregning af statistik paa historik
                    if (!dagensAfsluttedeOrdre.isEmpty()) {
                        ordreHistorikListe.addAll(dagensAfsluttedeOrdre);

                    }

                    //Vis omsaetning paa forskellige datoer i hele historik (plus dagens afsluttede ordre). Med ugedag.
                    Main.historikOmsaetningDatoer(ordreHistorikListe);

                    //Vis totale omsaetning for hele historik (og dagens afsluttede ordre)
                    System.out.printf("Total omsætning:          %10.2f kr.\n(Historik inklusiv dagens afsluttede ordre)\n", Main.historikOmsaetning(ordreHistorikListe));

                    System.out.println("--------------------------------------------------------------------------");

                    //Overskrift
                    System.out.println("\nAll time (historik inklusiv dagens afsluttede ordre)");

                    //Method til at lave histogram over frekvens af pizzaer og printe med hoejeste foerst
                    //Alle solgte pizzaer i historik (plus dagens afsluttede ordre)
                    Main.mestPopulaerePizzaer(ordreHistorikListe);

                    System.out.println("--------------------------------------------------------------------------");

                    //Toem listen da ikke brug for at have hele historik i memory
                    ordreHistorikListe.clear();


                    break;

                case 0:
                    redigerVidere = true;
                    break;

            }
        }
    }

    public void menuUdvikler(ArrayList<Ordre> aktiveOrdre, ArrayList<Ordre> dagensAfsluttedeOrdre, ArrayList<Ordre> ordreHistorikListe, Scanner scanner, FileIO myFileHandler) {

        boolean redigerVidere = false;

        while (!redigerVidere) {

            System.out.println("1. Tøm uafsluttede ordre liste for ordre og slet fil");
            System.out.println("2. Tøm dagens afsluttede ordre liste for ordre og slet fil");
            System.out.println("3. Gem uafsluttede ordre til egen backup fil");
            System.out.println("4. Hent uafsluttede ordre fra egen backup fil");
            System.out.println("5. Gem dagens afsluttede ordre til egen backup fil");
            System.out.println("6. Hent dagens afsluttede ordre fra egen backup fil");
            System.out.println("7. Goer samtlige pizzaer i uafsluttede ordre klar");
            System.out.println("8. Flyt ordre fra uafsluttede til afsluttede ordre");
            System.out.println("9. Aendre dato paa alle dagens afsluttede ordre");
            System.out.println("10. Flyt afsluttede ordre til ordre historik");
            System.out.println("0. Exit");



            switch (getValgInt(0, 10, false, scanner)) {
                case 1:
                    //1. Tøm uafsluttede ordre liste for ordre og slet fil
                    aktiveOrdre.clear();

                    try {
                        myFileHandler.deleteUafsluttedeOrdreFile();
                    } catch (NullPointerException e) {
                        System.err.println("error delete file uafsluttede ordre backup");
                    }
                    break;
                case 2:
                    //2. Tøm dagens afsluttede ordre liste for ordre og slet fil
                    dagensAfsluttedeOrdre.clear();
                    try {
                        myFileHandler.deleteafsluttedeOrdreFile();
                    } catch (NullPointerException e) {
                        System.err.println("error delete file afsluttede ordre backup  exp");
                    }
                    break;
                case 3:
                    //3. Gem uafsluttede ordre til egen backup fil
                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }

                    } else {
                        System.err.println("Ingen ordre at gemme til fil for dagens uafsluttede ordre   ");
                    }

                    break;
                case 4:
                    //4. Hent uafsluttede ordre fra egen backup fil
                    try {
                        //Hent alle ordre objekter fra saeerlige fil til aktive ordre liste ved hjaelp af method i instance af FileIO class
                        aktiveOrdre = myFileHandler.readAktiveOrdre();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at læse");
                    }
                    break;
                case 5:
                    //5. Gem dagens afsluttede ordre til egen backup fil
                    if (!dagensAfsluttedeOrdre.isEmpty()) {

                        for (Ordre ordre : dagensAfsluttedeOrdre) {

                            if (myFileHandler != null) {
                                myFileHandler.writeDagensAfsluttedeOrdre(dagensAfsluttedeOrdre);
                            } else {
                                System.err.println("Filehandler error");
                            }
                        }
                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }
                    break;
                case 6:
                    //6. Hent dagens afsluttede ordre fra egen backup fil

                    if (myFileHandler != null) {
                        dagensAfsluttedeOrdre = myFileHandler.readDagensAfsluttedeOrdre();
                    } else {
                        System.err.println("Filehandler error");
                    }
                    break;
                case 7:
                    //7. Goer samtlige pizzaer i uafsluttede ordre klar

                    for (Ordre ordre : aktiveOrdre) {
                        for (Pizza pizza : ordre.getOrdrePizzaListe()) {
                            pizza.setPizzaFaerdig(true);
                        }
                    }

                    break;
                case 8:
                    //8. Flyt ordre fra uafsluttede til afsluttede ordre
                    if (!aktiveOrdre.isEmpty()) {

                        for (int i = 0; i < aktiveOrdre.size(); i++) {
                            aktiveOrdre.get(i).setErAfsluttet(true);
                            dagensAfsluttedeOrdre.add(aktiveOrdre.remove(i));

                        }
                        try {
                            myFileHandler.deleteUafsluttedeOrdreFile();
                            for (Ordre ordre : dagensAfsluttedeOrdre) {

                                if (myFileHandler != null) {
                                    myFileHandler.writeDagensAfsluttedeOrdre(dagensAfsluttedeOrdre);
                                } else {
                                    System.err.println("Filehandler error");
                                }
                            }


                        } catch (NullPointerException e) {
                            System.err.println("error delete file uafsluttede ordre backup");
                        }



                    }
                    break;
                case 9:
                    //9. Aendre dato paa alle dagens afsluttede ordre


                    System.out.println("Dag i måneden: ");
                    int day = getValgInt(1, 28, false, scanner);

                    System.out.println("Måned: ");
                    int month = getValgInt(1, 12, false, scanner);

                    System.out.println("Årstal: ");
                    int year = getValgInt(2023, 2025, false, scanner);

                    LocalDateTime tempTimeAfhent = LocalDateTime.now();
                    tempTimeAfhent = tempTimeAfhent.withDayOfMonth(day);
                    tempTimeAfhent = tempTimeAfhent.withMonth(month);
                    tempTimeAfhent = tempTimeAfhent.withYear(year);

                    LocalDateTime tempTimeBestilt = tempTimeAfhent.minusHours(1);

                    for (Ordre ordre : dagensAfsluttedeOrdre) {
                        ordre.setTidspunktForOrdre(tempTimeBestilt);
                        ordre.setTidspunktForAfhentning(tempTimeAfhent);
                    }
                    break;
                case 10:
                    //10. Flyt afsluttede ordre til ordre historik

                    if (!dagensAfsluttedeOrdre.isEmpty()) {

                        for (Ordre ordre : dagensAfsluttedeOrdre) {
                            myFileHandler.write(ordre);
                        }

                        dagensAfsluttedeOrdre.clear();
                        try {
                            myFileHandler.deleteafsluttedeOrdreFile();
                        } catch (NullPointerException e) {
                            System.err.println("error delete file afsluttede ordre backup");
                        }

                    } else {
                        System.err.println("Ingen afsluttede ordre at gemme til fil");
                    }
                    break;

                case 0:
                    redigerVidere = true;
                    break;
                default:
                    System.err.println("switch error");
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
