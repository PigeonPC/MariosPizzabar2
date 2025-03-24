import java.util.ArrayList;

public class Pizza {

    //Declaring variables in Pizza
    int nummer;
    String navn;
    int pris;
    String[] ingredienser;
    String kommentar;
    boolean pizzaFaerdig;

    //Adding constructor for pizza
    public Pizza(int nummer, String navn, int pris, String[] ingredienser, String comment) {

        this.nummer = nummer;
        this.navn = navn;
        this.pris = pris;
        this.ingredienser = ingredienser;
        this.kommentar = comment;
        this.pizzaFaerdig = false;

    }

    public Pizza(Pizza that) {

        this.nummer = that.nummer;
        this.navn = that.navn;
        this.pris = that.pris;
        this.ingredienser = new String[that.ingredienser.length];
        for (int i = 0; i < that.ingredienser.length; i++) {
            this.ingredienser[i] = (that.ingredienser[i]);
        }
        this.kommentar = that.kommentar;
        this.pizzaFaerdig = false;



    }

    //Getter and setters for all variables in Pizza
    public boolean getPizzaFaerdig() {
        return this.pizzaFaerdig;
    }

    public void setPizzaFaerdig(boolean pizzaFaerdig) {
        this.pizzaFaerdig = pizzaFaerdig;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getPris() {
        return pris;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }

    public String[] getIngredient() {
        return ingredienser;
    }

    public void setIngredient(String[] ingredienser) {
        this.ingredienser = ingredienser;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Pizza copyOf() {
        return new Pizza(this);

    }

    //toString
    @Override
    public String toString() {

        //Using a string builder with the name string to print the array with the toString().
        StringBuilder string = new StringBuilder();

        //Adding number and name to string
        string.append(nummer + ": " + navn);

        //Adding a gap between number and name, and the ingredients.
        while(0 < 32 - string.length()) {
            string.append(" ");
        }

        //Adding all ingredients to the String builder
        for (int i = 0; i < ingredienser.length; i++) {
            string.append(ingredienser[i]);
            if (i < ingredienser.length - 2) {
                string.append(", ");
            }
            else if (i < ingredienser.length-1) {
                string.append(" og ");
            }
        }

        //Adding dots to make a gap between ingredients and price.
        while (125 - string.length() > 0) {
            string.append(".");
        }
        if(pris<100) {
            string.append(".");
        }

        //Adding price and currency
        string.append(pris + " kr.");

        //Adding Klar eller ingenting alt efter om pizza er bagt faerdig
        string.append((pizzaFaerdig) ? "Klar" : "");

        //If the comment is equal to null, it won't be added to the String Builder. If it isn't, it will be added underneath the information of the pizza object.
        if (!kommentar.equals("null")) {
            string.append("\nKommentarer: " + kommentar);
        }

        //Furthermore, the comment won't be initialized until the order is put down.

        //Returning the String Builder

        return string.toString();
    }

}
