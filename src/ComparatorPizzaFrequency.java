import java.util.*;

//Comparator til at sammenligne frekvenserne af pizzaer i pairs taget fra HashMap
//.getValue() bruges til at hente value delen af pair af Key og Value dvs frekvensen af et pizzanavn
//freq2 kommer foer freq1 i compare for at sortere med hoejeste frekvens foerst
//Hvis frekvensen er ens saa sorteres efter pizzanavn i Key, saa pizzaer med ens salg sorteres alfabetisk
public class ComparatorPizzaFrequency implements Comparator<Map.Entry<String, Integer>> {

    public int compare(Map.Entry<String, Integer> freq1, Map.Entry<String, Integer> freq2) {
        if (freq2.getValue().equals(freq1.getValue())) {
            return freq1.getKey().compareTo(freq2.getKey());
        }
        return freq2.getValue().compareTo(freq1.getValue());

    }

}
