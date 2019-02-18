package hhu.propra2.javageddon.teils.web;

import antlr.collections.AST;
import hhu.propra2.javageddon.teils.dataaccess.ArtikelRepository;
import hhu.propra2.javageddon.teils.dataaccess.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArtikelController {


    @Autowired
    private ArtikelRepository alleArtikel;

    @GetMapping("/")
    public String artikelListe(Model m){
        m.addAttribute("alleArtikel", alleArtikel.findByAktiv(true));
        return "start";
    }

    /*
        Diese Methode greift auf das Dateisystem des Dockercontainers zu und liefert das angefragte Bild aus.
     */
    @ResponseBody
    @RequestMapping(value = "/fotos/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImageAsResource(@PathVariable("id") String id) {
        return new FileSystemResource("fotos/" + id + ".jpg");
    }

/*
    @GetMapping("/edit/{id}")
    public String editPerson(Model m, @PathVariable("id") int id) {
        Person p = personen.getPerson(id);
        m.addAttribute("person", p);
        return "edit";
    }

    @PostMapping("/edit")
    public String changePerson(Model m, Person p, String skillList) {
        personen.merge(p, skillList);
        return "redirect:"
    }

    @GetMapping("/add")
    public String addPerson() {
        return "redirect:" + "/edit/" + personen.newPerson().getId();
    } */
}