package sn.esmt.gestionprojets.controller.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.security.UserPrincipal;
import sn.esmt.gestionprojets.service.ProjetService;

import java.util.List;

/**
 * Controller pour les pages web Thymeleaf.
 */
@Controller
public class HomeController {

    private final ProjetService projetService;

    public HomeController(ProjetService projetService) {
        this.projetService = projetService;
    }

    /**
     * Page d'accueil publique.
     */
    @GetMapping("/")
    public String home() {
        return "index";  // templates/index.html
    }

    /**
     * Page de login.
     */
    @GetMapping("/login")
    public String login() {
        return "login";  // templates/login.html
    }

    /**
     * Dashboard principal (après login).
     */
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = principal.getUser();

        // Récupérer les projets visibles par cet utilisateur
        List<Projet> projets = projetService.findVisibleProjects();

        model.addAttribute("user", user);
        model.addAttribute("nomComplet", user.getNomComplet());
        model.addAttribute("role", user.getRole());
        model.addAttribute("projets", projets);
        model.addAttribute("nombreProjets", projets.size());

        return "dashboard";  // templates/dashboard.html
    }

    /**
     * Page d'accès refusé.
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";  // templates/error/403.html
    }
}