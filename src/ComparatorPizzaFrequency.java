import java.util.*;

//Comparator til at sammenligne frekvenserne af pizzaer i pairs taget fra HashMap
//.getValue() bruges til at hente value delen af pair af Key og Value dvs frekvensen af et pizzanavn
//freq2 kommer foer freq1 i compare for at sortere med hoejeste frekvens foerst
public class ComparatorPizzaFrequency implements Comparator<Map.Entry<String, Integer>> {

    public int compare(Map.Entry<String, Integer> freq1, Map.Entry<String, Integer> freq2) {
        return freq2.getValue().compareTo(freq1.getValue());

    }

}
