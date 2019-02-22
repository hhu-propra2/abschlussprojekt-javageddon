package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ReservierungService {

    private ReservierungRepository alleReservierungen;

    public ReservierungService(ReservierungRepository reservierungen){
        this.alleReservierungen = reservierungen;
    }

    public Reservierung addReservierung(Reservierung r) {
        return alleReservierungen.save(r);
    }

    public List<Reservierung> findReservierungByArtikel(Artikel a){
        return alleReservierungen.findByArtikel(a);
    }

    public List<Reservierung> findReservierungByLeihender(Benutzer b){
        return alleReservierungen.findByLeihender(b);
    }

    public List<Reservierung> findReservierungByArtikelAndLeihender(Artikel a, Benutzer b){
        return alleReservierungen.findByArtikelAndLeihender(a,b);
    }
    
    public List<Reservierung> findReservierungByArtikelEigentuemerAndNichtBearbeitet(Benutzer b) {
    	return alleReservierungen.findByArtikelEigentuemerAndBearbeitet(b, false);
    }

    public List<Reservierung> findCurrentReservierungByArtikelOrderedByDate(Artikel a){
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikel(a);
        List<Reservierung> vergangeneReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getEnde().isBefore(currentDay)) {
                vergangeneReservierungen.add(res);
            }
        }
        artikelReservierungen.removeAll(vergangeneReservierungen);

        return artikelReservierungen
                .stream()
                .sorted((r1,r2) -> r1.getEnde().compareTo(r2.getEnde()))
                .collect(Collectors.toList());
    }

    public boolean isAllowedReservierungsDate(Artikel a, LocalDate startAntrag, LocalDate endeAntrag) {
        if (startAntrag.isBefore(LocalDate.now())) {
            return false;
        }
        if (endeAntrag.isBefore(startAntrag)) {
            return false;
        }
        Reservierung testDate = Reservierung.builder().start(startAntrag).ende(endeAntrag).build();
        List<Reservierung> artikelReservierung = alleReservierungen.findByArtikel(a);
        for (Reservierung res : artikelReservierung) {
            if (res.isBetween(startAntrag) || res.isBetween(endeAntrag)
                    || testDate.isBetween(res.getStart())
                    || testDate.isBetween(res.getEnde())) {
                return false;
            }
        }
        return true;
    }

}
