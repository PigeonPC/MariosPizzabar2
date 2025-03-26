import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;




public class ComparatorDatoFrequency implements Comparator<Map.Entry<LocalDateTime, Double>> {

    public int compare(Map.Entry<LocalDateTime, Double> freq1, Map.Entry<LocalDateTime, Double> freq2) {
        return freq2.getValue().compareTo(freq1.getValue());

    }

}