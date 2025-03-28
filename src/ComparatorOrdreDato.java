import java.util.Comparator;

public class ComparatorOrdreDato implements Comparator<Ordre> {

    public int compare(Ordre ordre1, Ordre ordre2) {
        return ordre1.getTidspunktForOrdre().compareTo(ordre2.getTidspunktForOrdre());

    }
}
