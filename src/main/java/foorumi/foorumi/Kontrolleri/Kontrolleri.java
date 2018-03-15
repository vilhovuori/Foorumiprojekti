package foorumi.foorumi.Kontrolleri;

import foorumi.foorumi.Aihe.Aiherepo;
import foorumi.foorumi.Viesti.Viesti;
import foorumi.foorumi.Viesti.Viestirepo;
import foorumi.foorumi.Viesti.Viestit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class Kontrolleri {

    private Viestirepo vr;

    private Aiherepo ar;
    private JdbcTemplate jdbc;

    public Kontrolleri(@Autowired Viestirepo vr, @Autowired Aiherepo ar, @Autowired JdbcTemplate jdbc) {
        this.vr = vr;
        this.ar = ar;
        this.jdbc = jdbc;
    }

    Viesti viesti = new Viesti();
    Viestit viestit = new Viestit(new ArrayList<>());

    // printtaa kaikki viestit perustuen foorumi-sivulla muodostettuun sql-lausekkeeseen!
    private List<Viesti> getViestis(String sql) {
        return jdbc.query(sql,
                (rs, index)->{
                    Viesti c = new Viesti();
                    c.setTeksti(rs.getString("teksti"));
                    c.setKayttaja(rs.getString("kayttaja"));
                    c.setId(rs.getInt("id"));
                    c.setAlue(rs.getString("alue"));
                    return c;
                });
    }

    // viestin lisäys lomakkeelta palataan tähän, joka tallentaa viestin DB:hen ja ohjaa oikealle foorumille!
    @PostMapping("/ohjaus")
    public String ohjattuUrli(@ModelAttribute Viesti viesti, Model model){
        Viesti uusi = new Viesti();
        uusi.setTeksti(viesti.getTeksti());
        uusi.setKayttaja(viesti.getKayttaja());
        uusi.setAlue(viesti.getAlue());
        vr.save(uusi);
        String url = "redirect:/" + viesti.getAlue().toLowerCase();
        return url;
    }

    // avaa etusivun
    @RequestMapping("/etusivu")
    public String etusivu(Model model) {
        model.addAttribute("otsikko", "Etusivu");
        return "Etusivu";
    }

    // avaa autohullut-foorumin ja printtaa kaikki sen viestit
    @GetMapping("/autohullut")
    public String listaaKeskusteluAutoista(Model model){
        String sql = "SELECT teksti, kayttaja, id, alue FROM viesti WHERE alue = 'Autohullut'";
        List<Viesti> viestit = getViestis(sql);
        model.addAttribute("viestit", viestit);
        return "autohullut";
    }

    @GetMapping("/viestilomake")
    public String viestit(Model model) {
        model.addAttribute("viestiOlio", new Viesti());
        return "lisaaviesti";
    }

    @PostMapping("/autohullut")
    public String lomakeKasittelija(@ModelAttribute Viesti viesti, Model model) {
        String sql = "SELECT teksti, kayttaja, id, alue FROM viesti WHERE alue = 'Autohullut'";
        List<Viesti> viestit = getViestis(sql);
        model.addAttribute("viestit", viestit);
        return "autohullut";
    }

    @RequestMapping("/kalajutut")
    public String lisaaKeskusteluKalajutuista(Model model) {
        String sql = "SELECT teksti, kayttaja, id, alue FROM viesti WHERE alue = 'Kalajutut'";
        List<Viesti> viestit = getViestis(sql);
        model.addAttribute("viestit", viestit);
        return "Kalajutut";
    }

    @RequestMapping("/leffalopinat")
    public String leffalopinat(Model model) {
        String sql = "SELECT teksti, kayttaja, id, alue FROM viesti WHERE alue = 'Leffalopinat'";
        List<Viesti> viestit = getViestis(sql);
        model.addAttribute("viestit", viestit);
        return "Leffalopinat";
    }

    @RequestMapping("/golfjuorut")
    public String golfjuorut(Model model) {
        String sql = "SELECT teksti, kayttaja, id, alue FROM viesti WHERE alue = 'Golfjuorut'";
        List<Viesti> viestit = getViestis(sql);
        model.addAttribute("viestit", viestit);
        return "Golfjuorut";
    }

   /* @GetMapping("/muokkaa")
    public String muokkaaViestia(@RequestParam(name = "id") int id, Model model) {
        Viesti etsitty = viestit.etsi(id);
        if (etsitty == null)
            return "Etusivu";
        model.addAttribute("viesti", etsitty);
        return "muokkaus";
    }*/

    /*@PostMapping("/muokattu")
    public String tallennaMuokattu(Viesti viesti, Model model) {
        viestit.muokkaa(viesti);
        return "Etusivu";
    }*/

    /*@GetMapping("/poista")
    public String poistaViesti(@RequestParam(name = "id") int id) {
        viestit.poista(id);
        return "Etusivu";
    }*/

    @DeleteMapping("/poista/{id}")
    public String poistaViesti(@PathVariable(name = "id") int id) {
        vr.deleteById(id);
        return "Etusivu";
    }

    @PutMapping("/muokkaa/{id}")
    public String muokkaaViestia(@PathVariable(name = "id") int id, @RequestBody Viesti viesti) {
        viesti.setId(id);
        vr.save(viesti);
        return "Etusivu";
    }

}
