
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;


// class til ordre
// implements Serializable for at gøre det muligt at streame indhold til fil med ObjectOutputStream
public class Ordre implements Serializable {

    //VARIABLER
    private LocalDateTime tidspunktForOrdre;
    private LocalDateTime tidspunktForAfhentning;
    private DateTimeFormatter formatDK;
    ArrayList<Pizza> ordrePizzaListe;
    private boolean erAfsluttet;
    private String kommentar;
    private double prisTotalOrdre;
    private String tlfNummerKunde;

    //ORDRE CONSTRUCTOR
    //Nok ikke i brug. Parametre, men tidspunkt for ordre sat til tidspunkt for creation
    public Ordre(LocalDateTime tidspunktForAfhentning, boolean erAfsluttet, String kommentar, double prisTotalOrdre, String tlfNummerKunde) {
        this.tidspunktForOrdre = LocalDateTime.now();
        this.tidspunktForAfhentning = tidspunktForAfhentning;
        this.ordrePizzaListe = new ArrayList<>();
        this.erAfsluttet = erAfsluttet;
        this.kommentar = kommentar;
        this.prisTotalOrdre = prisTotalOrdre;
        this.tlfNummerKunde = tlfNummerKunde;
    }

    //ORDRE CONSTRUCTOR
    //Denne uden parametre
    public Ordre() {
        this.tidspunktForOrdre = LocalDateTime.now();
        this.tidspunktForAfhentning = null;
        this.ordrePizzaListe = new ArrayList<>();
        this.erAfsluttet = false;
        this.kommentar = "Ingen kommentarer";
        this.prisTotalOrdre = 0.0;
        this.tlfNummerKunde = "Endnu ikke opgivet";
    }

    //GETTERS
    public LocalDateTime getTidspunktForOrdre() {
        return this.tidspunktForOrdre;
    }

    public LocalDateTime getTidspunktForAfhentning() {
        return this.tidspunktForAfhentning;
    }

    public DateTimeFormatter getFormatDK() {
        return this.formatDK;
    }

    public boolean getErAfsluttet() {
        return this.erAfsluttet;
    }

    public String getKommentar() {
        return this.kommentar;
    }

    public double getPrisTotalOrdre() {
        return this.prisTotalOrdre;
    }

    public String getTlfNummerKunde() {
        return this.tlfNummerKunde;
    }

    public ArrayList<Pizza> getOrdrePizzaListe() {
        return ordrePizzaListe;
    }

    //SETTERS
    public void setTidspunktForOrdre(LocalDateTime tidspunktForOrdre) {
        this.tidspunktForOrdre = tidspunktForOrdre;
    }

    public void setTidspunktForAfhentning(LocalDateTime tidspunktForAfhentning) {
        this.tidspunktForAfhentning = tidspunktForAfhentning;
    }

    public void setFormatDK(DateTimeFormatter formatDK) {
        this.formatDK = formatDK;
    }

    public void setErAfsluttet(boolean erAfsluttet) {
        this.erAfsluttet = erAfsluttet;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public void setPrisTotalOrdre(double prisTotalOrdre) {
        this.prisTotalOrdre = prisTotalOrdre;
    }

    public void setTlfNummerKunde(String tlfNummerKunde) {
        this.tlfNummerKunde = tlfNummerKunde;
    }


    //Tilfoejer pizza i parameter til ordren og opdaterer ordrens pris
    public void tilfoejPizzaTilOrdre(Pizza pizza) {
        ordrePizzaListe.add(pizza);
        opdaterTotalPrisOrdre();

    }

    //Fjerner alle pizzaer fra ordren
    public void toemOrdrePizzaListe() {
        ordrePizzaListe.clear();
    }

    //Sorterer pizzaerne i ordren efter pizza nr
    public void sorterOrdrePizzaListe() {
        //Bruger Comparator og lambda expression til at sortere pizzaer i ordrens pizza liste efter pizza nr via getter
        ordrePizzaListe.sort(Comparator.comparingInt(Pizza::getNummer));
    }


    //TO STRING METODE
    //Denne skal nok tilpasses i layout efter at Ordre class og Pizza class er sat sammen
    public String toString() {

        //Stringbuilder for lettere at bygge en omfattende String op
        StringBuilder tempString = new StringBuilder();

        //formaterer LocalDateTime vaerdi til letlaeselig dato og tidspunkt for ordren via helper method i HelpMethods class
        tempString.append(String.format("\n%-35s %s", "Tidspunkt for ordrebestilling:", HelpMethods.formatDK(tidspunktForOrdre)));

        //tilfoejer enten afhentningstidspunkt formateret via helper method i HelpMethods eller "Endnu ikke fastsat" hvis tidspunktet er null
        tempString.append(String.format("\n%-35s %s", "Afhentningstidspunkt:", (tidspunktForAfhentning != null) ? HelpMethods.formatDK(tidspunktForAfhentning) : "Endnu ikke fastsat"));

        //tilfoejer enb af de to Strings alt efter om erAfslutter har vaerdien true eller false
        tempString.append(String.format("\n%-35s %s", "Status for ordre:", (!erAfsluttet) ? "Igangværende" : "Afsluttet"));

        tempString.append(String.format("\n%-35s %s", "Kommentar:", kommentar));

        tempString.append(String.format("\n%-35s %s", "Ordre pris:", prisTotalOrdre));

        tempString.append(String.format("\n%-35s %s", "Kunde telefonnummer:", tlfNummerKunde));

        //Tilfoejer et par tomme linjer foer ordrens pizzaliste
        tempString.append("\n\n");

        //Overskrift over ordrens pizzaer (skal nok tilpasses efter merge med Pizza class)
        tempString.append("Nr Pizza                        Ingredienser                                                                                   Pris   Status\n");

        //Gaar gennem ordrens pizzaliste og kalder toString for hver pizza og tilfoejer denne String sammen med linjeskift
        for (int i = 0; i < ordrePizzaListe.size(); i++) {
            tempString.append(ordrePizzaListe.get(i).toString()).append("\n");
        }
        //Linjeopdeler mellem ordre til overblik naar flere ordrer printes
        tempString.append("\n---------------------------------------------------------------------------------------------------------------------------------------------\n");

        //Her omdannes StringBuilder objektet til en String
        return tempString.toString();
    }

    //Ordrens totale pris beregnes og opdateres ved at hente prisen paa samtlige pizzaer i odrens pizzaliste
    public void opdaterTotalPrisOrdre() {
        prisTotalOrdre = 0;
        for (Pizza pizza : ordrePizzaListe) {
            prisTotalOrdre += pizza.getPris();
        }
    }


    //Printer ordrens pizzaliste tilfoejet et nr. ude til venstre fra 1 og opefter
    //Det er for at kunne vaelge en bestemt pizza i ordren ved indtastning
    public void udskrivOrdrePizzaListeMedId() {
        for (int i = 0; i < ordrePizzaListe.size(); i++) {
            System.out.printf("[%2d] %s\n", i + 1, ordrePizzaListe.get(i).toString());

        }
    }

    //Method til at tjekke om ordre har ufaerdige pizzaer
    public boolean ordreHarUfaerdigePizza() {

        boolean flagUfaerdigePizzaDenneOrdre = false;

        for (int i = 0; i < ordrePizzaListe.size(); i++) {
            if (ordrePizzaListe.get(i).getPizzaFaerdig() == false) {
                flagUfaerdigePizzaDenneOrdre = true;
            }
        }

        return flagUfaerdigePizzaDenneOrdre;
    }

    //Find aktive ordre og søg efter tlf nummer
    //Find aktive ordre:
    public static void visAktiveOrdre(ArrayList<Ordre> aktiveOrdre) {


        Scanner scanner = new Scanner(System.in);
        System.out.println("Indtast tlf nummer tilknyttet ordren:");

        String soegTlfNummer = scanner.nextLine();

        //ArrayList<Ordre> aktiveOrdre;

        for (int i = 0; i < aktiveOrdre.size(); i++) {
            if (aktiveOrdre.get(i).getTlfNummerKunde().matches(soegTlfNummer)) {
                System.out.println("Resultat fundet: " + aktiveOrdre.get(i).getTlfNummerKunde());
                System.out.println(aktiveOrdre.get(i));


            } else {
                System.out.println("Telefonnummeret findes ikke på listen");
            }

        }

    }

    //Udlever/annuller ordre

    public static void udleverOrdre(ArrayList<Ordre> aktiveOrdre, ArrayList<Ordre> dagensAfsluttedeOrdre) {

        Scanner scanner = new Scanner(System.in);
        String soegTlfNummer = scanner.nextLine();

        for (int i = 0; i < aktiveOrdre.size(); i++) {
            System.out.println("Indtast tlf nummer tilknyttet ordren:");

            if (aktiveOrdre.get(i).getTlfNummerKunde().matches(soegTlfNummer)) {
                System.out.println("Resultat fundet: " + aktiveOrdre.get(i).getTlfNummerKunde());
                System.out.println(aktiveOrdre.get(i));
                System.out.println("Bekræft betaling (1) ja, (2) nej: ");
                if (HelpMethods.getValgInt(1, 2, false, scanner)==1){
                    aktiveOrdre.get(i).setErAfsluttet(true);
                    dagensAfsluttedeOrdre.add(aktiveOrdre.remove(i));
                    System.out.println("Betaling gennemført, udlever ordre");
                } else {
                    System.out.println("Betaling afbrudt");
                    aktiveOrdre.remove(i);
                }

            } else {
                System.out.println("Telefonnummeret findes ikke på listen");
            }

        }

    }


}











