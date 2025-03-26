import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Samling af helper methods til validering mm som muligvis kan genbruges andre steder i projektet
public class HelpMethods {

    //DateTimeFormatter i to forskellige formater som senere bruges til at konvertere LocalDateTime til passende String
    static DateTimeFormatter formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
    static DateTimeFormatter formatDKKlokken = DateTimeFormatter.ofPattern("HH:mm");


    //Helper method til at modtage text input fra bruger med max laengde
    //Hvis useEmpty er true saa ogsaa tilladt med tom String (Enter)
    public static String getValidatedTekstLaengde(int max, boolean useEmpty, Scanner in) {
        String tempString;
        boolean isValidated = false;

        do {
            tempString = in.nextLine();
            if (tempString.length() > max) {
                System.err.printf("Din tekst er for lang.\nDen må max være %d karakterer.\nPrøv igen\n", max);
            } else {
                if (tempString.isEmpty() && !useEmpty) {
                    System.err.println("Du indtastede ingenting");
                } else {
                    isValidated = true;
                }
            }

        } while (!isValidated);
        return tempString;
    }

    //Helper method til modtage int i range min max fra bruger
    //Hvis useEmpty er true saa er tomt input (Enter) ogsaa tilladt. Dette returnerer Integer.MIN_VALUE
    public static int getValgInt(int min, int max, boolean useEmpty, Scanner in) {

        String tempInput;
        int number;

        while (true) {
            tempInput = in.nextLine();

            if (tempInput.isEmpty()) {
                if (useEmpty) {
                    return Integer.MIN_VALUE;
                } else {
                    System.err.println("Du bliver nødt til at taste noget her");
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

    //Helper method til at modtage int fra bruger
    //Hvis useEmpty er true saa er tomt input (Enter) ogsaa tilladt. Dette returnerer Integer.MIN_VALUE
    public static int getInt(boolean useEmpty, Scanner in) {


        String tempInput;

        while (true) {
            tempInput = in.nextLine();

            if (tempInput.isEmpty() && useEmpty) {
                return Integer.MIN_VALUE;
                //Test om String bestaar af en raekke cifre
            } else if (tempInput.matches("-?\\d+")) {
                //Omdan cifrene i String til en int
                return Integer.parseInt(tempInput);
            }

            System.err.println("Det er ikke et tal du har indtastet. Prøv igen");

        }

    }

    //Helper method til at vaelge en pizza som findes
    //Rest fra da vi opererede med pizzamenu med faste pladser ud fra pizza nr og null paa uudfyldte pladser
    public static int getValgPizzaSomFindes(ArrayList<Pizza> pizzaMenuUdvalg, Scanner in) {
        boolean isValidated = false;
        int number;

        do {
            while (!in.hasNextInt()) {
                in.next();
                System.err.println("Det er ikke et tal du har indtastet. Prøv igen");
            }

            number = in.nextInt();

            if (number > pizzaMenuUdvalg.size() || number < 1) {
                System.err.println("Vi har ikke nogen pizza med det nummer. Prøv igen.");
            } else {

                if (pizzaMenuUdvalg.get(number) != null) {
                    isValidated = true;
                } else {
                    System.err.println("Vi har ikke nogen pizza med det nummer. Prøv igen.");
                }
            }
        } while (!isValidated);

        in.nextLine();
        return number;
    }

    //Helper method til at lade bruger indtaste flere pizzanumre i samme input
    //* modtages ogsaa til fx 3*6 (3 stk af pizza nr 6)
    //Der kan vaere flere mellemrum mellem pizzanumre
    //Returnerer et two dimensional array hvor
    public static int[][] getArrayValgPizzaSomFindes(ArrayList<Pizza> pizzaMenuUdvalg, Scanner in) {
        String[] pizzaValgTokenz;
        //two dimensional array
        int[][] arrayPizzaValg;
        String[] tempArrayForMulti;
        boolean flagSyntaxOk;
        boolean flagPizzaFindes;

        do {
            flagSyntaxOk = true;
            flagPizzaFindes = true;

            String tempString = in.nextLine();

            //Split String input ud fra smaa og store mellemrum mellem pizzanumre. Saet ind i String array
            pizzaValgTokenz = tempString.split("\\s+");

            //Create two dimensional array af int med samme laengde som String array
            //Paa denne maade staar pizza nummer og antal af denne pizza med samme index   dvs int[pizzanr][antal]
            arrayPizzaValg = new int[pizzaValgTokenz.length][2];

            //Gennemgaa alle bidder fra input
            for (int i = 0; i < pizzaValgTokenz.length; i++) {
                //Bestaar bid af et antal cifre?
                if (pizzaValgTokenz[i].matches("\\d+")) {
                    //Saa omdan cifre til int og saet dem ind som pizza nr
                    arrayPizzaValg[i][0] = Integer.parseInt(pizzaValgTokenz[i]);
                    //Saet antal som 1 stk da der ikke forekom * men kun cifre
                    arrayPizzaValg[i][1] = 1;

                //Bestaar bid af et antal cifre paa hver side af et * tegn ?
                } else if (pizzaValgTokenz[i].matches("(\\d+)\\*(\\d+)")) {
                    //split ud fra * tegn og saet cifre paa hver side ind i et String array
                    tempArrayForMulti = pizzaValgTokenz[i].split("\\*");
                    //omdan cifre der var i String efter * til int og saet ind som pizza nr
                    arrayPizzaValg[i][0] = Integer.parseInt(tempArrayForMulti[1]);
                    //omdan cifre der var i String foer * til int og saet ind som antal af denne pizza
                    arrayPizzaValg[i][1] = Integer.parseInt(tempArrayForMulti[0]);

                //Hvis ingen af ovenstaaende matcher saa er der fejl i indtastningen
                } else {
                    flagSyntaxOk = false;
                }

                //Tjek om pizzanummer gaar ud over pizza menuens range af numre
                if (arrayPizzaValg[i][0] > pizzaMenuUdvalg.size() || arrayPizzaValg[i][0] < 1) {
                    flagPizzaFindes = false;
                } else {
                    //Tjek om plads i pizza menu er null. Rest fra da vi havde faste pladser i pizzamenu med null mellem
                    if (pizzaMenuUdvalg.get(arrayPizzaValg[i][0]-1) != null) {

                    } else {
                        flagPizzaFindes = false;
                    }
                }




            }

            //Giv brugeren instruks om fejl ud fra flags foer indtaste igen
            if (!flagSyntaxOk) {
                System.err.println("Der er en syntax fejl i indtastningen");
            } else if (!flagPizzaFindes) {
                System.err.println("Du har indtastet et forkert pizza nummer");
            }
        //Gentag indtastningsproces hvis fejl i syntax af input eller pizza nr ikke findes
        } while (!flagPizzaFindes || !flagSyntaxOk);



        //Returner two dimensional int array som indeholder pizza nr og antal paa hvert index i hele sin laengde
        return arrayPizzaValg;


    }

    //Helper method til at modtage tidspunkt fra bruger indtastet som text String
    //Tjekker at tidspunkt er senere end nu, men det kan kun vaere i dag
    //Hvis askDiff er true, saa vises tidsrummet op til indtastede tidspunkt og spoerges om bekraeftelse
    public static LocalDateTime getValidatedTidspunktSenere(boolean askDiff, Scanner scanner) {

        String userInput;
        String[] tempArrayHM;
        boolean timeFormatValid;
        boolean timeIsLater;
        boolean timeDiffOk;
        LocalDateTime basisTime;


        timeDiffOk = true;

        //Hent nuvaerende tidspunkt og nuvaerende dato
        basisTime = LocalDateTime.now();

        do {

            System.out.println("Indtast tidspunkt for afhentning (HH:MM format):");

            timeIsLater = false;

            while (!timeIsLater) {


                timeFormatValid = false;

                while (!timeFormatValid) {

                    userInput = scanner.nextLine();

                    //Tjek indtastede String med Regular expression
                    //Der skal vaere : i midten
                    //Paa venstre side foelgende tilladt
                    //  0 eller 1 efterfulgt af 0-9
                    //  2 efterfulgt af 0-3
                    //  0-9
                    //Paa hoejre side er foelgende tilladt
                    //  0-5 efterfulgt af 0-9
                    if (userInput.matches("^([0-1][0-9]|2[0-3]|[0-9]):[0-5][0-9]")) {
                        //Split String ud fra : over i String array
                        tempArrayHM = userInput.split(":");
                        //Tag det gemte nuvaerende tidspunkt og dato og udskift Hour med cifre paa venstre side omdannet til int
                        basisTime = basisTime.withHour(Integer.parseInt(tempArrayHM[0]));
                        //Tag det gemte nuvaerende tidspunkt og dato og udskift Minute med cifre paa venstre side omdannet til int
                        basisTime = basisTime.withMinute(Integer.parseInt(tempArrayHM[1]));
                        timeFormatValid = true;

                        //Sammenlign den nye tid med nuvaerende tid og tjek om den er senere
                        if (!basisTime.isAfter(LocalDateTime.now())) {
                            System.err.println("Klokken er allerede mere end det indtastede");
                        } else {
                            timeIsLater = true;
                        }
                    //Hvis Regular expression ikke matcher saa er der fejl i input
                    } else {
                        System.err.println("Syntax fejl i indtastede tidspunkt. Prøv igen");

                    }
                }


            }
            //Tjek om parameter er true saa tidsrum til nye tid skal vises og bekraeftes
            if (askDiff) {

                //Brug Duration object til at beregne tidsforskel mellem nu og indtastede tid
                Duration d = Duration.between(LocalDateTime.now(), basisTime);
                //Get antal timer i tidsforskel
                long timer = d.toHours();
                //Get total antal minutter i tidsforskel
                long minutes = d.toMinutes();
                //Vis tidsforskel ved at bruge modulus 60 paa minutter saa kun vise de minutter som ikke er daekket af antal timer
                System.out.printf("Det er om %2d:%02d\nPasser det? (1)Ja 2(Nej)\n", timer, minutes % 60);

                //Brug helper method til at modtage valg fra bruger. Om tidsforskellen er acceptabel
                if (getValgInt(1, 2, false, scanner) == 2) {
                    timeDiffOk = false;
                } else {
                    timeDiffOk = true;
                }

            }
        } while (!timeDiffOk);

        //returner den indtastede tid i LocalDateTime format
        return basisTime;
    }

    //Helper method til at modtage DK telefonnummer fra bruger
    //Validated med Regular expression
    //Hvis useEmpty er true saa er tryk paa Enter ogsaa en mulighed for input
    public static String getValidatedTlfDK(boolean useEmpty, Scanner scanner) {

        boolean isValid = false;
        String userInput;

        do {

            userInput = scanner.nextLine();

            //Tjek for om bruger har indtastet tom String (Enter) og om parameter tillader dette som input
            if (userInput.isEmpty() && useEmpty) {
                //Hvis ja, saa angiv at brugbart input er opnaaet
                isValid = true;
            }

            //Tjek om input bestaar af 8 cifre hvor det foerste er min 2. Hvis ikke, og hvis String ikke er tom, saa fejl
            if (!userInput.matches("[2-9]\\d{7}") && !userInput.isEmpty()) {
                System.err.println("Det indtastede er ikke et dansk tlf nummer. Prøv igen");
            } else {
                //ellers brugbart input
                isValid = true;
            }

        } while (!isValid);

        return userInput;
    }


    //Helper method til at formatere LocalDateTime til fx 240325 15:14
    public static String formatDK(LocalDateTime time) {
        return time.format(formatDK);
    }

    //Helper method til at formatere LocalDateTime til fx 15:14
    public static String formatformatDKKlokken(LocalDateTime time) {
        return time.format(formatDKKlokken);
    }



}
