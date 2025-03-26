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


    //Indholder midlertidigt Menu til test af ordre og filhaandtering i forbindelse med ordre objekter
    //Men ogsaa blivende variabler
    public static void run() {

        ArrayList<Ordre> aktiveOrdre;
        aktiveOrdre = new ArrayList<>();

        //Dagens ordre som er afsluttede men endnu ikke er skrevet til fil med ordre historik
        ArrayList<Ordre> dagensAfsluttedeOrdre;

        //Indeholder pizzaerne som er menuen
        ArrayList<Pizza> menuPizzaUdvalg;
        menuPizzaUdvalg = new ArrayList<Pizza>();

        Scanner scanner = new Scanner(System.in);


        // Lav instance af FileIO class til filhaandtering af ordre objekter
        FileIO myFileHandler = null;
        try {
            myFileHandler = new FileIO();
        } catch (IOException e) {
            System.err.println("FileIO objekt kunne ikke instantiates");
            e.printStackTrace();
        }

        //Hent pizza menu fra fil og saet ind i ArrayList
        menuPizzaUdvalg = Menukort.getMenu();


        //HER STARTER MIDLERTIDIG MENU
        // menu dummy til test oprette og write/read ordre objekter
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("\n1. Opret ordre og tilfoej til ordre liste");
            System.out.println("2. Se ordre i ordre liste");
            System.out.println("3. Toem ordre liste for ordre");
            System.out.println("4. Test at aendre anden pizza i foerste ordre i ordre liste til klar");
            System.out.println("5. Tilfoej ordre fra liste til fil");
            System.out.println("6. Hent alle ordre fra fil til ordre liste");
            System.out.println();
            System.out.println("0. Exit");

            switch (HelpMethods.getValgInt(0, 6, false, scanner)) {
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
                    //2. Se ordre i ordre liste

                    //Tjek om der er nogen ordre i ordre liste
                    if (!aktiveOrdre.isEmpty()) {
                        //svarer til System.out.println paa samtlige ordre i ordre liste
                        aktiveOrdre.forEach(System.out::println);
                    } else {
                        System.err.println("Der er ingen ordre at printe");
                    }
                    break;
                case 3:
                    //3. Toem ordre liste for ordre
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
                    //5. Tilfoej ordre fra liste til fil

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
                    //6. Hent alle ordre fra fil til ordre liste

                    try {
                        //Hent alle ordre objekter fra fil til ordre liste ved hjaelp af method i instance af FileIO class
                        aktiveOrdre = myFileHandler.read();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at laese");
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
                        valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[0][0]).copyOf();

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
                            valgtPizza = menuPizzaUdvalg.get(brugerValgPizza[y][0]).copyOf();

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






}


