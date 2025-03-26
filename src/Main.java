import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {


    public static void main(String [] args) {
        run();

    }


    //Indholder midlertidigt Menu til test af ordre og filhaandtering i forbindelse med ordre objekter
    //Men ogsaa blivende variabler
    public static void run() {


        //Dagens ordre som endnu ikke er afsluttede
        ArrayList<Ordre> aktiveOrdre;
        aktiveOrdre = new ArrayList<>();

        //Dagens ordre som er afsluttede men endnu ikke er skrevet til fil med ordre historik
        ArrayList<Ordre> dagensAfsluttedeOrdre;
        dagensAfsluttedeOrdre = new ArrayList<Ordre>();

        //Indeholder pizzaerne som er menuen
        ArrayList<Pizza> menuPizzaUdvalg;
        menuPizzaUdvalg = new ArrayList<Pizza>();

        //Indeholder pizzaer relevante for Mario
        //For at undgaa ren print
        //Paa den her maade bliver det nemmere at registrere bestemte pizzaer som faerdige inde i relevante ordre
        ArrayList<Pizza> marioPizzaListe;
        marioPizzaListe = new ArrayList<>();


        //ArrayList til at hente gamle ordre i historik fil naar lave fuld statistik
        ArrayList<Ordre> ordreHistorikListe;
        ordreHistorikListe = new ArrayList<>();


        Scanner scanner = new Scanner(System.in);


        // Lav instance af FileIO class til filhaandtering af ordre objekter
        FileIO myFileHandler = null;
        try {
            myFileHandler = new FileIO();
        } catch (IOException e) {
            System.err.println("FileIO historik objekt kunne ikke instantiates");
            e.printStackTrace();
        }


        //Hent pizza menu fra fil og saet ind i ArrayList
        menuPizzaUdvalg = Menukort.getMenu();


        //HER STARTER MIDLERTIDIG MENU
        // menu dummy til test oprette og write/read ordre objekter
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("\n1. Opret ordre og tilfoej til ordre liste");
            System.out.println("2. Se ordre i aktive ordre liste");
            System.out.println("3. Toem aktive ordre liste for ordre");
            System.out.println();
            System.out.println("4. Test at aendre anden pizza i foerste ordre i ordre liste til klar");
            System.out.println();
            System.out.println("5. Flyt afsluttede ordre til ordre historik");
            System.out.println("6. Vis statistik paa ordre historik gamle ordre");
            System.out.println();
            System.out.println("7. Vis Mario liste og tlf numre paa ordre som er klar til udlevering");
            System.out.println("8. Marker pizza på Marios liste som faerdig");
            System.out.println();
            System.out.println("9. Gem aktive ordre til egen backup fil");
            System.out.println("10. Hent aktive ordre fra egen backup fil");
            System.out.println();
            System.out.println("11. Gem dagens afsluttede ordre til egen backup fil");
            System.out.println("12. Hent dagens afsluttede ordre fra egen backup fil");
            System.out.println();
            System.out.println("13. Vis statistik for dagen");
            System.out.println();
            System.out.println("14. Se ordre i dagens afsluttede ordre liste");
            System.out.println("15. Toem dagens afsluttede ordre liste for ordre uden at gemme dem");
            System.out.println("16. Vis Ordre Historik fra filen");
            System.out.println("17. Flyt klar ordre fra aktiv til afsluttede ordre");


            System.out.println();
            System.out.println("0. Exit");

            switch (HelpMethods.getValgInt(0, 17, false, scanner)) {
                case 0:
                    //0. Exit
                    //ret flag til at stoppe loop
                    exitApp = true;
                    break;
                case 1:
                    //1. Opret ordre og tilfoej til ordre liste
                    //Tilfoej en ny ordre til listen af ordre ved at kalde method for opret ordre med pizza menu som parameter
                    aktiveOrdre.add(menuOpretOrdre(menuPizzaUdvalg, scanner));
                    break;
                case 2:
                    //2. Se ordre i aktive ordre liste

                    //Tjek om der er nogen ordre i aktive 2ordre liste
                    if (!aktiveOrdre.isEmpty()) {
                        //svarer til System.out.println paa samtlige ordre i aktive ordre liste
                        aktiveOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen ordre at printe");
                    }


                    break;
                case 3:
                    //3. Toem aktive ordre liste for ordre
                    //
                    aktiveOrdre.clear();
                    break;
                case 4:
                    //4. Test at aendre anden pizza i foerste ordre i ordre liste til klar
                    //Har bare vaeret brugt til test
                    //Tjek at der er min 1 ordre i ordre liste og at denne har min 2 pizzaer
                    //for at undgaa fejl ved at tilgaa objekt som ikke findes
                    if (!aktiveOrdre.isEmpty() && aktiveOrdre.get(0).getOrdrePizzaListe().size() >= 2) {
                        //tag foerste ordre i ordre liste tag dennes liste af pizzaer og tag anden pizza og brug dens setter til pizzaFaerdig attribut
                        aktiveOrdre.get(0).getOrdrePizzaListe().get(1).setPizzaFaerdig(true);
                    } else {
                        System.err.println("Den pizza og/eller ordre findes ikke");
                    }
                    break;

                case 5:
                    //5. Flyt afsluttede ordre til ordre historik

                    //Temp code til at lave dummies med ved at aendre dato paa ordre foer de gemmes i historik
                    //Saa der kan laves ordre tilbage i tid
                    //Temp code start
                    LocalDateTime nu = LocalDateTime.now();
                    LocalDateTime nyMaaned = nu.withMonth(3);
                    LocalDateTime nyMaanedOgDag = nyMaaned.withDayOfMonth(18);

                    int tempHour = nyMaanedOgDag.getHour();
                    int senereHourTilAfhentning = tempHour + 1;


                    for (Ordre ordre : dagensAfsluttedeOrdre) {
                        ordre.setTidspunktForOrdre(nyMaanedOgDag);
                        ordre.setTidspunktForAfhentning(nyMaanedOgDag.withHour(senereHourTilAfhentning));

                    }
                    //Temp Code slut

                    if (!dagensAfsluttedeOrdre.isEmpty()) {

                        for (Ordre ordre : dagensAfsluttedeOrdre) {
                            myFileHandler.write(ordre);
                        }

                        dagensAfsluttedeOrdre.clear();

                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }
                    break;

                case 6:
                    //6. Vis statistik paa ordre historik gamle ordre

                    try {
                        //Hent alle gamle ordre objekter fra fil til ordreHistorik liste ved hjaelp af method i instance af FileIO class
                        ordreHistorikListe = myFileHandler.read();


                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
                    }

                    //Udvid dagensOmsaetning() til at vise omsaetning fordelt paa datoer
                    historikOmsaetningDatoer(ordreHistorikListe);

                    //Vis totale omsaetning for hele historik
                    //System.out.printf("Historik total omsaetning: %.2f kr.\n", historikOmsaetning(ordreHistorikListe));

                    //Overskrift
                    System.out.println("\nAll time");

                    //Method til at lave histogram over frekvens af pizzaer og printe med hoejeste foerst
                    mestPopulaerePizzaer(ordreHistorikListe);


                    break;
                case 7:
                    //7. Vis Mario liste og tlf numre paa ordre som er klar til udlevering

                    opdaterMarioListe(aktiveOrdre, marioPizzaListe, true);
                    break;
                case 8:
                    //8. Marker pizza på Marios liste som faerdig
                    markerPizzaSomFaerdig(aktiveOrdre, marioPizzaListe, scanner);
                    break;
                case 9:
                    //9. Gem aktive ordre til egen fil
                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }

                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }
                    break;
                case 10:
                    //10. Hent aktive ordre fra egen fil
                    try {
                        //Hent alle ordre objekter fra saeerlige fil til aktive ordre liste ved hjaelp af method i instance af FileIO class
                        aktiveOrdre = myFileHandler.readAktiveOrdre();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
                    }

//                    //temp loop til at goere inlaese aktive ordre til afsluttede for at lave dummies
//                    for (int i = 0; i < aktiveOrdre.size(); i++) {
//                        aktiveOrdre.get(i).setErAfsluttet(true);
//                        dagensAfsluttedeOrdre.add(aktiveOrdre.get(i));
//
//                    }


                    break;

                case 11:
                    //11. Gem dagens afsluttede ordre til egen backup fil
                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeDagensAfsluttedeOrdre(aktiveOrdre);
                        }

                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }

                    break;
                case 12:
                    //12. Hent dagens afsluttede ordre fra egen backup fil
                    try {
                        //Hent alle ordre objekter fra saeerlige fil til dagens afsluttede ordre liste ved hjaelp af method i instance af FileIO class
                        dagensAfsluttedeOrdre = myFileHandler.readDagensAfsluttedeOrdre();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
                    }
                    break;
                case 13:
                    //13. Vis statistik for dagen

                    System.out.printf("Dagens omsaetning: %.2f kr.\n", dagensOmsaetning(dagensAfsluttedeOrdre));

                    if (!dagensAfsluttedeOrdre.isEmpty()) {
                        System.out.println("\nDagens");
                    }
                    mestPopulaerePizzaer(dagensAfsluttedeOrdre);


                    break;
                case 14:
                    //14. Se ordre i dagens afsluttede ordre liste

                    //Tjek om der er nogen ordre i dagens afsluttede ordre liste
                    if (!dagensAfsluttedeOrdre.isEmpty()) {
                        //svarer til System.out.println paa samtlige ordre i dagens afsluttede ordre liste
                        dagensAfsluttedeOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen ordre at printe");
                    }

                    break;

                case 15:
                    //15. Toem dagens afsluttede ordre liste for ordre uden at gemme dem
                    dagensAfsluttedeOrdre.clear();
                    break;

                case 16:
                    //16. Vis Ordre Historik fra filen

                    try {
                        //Hent alle gamle ordre objekter fra fil til ordreHistorik liste ved hjaelp af method i instance af FileIO class
                        ordreHistorikListe = myFileHandler.read();


                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
                    }

                    System.out.println(ordreHistorikListe);


                    break;
                case 17:
                    //17. Flyt klar ordre fra aktiv til afsluttede ordre

                    if (!aktiveOrdre.isEmpty()) {

                        for (int i = 0; i < aktiveOrdre.size(); i++) {
                            dagensAfsluttedeOrdre.add(aktiveOrdre.remove(i));

                        }
                    }


                    break;


            }

        }

    }

    //Denne method kan bruges til at oprette en ny ordre fra bunden og faa alle relevante vaerdier fyldt ud
    //foer den returneres
    public static Ordre menuOpretOrdre(ArrayList<Pizza> menuPizzaUdvalg, Scanner scanner) {

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
            switch (HelpMethods.getValgInt(0, 8, false, scanner)) {
                case 0:
                    //0. Exit uden at gennemføre ordre
                    //juster flag til afslutte og returnere ordre
                    orderVidere = true;
                    break;

                case 1:
                    //1. Tilføj pizza til ordre

                    //Denne method til at vise pizza menu skal muligvis udskiftes med den fra Menukort class efter merge
                    udskrivPizzaUdvalg(menuPizzaUdvalg);

                    //Brug helper method til at modtage brugers multivalg af pizzaer. Modtages i two dimensional array med pizza nr og antal paa hvert index
                    brugerValgPizza = HelpMethods.getArrayValgPizzaSomFindes(menuPizzaUdvalg, scanner);

                    //Tjek om der kun er valgt en type pizza og kun 1 stk af denne
                    if (brugerValgPizza.length == 1 && brugerValgPizza[0][1] == 1) {
                        //Create nyt selvstaendigt pizza objekt som har samme values som den valgte pizza fra menuen
                        //Basically kopier en pizza
                        //Hvis blot bruge reference/alias saa bliver pizza i ordre ikke selvstaendige
                        //det ville medfoere at en kommentar til en Margarita i en ordre ville blive en kommentar til alle Margarita i alle ordre og i menu
                        valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[0][0] - 1).copyOf();

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
                            valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[y][0] - 1).copyOf();

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

                        System.out.println("Enter [numnmer] på pizza som du vil slette fra ordre (0 for exit)");

                        //brug helper method til at modtage valg fra bruger ud fra id 0 til antal pizzaer i listen. 0 er til exit.
                        tempValg = HelpMethods.getValgInt(0, tempOrdre.ordrePizzaListe.size(), false, scanner);

                        //Fjern pizza fra ordrens pizza liste med remove
                        //Hvis ikke tastet 0
                        //Index justeret da foerste pizza er index 0 og ikke index 1
                        if (tempValg != 0) {

                            tempOrdre.ordrePizzaListe.remove(tempValg - 1);
                        }
                    }

                    break;

                case 3:
                    //3. Fjern samtlige pizza fra ordre
                    System.out.println("Er du sikker på at du vil fjerne alle pizza fra denne ordre?  (1)NEJ (2)JA");

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

                        System.out.println("Enter [numnmer] på pizza som du vil ændre kommentar på (0 for exit)");

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


    //Method til udskrive pizza menu fra dengang vi opererede med faste pladser ud fra pizza nr og null i mellemrum
    public static void udskrivPizzaUdvalg(ArrayList<Pizza> menuPizzaUdvalg) {
        for (int i = 0; i < menuPizzaUdvalg.size(); i++) {
            if (menuPizzaUdvalg.get(i) != null) {
                System.out.println(menuPizzaUdvalg.get(i));
            }
        }

    }

    //Opdater Marios liste der indeholder endnu ikke bagte pizzaer fra dagens aktive ordre
    //Hvis visListe true saa printes listen ogsaa
    public static void opdaterMarioListe(ArrayList<Ordre> aktiveOrdre, ArrayList<Pizza> marioPizzaListe, boolean visListe) {

        //Forbered StringBuilder til at tilfoeje tlf numre paa ordre som er faerdige
        StringBuilder ordrerSomErFaerdige = new StringBuilder("Tlf nummer paa ordre som er klar til udlevering");

        //Toem listen foer opdatering af den, hvis ikke allerede tom
        if (!marioPizzaListe.isEmpty()) {
            marioPizzaListe.clear();
        }

        //Sorter foerst aktive ordre ud fra ordres afhentningstidspunkt
        //Pizzaerne i ordren ved ikke selv hvornaae de skal afhentes
        //saa ved at snuppe pizza fra ordrer i raekkefoelge kan ordrens afhentningstidspunkt bruges i sektioner
        aktiveOrdre.sort(Comparator.comparing(Ordre::getTidspunktForAfhentning));


        //Flag til at styre at overskriften til pizzaer i Marios liste kun udskrives hvis listen ikke ender tom
        boolean ikkeMereOverskriftPizza = false;

        //Flag til at skrive overskrift til tlf numre paa ordrer hvor alle pizzaer er faerdige
        boolean ikkeMereOverskriftTlf = false;

        //Flag til at vise status paa om en tjekket ordre har samtlige pizza klar
        boolean alleOrdrensPizzaerErKlar;

        //Gaa gennem samtlige aktive ordre
        for (Ordre ordre : aktiveOrdre) {

            //Saet flag til at denne ordre som udgangspunkt har alle pizza klar
            alleOrdrensPizzaerErKlar = true;

            //Gaa gennem samtlige pizzaer i den enkelte ordre
            for (Pizza pizza : ordre.getOrdrePizzaListe()) {
                //Tjek om specifik pizza er ufaerdig
                if (!pizza.getPizzaFaerdig()) {
                    //Hvis foerst fundne ufaerdige pizza overhovedet, saa print overskrift til listen
                    //Hvis visListe er true
                    if (!ikkeMereOverskriftPizza && visListe) {
                        System.out.println("Afhentningstidspunkt\n  |");
                    }
                    //Tilfoej den ufaerdige til Marios liste
                    marioPizzaListe.add(pizza);

                    //Print den ufaerdige pizza forudgaaet af aktuelle ordres afhentningstidspunkt
                    //Hvis visListe er true
                    if (visListe) {
                        System.out.printf("%s  %s\n", HelpMethods.formatformatDKKlokken(ordre.getTidspunktForAfhentning()), pizza.toString());
                    }

                    //Saet flag til at overskrift allerede er udskrevet
                    ikkeMereOverskriftPizza = true;

                    //Saet flag til at denne ordres pizzaer ikke alle er klar, nu hvor en ufaerdig er fundet
                    alleOrdrensPizzaerErKlar = false;

                }


            }
            //Hvis den netop tjekkede ordre har alle pizzaer faerdige, saa udskriv ordrens Tlf nummer
            //Saa Alfonso eller Mario kan raabe afventende kunder op om at deres ordre er klar
            //Hvis visListe er true


            if (alleOrdrensPizzaerErKlar) {
                ordrerSomErFaerdige.append("  ").append(ordre.getTlfNummerKunde());
            }
        }

        if (visListe) {
            System.out.printf("\n%s\n", ordrerSomErFaerdige.toString());
        }
    }

    public static void markerPizzaSomFaerdig(ArrayList<Ordre> aktiveOrdre, ArrayList<Pizza> marioPizzaListe, Scanner scanner) {

        //Variabel til modtage valg fra bruger
        int tempValg;

        //Opdater Mario pizza liste
        opdaterMarioListe(aktiveOrdre, marioPizzaListe, false);

        //Udskriv pizzaer fra Marios pizza liste med id til at bruger kan vaelge en
        for (int i = 0; i < marioPizzaListe.size(); i++) {
            System.out.printf("[%2d] %s\n", i + 1, marioPizzaListe.get(i).toString());

        }

        System.out.println("Enter [numnmer] på pizza som du vil slette fra ordre (0 for exit)");

        //brug helper method til at modtage valg fra bruger ud fra id 0 til antal pizzaer i listen. 0 er til exit.
        tempValg = HelpMethods.getValgInt(0, marioPizzaListe.size(), false, scanner);

        //Aendre status paa pizza som faerdig
        //Her bruges Marios liste som reference saa det ikke er noedvendigt at finde relevante ordre
        //Index justeret for at id 1 er pizza index 0
        marioPizzaListe.get(tempValg - 1).setPizzaFaerdig(true);

        //Opdater Mario pizza liste
        opdaterMarioListe(aktiveOrdre, marioPizzaListe, false);

    }

    public static double dagensOmsaetning(ArrayList<Ordre> dagensAfsluttedeOrdre) {
        double dagensOmsaetningAfsluttedeOrdre = 0;

        for (Ordre ordre : dagensAfsluttedeOrdre) {
            dagensOmsaetningAfsluttedeOrdre += ordre.getPrisTotalOrdre();
        }

        return dagensOmsaetningAfsluttedeOrdre;
    }

    //Laver og printer histogram over frekvens af pizzaer i ordrer i listen der gives som argument
    //Printer pizzanavne sorteres efter hoejeste frekvens foerst
    public static void mestPopulaerePizzaer(ArrayList<Ordre> listeMedOrdrer) {

        //Tjek om der overhovedet er ordre i listen
        if (listeMedOrdrer.isEmpty()) {
            System.err.println("Ingen pizza obejkter i denne liste");
            return;
        }

        //Create et HashMap til histogrammet
        //HashMap har en Key i stedet for index til at hente Value
        //En key kan kun forekomme 1 gang
        //Her angives key til at vaere en String og dens value til at vaere en int
        //Det er til at ende med pizzanavn sat overfor frekvens
        Map<String, Integer> dagensMap = new HashMap<String, Integer>();

        //loop gennem samtlige ordrer i listen af ordrer
        for (Ordre ordre : listeMedOrdrer) {
            //I en specifik ordre gaas gennem samtlige pizzaer der maatte vaere i ordren
            for (int p = 0; p < ordre.getOrdrePizzaListe().size(); p++) {
                //Hent en specifik pizzas navn og tjek om det navn allerede forkommer i HashMap som key
                if (dagensMap.containsKey(ordre.getOrdrePizzaListe().get(p).getNavn())) {
                    //Hvis det goer saa hent nuvaerende value og overwrite key med value + 1
                    //Altsaa tael at der er fundet endnu en forekomst af pizzanavnet
                    dagensMap.put(ordre.getOrdrePizzaListe().get(p).getNavn(),
                            dagensMap.get(ordre.getOrdrePizzaListe().get(p).getNavn()) + 1);
                    //Hvis der ikke allerede er en key med pizzanavnet saa create den key med value 1
                    //Dvs registrer at dette pizzanavn foreloebigt forekommer en enkelt gang
                } else {
                    dagensMap.put(ordre.getOrdrePizzaListe().get(p).getNavn(), 1);
                }
            }
        }

        //Create en ArrayList hvor hver element er et pair af Key og Value fra Hashmap
        //.entrySet() traekker alle pairs ud af Hashmap
        //Dette goeres fordi at det ikke er muligt at sortere HashMap direkte
        //saa det er et histogram uden orden
        //Men naar disse pairs er i ArrayList kan der sorteres
        ArrayList<Map.Entry<String, Integer>> pizzaFrequency = new ArrayList<Map.Entry<String, Integer>>(dagensMap.entrySet());

        //Sorter ArrayList med pairs ud fra Comparator i separat class
        //Den definerede Comparator bruger .getValue() til at hente Value fra de pairs som sammenlignes
        Collections.sort(pizzaFrequency, new ComparatorPizzaFrequency());

        //Overskrift
        System.out.println("mest populære pizzaer\n\nNavn                 Antal solgte");

        //Her printes hvert pair i ArrayList ved hjaelp af .getKey() og .getValue();
        for (int i = 0; i < pizzaFrequency.size(); i++) {
            System.out.printf("%-20s %d\n", pizzaFrequency.get(i).getKey(), pizzaFrequency.get(i).getValue());
        }

    }

    //Returnerer hele total omsaetning for ordre i historik
    public static double historikOmsaetning(ArrayList<Ordre> ordreHistorikListe) {
        double historikOmsaetning = 0;

        for (Ordre ordre : ordreHistorikListe) {
            historikOmsaetning += ordre.getPrisTotalOrdre();
        }

        return historikOmsaetning;
    }

    //Ufaerdig method der arbejdes paa
    public static void historikOmsaetningDatoer(ArrayList<Ordre> ordreHistorikListe) {


        //Tjek om der overhovedet er ordre i listen
        if (ordreHistorikListe.isEmpty()) {
            System.err.println("Ingen pizza obejkter i denne liste");
            return;
        }


        Map<LocalDateTime, Double> historikOmsaetningDatoMap = new HashMap<LocalDateTime, Double>();


        for (Ordre ordre : ordreHistorikListe) {


            if (historikOmsaetningDatoMap.containsKey(ordre.getTidspunktForAfhentning())) {
                historikOmsaetningDatoMap.put(ordre.getTidspunktForAfhentning(),
                        (historikOmsaetningDatoMap.get(ordre.getTidspunktForAfhentning()) + ordre.getPrisTotalOrdre()));
            } else {
                historikOmsaetningDatoMap.put(ordre.getTidspunktForAfhentning(), ordre.getPrisTotalOrdre());
            }

            ArrayList<Map.Entry<LocalDateTime, Double>> datoFrequency = new ArrayList<Map.Entry<LocalDateTime, Double>>(historikOmsaetningDatoMap.entrySet());


            Collections.sort(datoFrequency, new ComparatorDatoFrequency());

            for (int i = 0; i < datoFrequency.size(); i++) {
                System.out.printf("%s %s", datoFrequency.get(i).getKey(), datoFrequency.get(i).getValue());
            }



        }


    }
}


