import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {


    public static void main(String [] args) {

        run();


    }



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

        Menu menuObject = new Menu(scanner);



    // Lav instance af FileIO class til filhaandtering af ordre objekter
        FileIO myFileHandler = null;
        try {
            myFileHandler = new FileIO();
        } catch (IOException e) {
            System.err.println("FileIO historik objekt kunne ikke instantieres");
            e.printStackTrace();
        }


        //Hent pizza menu fra fil og saet ind i ArrayList
        menuPizzaUdvalg = Menukort.getMenu();


        //Hent dagens uafsluttede ordre fra egen fil til hvis program har vaeret afsluttet uventet
        if (myFileHandler != null) {
            aktiveOrdre = myFileHandler.readAktiveOrdre();
        } else {
            System.err.println("Filehandler error");
        }
        //HER STARTER MIDLERTIDIG MENU
        // menu dummy til test oprette og write/read ordre objekter
        boolean exitApp = false;

        while (!exitApp) {
            System.out.println("1. Opret ordre og tilføj til ordre liste");
            System.out.println("2. Se ordre i aktive ordre liste");
            System.out.println("3. Tøm aktiv ordre liste for ordre");
            System.out.println();
            System.out.println("4. Test at ændre anden pizza i første ordre i ordre liste til klar");
            System.out.println("5. Flyt afsluttede ordre til ordre historik");
            System.out.println("6. Vis statistik på ordre historik gamle ordre");
            System.out.println("7. Søg efter ordre");
            System.out.println("8. Udlever/annuller ordre");
            System.out.println();
            System.out.println();
            System.out.println("10. Vis Mario liste og tlf numre på ordre som er klar til udlevering");
            System.out.println("11. Marker pizza på Marios liste som færdig");
            System.out.println();
            System.out.println("12. Gem aktive ordre til egen backup fil");
            System.out.println("13. Hent aktive ordre fra egen backup fil");
            System.out.println();
            System.out.println("14. Gem dagens afsluttede ordre til egen backup fil");
            System.out.println("15. Hent dagens afsluttede ordre fra egen backup fil");
            System.out.println();
            System.out.println("16. Vis statistik for dagen");
            System.out.println();
            System.out.println("17. Se ordre i dagens afsluttede ordre liste");
            System.out.println("18. Tøm dagens afsluttede ordre liste for ordre uden at gemme dem");
            System.out.println("19. Vis ordre historik fra filen");
            System.out.println("20. Flyt klar ordre fra aktiv til afsluttede ordre");

            System.out.println("0. Exit");

            switch (HelpMethods.getValgInt(0, 20, false, scanner)) {
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

//                    //Temp code til at lave dummies med ved at aendre dato paa ordre foer de gemmes i historik
//                    //Saa der kan laves ordre tilbage i tid
//                    //Temp code start
//                    LocalDateTime nu = LocalDateTime.now();
//                    LocalDateTime nyMaaned = nu.withMonth(9);
//                    LocalDateTime nyMaanedOgDag = nyMaaned.withDayOfMonth(10);
//                    LocalDateTime nyMaanedOgDagOgAar = nyMaanedOgDag.withYear(2024);
//
//                    int tempHour = nyMaanedOgDagOgAar.getHour();
//                    int senereHourTilAfhentning = tempHour + 1;
//
//
//                    for (Ordre ordre : dagensAfsluttedeOrdre) {
//                        ordre.setTidspunktForOrdre(nyMaanedOgDagOgAar);
//                        ordre.setTidspunktForAfhentning(nyMaanedOgDagOgAar.withHour(senereHourTilAfhentning));
//
//                    }
//                    //Temp Code slut

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
                    //6. Vis statistik paa alle ordre i historik plus dagens afslutttede ordre

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
                    historikOmsaetningDatoer(ordreHistorikListe);

                    //Vis totale omsaetning for hele historik (og dagens afsluttede ordre)
                    System.out.printf("Total omsætning:          %10.2f kr.\n(Historik inklusiv dagens afsluttede ordre)\n", historikOmsaetning(ordreHistorikListe));

                    System.out.println("--------------------------------------------------------------------------");

                    //Overskrift
                    System.out.println("\nAll time (historik inklusiv dagens afsluttede ordre)");

                    //Method til at lave histogram over frekvens af pizzaer og printe med hoejeste foerst
                    //Alle solgte pizzaer i historik (plus dagens afsluttede ordre)
                    mestPopulaerePizzaer(ordreHistorikListe);

                    System.out.println("--------------------------------------------------------------------------");

                    //Toem listen da ikke brug for at have hele historik i memory
                    ordreHistorikListe.clear();

                    break;

                case 7:
                    //7. Søg efter ordre
                    Ordre.visAktiveOrdre(aktiveOrdre);
                    break;

                case 8:
                    //8. Udlever/annuller ordre
                    Ordre.udleverOrdre(aktiveOrdre, dagensAfsluttedeOrdre);
                    break;

                case 10:
                    //10. Vis Mario liste og tlf numre paa ordre som er klar til udlevering

                    opdaterMarioListe(aktiveOrdre, marioPizzaListe, true);
                    break;
                case 11:
                    //11. Marker pizza på Marios liste som faerdig
                    markerPizzaSomFaerdig(aktiveOrdre, marioPizzaListe, scanner);
                    break;
                case 12:
                    //12. Gem aktive ordre til egen backup fil
                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                        }

                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }
                    break;
                case 13:
                    //13. Hent aktive ordre fra egen backup fil
                    try {
                        //Hent alle ordre objekter fra saeerlige fil til aktive ordre liste ved hjaelp af method i instance af FileIO class
                        aktiveOrdre = myFileHandler.readAktiveOrdre();
                    } catch (NullPointerException e) {
                        System.err.println("Der er ingen fil at læse");
                    }

//                    //temp loop til at goere inlaese aktive ordre til afsluttede for at lave dummies
//                    for (int i = 0; i < aktiveOrdre.size(); i++) {
//                        aktiveOrdre.get(i).setErAfsluttet(true);
//                        dagensAfsluttedeOrdre.add(aktiveOrdre.get(i));
//
//                    }


                    break;

                case 14:
                    //14. Gem dagens afsluttede ordre til egen backup fil


                    if (!aktiveOrdre.isEmpty()) {


                        for (Ordre ordre : aktiveOrdre) {

                            if (myFileHandler != null) {
                                myFileHandler.writeDagensAfsluttedeOrdre(aktiveOrdre);
                            } else {
                                System.err.println("Filehandler error");
                            }
                        }


                    } else {
                        System.err.println("Ingen ordre at gemme til fil");
                    }


        //Hent dagens afsluttede ordre fra egen fil til hvis program har vaeret afsluttet uventet
        if (myFileHandler != null) {
            dagensAfsluttedeOrdre = myFileHandler.readDagensAfsluttedeOrdre();
        } else {
            System.err.println("Filehandler error");
        }
                    //Hent alle ordre objekter fra saeerlige fil til dagens afsluttede ordre liste ved hjaelp af method i instance af FileIO class
                    if (myFileHandler != null) {
                        dagensAfsluttedeOrdre = myFileHandler.readDagensAfsluttedeOrdre();
                    } else {
                        System.err.println("Filehandler error");
                    }




        //Start hovedmenu som ogsaa tager imod valg
        menuObject.level1(menuPizzaUdvalg, marioPizzaListe, aktiveOrdre, dagensAfsluttedeOrdre, ordreHistorikListe, myFileHandler);



        //Gem dagens aktive ordre i egen fil saa tilgaengelige naar starte program igen
        if (!aktiveOrdre.isEmpty()) {
            if (myFileHandler != null) {

                for (Ordre ordre : aktiveOrdre) {

                    myFileHandler.writeActiveOrderArrayToFile(aktiveOrdre);
                }
            } else {
                System.err.println("Filehandler error");
            }
        }

        //Gem dagens afsluttede ordre i egen fil saa tilgaengelige naar starte program igen
        if (!dagensAfsluttedeOrdre.isEmpty()) {
            if (myFileHandler != null) {

                for (Ordre ordre : dagensAfsluttedeOrdre) {


                    myFileHandler.writeDagensAfsluttedeOrdre(dagensAfsluttedeOrdre);

                }

            } else {
                System.err.println("Filehandler error");
            }

        }

        //Luk scanner foer program slutter
        scanner.close();




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
    public static void opdaterMarioListe(ArrayList<Ordre> aktiveOrdre, ArrayList<Pizza> marioPizzaListe, boolean visListe, boolean visTlf) {

        //Forbered StringBuilder til at tilfoeje tlf numre paa ordre som er faerdige
        StringBuilder ordrerSomErFaerdige = new StringBuilder();

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

        if (visTlf && !ordrerSomErFaerdige.toString().isEmpty()) {
            System.out.printf("\\nTlf nummer på ordre som er klar til udlevering: %s\n\n", ordrerSomErFaerdige.toString());


        }
    }

    public static void markerPizzaSomFaerdig(ArrayList<Ordre> aktiveOrdre, ArrayList<Pizza> marioPizzaListe, Scanner scanner) {

        //Variabel til modtage valg fra bruger
        int tempValg;

        //Opdater Mario pizza liste
        opdaterMarioListe(aktiveOrdre, marioPizzaListe, false, false);

        //Stop method hvis Marios liste er tom og der derfor ikke er noget at fjerne
        if (marioPizzaListe.isEmpty()) {
            System.out.println("Alle pizzaer er allerede klar\n");
            return;
        }

        //Udskriv pizzaer fra Marios pizza liste med id til at bruger kan vaelge en
        for (int i = 0; i < marioPizzaListe.size(); i++) {
            System.out.printf("[%2d] %s\n", i + 1, marioPizzaListe.get(i).toString());

        }

        System.out.println("Enter [nummer] på pizza som du vil markere som klar (0 for exit)");

        //brug helper method til at modtage valg fra bruger ud fra id 0 til antal pizzaer i listen. 0 er til exit.
        tempValg = HelpMethods.getValgInt(0, marioPizzaListe.size(), false, scanner);

        //Exit ved 0 indtastning
        if (tempValg == 0) {
            return;
        }

        //Aendre status paa pizza som faerdig
        //Her bruges Marios liste som reference saa det ikke er noedvendigt at finde relevante ordre
        //Index justeret for at id 1 er pizza index 0
        marioPizzaListe.get(tempValg - 1).setPizzaFaerdig(true);

        //Opdater Mario pizza liste
        opdaterMarioListe(aktiveOrdre, marioPizzaListe, false, false);

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
            System.err.println("Ingen pizza objekter i denne liste");
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

    //Method der deler omsaetningen paa ordrer i liste op i datoer med ugedag
    //Datoer sorteres med seneste foerst
    public static void historikOmsaetningDatoer(ArrayList<Ordre> ordreHistorikListe) {

        //Variabel til at modtage en ordres afhentningstidspunkt med dato og klokkeslet
        LocalDateTime ordreAfhentningDatoOgTidspunkt;

        //Variabel til at modtage konvertering af afhentningstidspunkt som dato uden klokkeslet
        //Hvis bruge LocalDateTime som Key ville alle ordre faa deres egen Key
        //Maalet er at alle ordre paa samme dato faar samme Key
        LocalDate ordreAfhentningDato;

        //Tjek om der overhovedet er ordre i listen
        if (ordreHistorikListe.isEmpty()) {
            System.err.println("Ingen pizza objekter i denne liste");
            return;
        }

        //Create HashMap der har LocalDate som Key og double som value til sum af omsaetning paa datoens ordre
        Map<LocalDate, Double> historikOmsaetningDatoMap = new HashMap<LocalDate, Double>();


        //Gaa samtlige ordrer igennem
        for (Ordre ordre : ordreHistorikListe) {

            //Hent ordre afhentningstidspunkt med dato og klokkeslet
            ordreAfhentningDatoOgTidspunkt = ordre.getTidspunktForAfhentning();

            //Traek ren dato ud og til sammenligning
            ordreAfhentningDato = ordreAfhentningDatoOgTidspunkt.toLocalDate();

            //Tjek om denne dato allerede er som key i map
            if (historikOmsaetningDatoMap.containsKey(ordreAfhentningDato)) {
                //Hent gamle value og genskriv key med gamle value + denne ordres pris
                historikOmsaetningDatoMap.put(ordreAfhentningDato,
                        (historikOmsaetningDatoMap.get(ordreAfhentningDato) + ordre.getPrisTotalOrdre()));
            } else {
                //Ellers opret denne dato som key med denne ordres pris som value
                historikOmsaetningDatoMap.put(ordreAfhentningDato, ordre.getPrisTotalOrdre());
            }

        }

        //Create ArrayList med pairs fra HashMap som bestaar af dato og tilhoerende double som er datoens omsaetning
        //Det goer det muligt at sortere pairs
        ArrayList<Map.Entry<LocalDate, Double>> datoFrequency = new ArrayList<Map.Entry<LocalDate, Double>>(historikOmsaetningDatoMap.entrySet());

        //Sorter ArrayList med Comparator som bruger .getKey() til at hente datoen fra pairs
        //Datoer sorteres med seneste dato foerst
        Collections.sort(datoFrequency, new ComparatorDatoOmsaetning());

        //Overskrift
        System.out.println("\nUgedag     Dato               Datoens totale omsætning");

        //Print ugedag, dato og total omsaetning for datoen
        //.getDayOfWeek() bruges til at finde ugedag paa datoen i pair som hentes med .getKey()
        //Denne ugedag oversaettes fra engelsk til dansk med helper method
        //Datoen fra pair formateres med helper method
        //Totalomsaetningen er fra Value i pair som hentes med .getValue()
        for (int i = 0; i < datoFrequency.size(); i++) {
            System.out.printf("%-10s %10s     %10.2f kr.\n", HelpMethods.translateUgedag(String.valueOf(datoFrequency.get(i).getKey().getDayOfWeek())), HelpMethods.formatDate(datoFrequency.get(i).getKey()), datoFrequency.get(i).getValue());
        }

        System.out.println();

    }
}

