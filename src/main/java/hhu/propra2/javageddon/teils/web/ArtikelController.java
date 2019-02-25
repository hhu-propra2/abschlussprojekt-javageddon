package hhu.propra2.javageddon.teils.web;

import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.BenutzerRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import hhu.propra2.javageddon.teils.services.ArtikelService;
import hhu.propra2.javageddon.teils.services.BenutzerService;
import hhu.propra2.javageddon.teils.model.Adresse;
import hhu.propra2.javageddon.teils.services.ReservierungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ArtikelController {


    @Autowired
    private ArtikelService alleArtikel;

    @Autowired
    private BenutzerService alleBenutzer;

    @Autowired
    private ReservierungService alleReservierungen;

    @GetMapping("/")
    public String artikelListe(Model m){
        m.addAttribute("alleArtikel", alleArtikel.findAllAktivArtikel());
        return "start";
    }

    /*
    Diese Methode greift auf das Dateisystem des Dockercontainers zu und liefert das angefragte Bild aus.
    */
    @ResponseBody
    @RequestMapping(value = "/fotos/{id}", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImageAsResource(@PathVariable("id") String id) {
        return new FileSystemResource("fotos/" + id + ".jpg");
    }

    @RequestMapping(value = "/details", method = GET)
    public String getDetailsByArtikelId( Model m, @RequestParam("id") long id) {
        Artikel artikel = alleArtikel.findArtikelById(id);
        List<Reservierung> artikelReservierungen = alleReservierungen.findCurrentReservierungByArtikelOrderedByDate(artikel);
        m.addAttribute("alleReservierungen", artikelReservierungen);
        m.addAttribute("artikel", artikel);
        return "artikel_details";
    }

    @GetMapping("/artikel_erstellen")
    public String artikelErstellen(Model m){
        m.addAttribute("artikel", new Artikel());
        m.addAttribute("adresse", new Adresse());
        return "artikel_erstellen";
    }

    @PostMapping("/artikel_erstellen")
    public String erstelleArtikel(@Valid Artikel artikel, BindingResult artikelBindingResult, @Valid Adresse adresse, BindingResult standortBindingResult){
        if(artikelBindingResult.hasErrors() || standortBindingResult.hasErrors()){
            return "artikel_erstellen";
        }else {
            Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) currentUser).getUsername();
            Long id = alleBenutzer.getIdByName(username);

            artikel.setStandort(adresse);
            artikel.setFotos(new ArrayList<String>());
            artikel.setEigentuemer(alleBenutzer.findBenutzerById(id));
            alleArtikel.addArtikel(artikel);
            return "redirect:/fotoupload/" + artikel.getId();
        }
    }

}