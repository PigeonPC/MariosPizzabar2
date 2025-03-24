
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;

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
    public Ordre(LocalDateTime tidspunktForAfhentning, boolean erAfsluttet, String kommentar, double prisTotalOrdre, String tlfNummerKunde){
        this.tidspunktForOrdre = LocalDateTime.now();
        this.tidspunktForAfhentning = tidspunktForAfhentning;
        //this.formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
        this.ordrePizzaListe = new ArrayList<>();
        this.erAfsluttet = erAfsluttet;
        this.kommentar = kommentar;
        this.prisTotalOrdre = prisTotalOrdre;
        this.tlfNummerKunde = tlfNummerKunde;
    }

    public Ordre() {
        this.tidspunktForOrdre = LocalDateTime.now();
        this.tidspunktForAfhentning = null;
        //this.formatDK = DateTimeFormatter.ofPattern("ddMMyy HH:mm");
        this.ordrePizzaListe = new ArrayList<>();
        this.erAfsluttet = false;
        this.kommentar = "Ingen kommentarer";
        this.prisTotalOrdre = 0.0;
        this.tlfNummerKunde = "Endnu ikke opgivet";;
    }

    //GETTERS
    public LocalDateTime getTidspunktForOrdre(){
        return this.tidspunktForOrdre;
    }
    public LocalDateTime getTidspunktForAfhentning(){
        return this.tidspunktForAfhentning;
    }
    public DateTimeFormatter getFormatDK(){
        return this.formatDK;
    }
    public boolean getErAfsluttet(){
        return this.erAfsluttet;
    }
    public String getKommentar(){
        return this.kommentar;
    }
    public double getPrisTotalOrdre(){
        return this.prisTotalOrdre;
    }
    public String getTlfNummerKunde(){
        return this.tlfNummerKunde;
    }

    //SETTERS
    public void setTidspunktForOrdre(LocalDateTime tidspunktForOrdre){
        this.tidspunktForOrdre = tidspunktForOrdre;
    }
    public void setTidspunktForAfhentning(LocalDateTime tidspunktForAfhentning){
        this.tidspunktForAfhentning = tidspunktForAfhentning;
    }
    public void setFormatDK(DateTimeFormatter formatDK){
        this.formatDK = formatDK;
    }
    public void setErAfsluttet(boolean erAfsluttet){
        this.erAfsluttet = erAfsluttet;
    }
    public void setKommentar(String kommentar){
        this.kommentar = kommentar;
    }
    public void setPrisTotalOrdre(double prisTotalOrdre){
        this.prisTotalOrdre = prisTotalOrdre;
    }

    public void setTlfNummerKunde(String tlfNummerKunde){
        this.tlfNummerKunde = tlfNummerKunde;
    }

    public ArrayList<Pizza> getOrdrePizzaListe() {
        return ordrePizzaListe;
    }

    public void tilfoejPizzaTilOrdre(Pizza pizza) {
        ordrePizzaListe.add(pizza);
        opdaterTotalPrisOrdre();

    }

    public void toemOrdrePizzaListe() {
        ordrePizzaListe.clear();
    }

    public void sorterOrdrePizzaListe() {
        ordrePizzaListe.sort(Comparator.comparingInt(Pizza::getNummerPizza));
    }


    //TO STRING METODE
    public String toString() {
        StringBuilder tempString = new StringBuilder();
        tempString.append(String.format("\n%-35s %s", "Tidspunkt for ordrebestilling:", HelpMethods.formatDK(tidspunktForOrdre)));

        tempString.append(String.format("\n%-35s %s", "Afhentningstidspunkt:", (tidspunktForAfhentning != null) ? HelpMethods.formatDK(tidspunktForAfhentning) : "Endnu ikke fastsat"));

        tempString.append(String.format("\n%-35s %s", "Status for ordre:", (!erAfsluttet) ? "Igangværende" : "Afsluttet"));

        tempString.append(String.format("\n%-35s %s", "Kommentar:", kommentar));

        tempString.append(String.format("\n%-35s %s", "Ordre pris:", prisTotalOrdre));

        tempString.append(String.format("\n%-35s %s", "Kunde telefonnummer:", tlfNummerKunde));

        tempString.append("\n\n");

        tempString.append("Nr. Pizza              Ingredienser                                               Pris      Status       Kommentar\n");

        for (int i = 0; i < ordrePizzaListe.size(); i++) {
            tempString.append(ordrePizzaListe.get(i).toString()).append("\n");
            //tempString.append("\n");
        }

        tempString.append("\n---------------------------------------------------------------------------------------------------------------------------------------------\n");

        return tempString.toString();
    }

    public void opdaterTotalPrisOrdre() {
        prisTotalOrdre = 0;
        for (Pizza pizza : ordrePizzaListe) {
            prisTotalOrdre += pizza.getPrice();
        }
    }



    public void udskrivOrdrePizzaListeMedId() {
        for (int i = 0; i < ordrePizzaListe.size(); i++) {
            System.out.printf("[%2d] %s\n", i + 1, ordrePizzaListe.get(i).toString());

        }
    }

}







