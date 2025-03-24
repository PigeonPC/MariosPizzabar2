import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpMethods {

    static DateTimeFormatter formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
    static DateTimeFormatter formatDKKlokken = DateTimeFormatter.ofPattern("HH:mm");

    public static String getValidatedTlfNummer (Scanner in) {


        Pattern pattern = Pattern.compile("^\\d{8}$");

        Matcher matcher;
        boolean isValidated = false;
        String numberString;

        do {
            numberString = in.nextLine();

            matcher = pattern.matcher(numberString);


            if (!matcher.matches()) {
                System.err.println("Det er ikke et dansk telefonnummer. Prøv igen.");
            } else {
                isValidated = true;
            }

        } while (!isValidated);

        return numberString;
    }

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
            } else if (tempInput.matches("-?\\d+")) {
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

    public static int getInt(boolean useEmpty, Scanner in) {


        String tempInput;

        while (true) {
            tempInput = in.nextLine();

            if (tempInput.isEmpty() && useEmpty) {
                return Integer.MIN_VALUE;
            } else if (tempInput.matches("-?\\d+")) {
                return Integer.parseInt(tempInput);
            }

            System.err.println("Det er ikke et tal du har indtastet. Prøv igen");

        }

    }

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


    public static int[][] getArrayValgPizzaSomFindes(ArrayList<Pizza> pizzaMenuUdvalg, Scanner in) {
        String[] pizzaValgTokenz;
        int[][] arrayPizzaValg;
        String[] tempArrayForMulti;
        boolean flagSyntaxOk;
        boolean flagPizzaFindes;

        do {
            flagSyntaxOk = true;
            flagPizzaFindes = true;

            String tempString = in.nextLine();
            pizzaValgTokenz = tempString.split("\\s+");

            arrayPizzaValg = new int[pizzaValgTokenz.length][2];

            for (int i = 0; i < pizzaValgTokenz.length; i++) {
                if (pizzaValgTokenz[i].matches("\\d+")) {
                    arrayPizzaValg[i][0] = Integer.parseInt(pizzaValgTokenz[i]);
                    arrayPizzaValg[i][1] = 1;
                } else if (pizzaValgTokenz[i].matches("(\\d+)\\*(\\d+)")) {
                    tempArrayForMulti = pizzaValgTokenz[i].split("\\*");
                    arrayPizzaValg[i][0] = Integer.parseInt(tempArrayForMulti[1]);
                    arrayPizzaValg[i][1] = Integer.parseInt(tempArrayForMulti[0]);

                } else {
                    flagSyntaxOk = false;
                }

                if (arrayPizzaValg[i][0] > pizzaMenuUdvalg.size() || arrayPizzaValg[i][0] < 1) {
                    flagPizzaFindes = false;
                } else {

                    if (pizzaMenuUdvalg.get(arrayPizzaValg[i][0]) != null) {
                        //flagPizzaFindes = true;
                    } else {
                        flagPizzaFindes = false;
                    }
                }




            }

            if (!flagSyntaxOk) {
                System.err.println("Der er en syntax fejl i indtastningen");
            } else if (!flagPizzaFindes) {
                System.err.println("Du har indtastet et forkert pizza nummer");
            }

        } while (!flagPizzaFindes || !flagSyntaxOk);




        return arrayPizzaValg;


    }

    public static LocalDateTime getValidatedTidspunktSenere(boolean askDiff, Scanner scanner) {

        String userInput;
        String[] tempArrayHM;
        boolean timeFormatValid;
        boolean timeIsLater;
        boolean timeDiffOk;
        LocalDateTime basisTime;


        timeDiffOk = true;

        basisTime = LocalDateTime.now();

        do {

            System.out.println("Indtast tidspunkt for afhentning (HH:MM format):");

            timeIsLater = false;

            while (!timeIsLater) {


                timeFormatValid = false;

                while (!timeFormatValid) {

                    userInput = scanner.nextLine();

                    if (userInput.matches("^([0-1][0-9]|2[0-3]|[0-9]):[0-5][0-9]")) {
                        tempArrayHM = userInput.split(":");
                        basisTime = basisTime.withHour(Integer.parseInt(tempArrayHM[0]));
                        basisTime = basisTime.withMinute(Integer.parseInt(tempArrayHM[1]));
                        timeFormatValid = true;

                        if (!basisTime.isAfter(LocalDateTime.now())) {
                            System.err.println("Klokken er allerede mere end det indtastede");
                        } else {
                            timeIsLater = true;
                        }

                    } else {
                        System.err.println("Syntax fejl i indtastede tidspunkt. Prøv igen");

                    }
                }


            }

            if (askDiff) {
                Duration d = Duration.between(LocalDateTime.now(), basisTime);
                long timer = d.toHours();
                long minutes = d.toMinutes();
                System.out.printf("Det er om %2d:%02d\nPasser det? (1)Ja 2(Nej)\n", timer, minutes % 60);

                if (getValgInt(1, 2, false, scanner) == 2) {
                    timeDiffOk = false;
                } else {
                    timeDiffOk = true;
                }

            }
        } while (!timeDiffOk);

        return basisTime;
    }

    public static String getValidatedTlfDK(boolean useEmpty, Scanner scanner) {
        boolean isValid = false;

        String userInput;

        do {

            userInput = scanner.nextLine();

            if (userInput.isEmpty() && useEmpty) {
                isValid = true;
            }

            if (!userInput.matches("[2-9]\\d{7}") && !userInput.isEmpty()) {
                System.err.println("Det indtastede er ikke et dansk tlf nummer. Prøv igen");
            } else {
                isValid = true;
            }

        } while (!isValid);

        return userInput;
    }

    public static String formatDK(LocalDateTime time) {
        return time.format(formatDK);
    }

    public static String formatformatDKKlokken(LocalDateTime time) {
        return time.format(formatDKKlokken);
    }



}
