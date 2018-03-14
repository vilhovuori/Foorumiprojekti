package foorumi.foorumi.Kontrolleri;

import foorumi.foorumi.Viesti.Viesti;
import foorumi.foorumi.Viestit.Viestit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class Kontrolleri {

    Viesti viesti = new Viesti();
    Viestit viestit = new Viestit(new ArrayList<>());

    @RequestMapping("/etusivu")
    public String Etusivu(Model model) {
        model.addAttribute("otsikko", "Etusivu");
        return "Etusivu";
    }

    @RequestMapping("/autohullut")
    public String Autohullut(@ModelAttribute Viesti viesti, Model model) {
        model.addAttribute("viestit", viestit.getViestit());
        return "Autohullut";
    }

    @GetMapping("/viestilomake")
    public String viestit(Model model) {
        model.addAttribute("viestiOlio", new Viesti());
        return "lisaaviesti";
    }

    @PostMapping("/autohullut")
    public String lomakeKasittelija(@ModelAttribute Viesti viesti, Model model) {
        viestit.lisaaListalle(viesti);
        model.addAttribute("viestit", viestit.getViestit());
        return "Autohullut";
    }

//    @RequestMapping("/kalajutut")
//    public String Kalajutut(Model model) {
//        model.addAttribute("otsikko", "Kalajutut");
//        return "Kalajutut";
//    }
//
//    @RequestMapping("/leffalopinat")
//    public String Leffalopinat(Model model) {
//        model.addAttribute("otsikko", "Leffalopinat");
//        return "Leffalopinat";
//    }
//
//    @RequestMapping("/golfjuorut")
//    public String Golfjuorut(Model model) {
//        model.addAttribute("otsikko", "Golfjuorut");
//        return "Golfjuorut";
//    }



    @GetMapping("/muokkaa")
    public String muokkaaSanontaa(@RequestParam(name = "id") int id, Model model) {
        Viesti etsitty = viesti.etsi(id);
        if (etsitty == null)
            return "redirect:etusivu";
        model.addAttribute("viesti", etsitty);
        return "muokkaus";
    }

    @PostMapping("/muokattu")
    public String tallennaMuokattu(Viesti viesti, Model model) {
        viesti.muokkaa(viesti);
        return "Etusivu";
}}
