import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;



//Comparator til at sammenligne datoer paa ordre uden at tage klokkeslet i betragning
//Datoer i ordre er LocalDateTime men i hashMap er de LocalDate
//dato2 er sat foer dato1 i .compareTo() for at faa seneste dato foerst i listen
public class ComparatorDatoOmsaetning implements Comparator<Map.Entry<LocalDate, Double>> {

    public int compare(Map.Entry<LocalDate, Double> dato1, Map.Entry<LocalDate, Double> dato2) {
        return dato2.getKey().compareTo(dato1.getKey());
        }
}

