package sn.esmt.gestionprojets.controller.web;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.security.UserPrincipal;
import sn.esmt.gestionprojets.service.DomaineRechercheService;
import sn.esmt.gestionprojets.service.ProjetService;
import sn.esmt.gestionprojets.service.UserService;

import java.util.List;

/**
 * Controller web pour la gestion des projets (pages Thymeleaf).
 */
@Controller
@RequestMapping("/projets")
public class ProjetWebController {

    private final ProjetService projetService;
    private final DomaineRechercheService domaineService;
    private final UserService userService;

    public ProjetWebController(ProjetService projetService,
                               DomaineRechercheService domaineService,
                               UserService userService) {
        this.projetService = projetService;
        this.domaineService = domaineService;
        this.userService = userService;
    }

    /**
     * GET /projets - Liste des projets.
     */
    @GetMapping
    public String listProjets(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = principal.getUser();
        List<Projet> projets = projetService.findVisibleProjects();

        model.addAttribute("projets", projets);
        model.addAttribute("user", user);
        return "projets/list";  // templates/projets/list.html
    }

    /**
     * GET /projets/{id} - Détails d'un projet.
     */
    @GetMapping("/{id}")
    public String viewProjet(@PathVariable Long id, Model model) {
        Projet projet = projetService.findById(id);
        model.addAttribute("projet", projet);
        return "projets/view";  // templates/projets/view.html
    }

    /**
     * GET /projets/nouveau - Formulaire de création.
     */
    @GetMapping("/nouveau")
    public String showCreateForm(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = principal.getUser();
        List<DomaineRecherche> domaines = domaineService.findAll();
        List<User> users = user.isManagerOrAdmin() ? userService.findAll() : null;

        model.addAttribute("projetRequest", new ProjetRequest());
        model.addAttribute("domaines", domaines);
        model.addAttribute("users", users);
        model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());

        return "projets/create";  // templates/projets/create.html
    }

    /**
     * POST /projets - Créer un projet.
     */
    @PostMapping
    public String createProjet(
            @Valid @ModelAttribute("projetRequest") ProjetRequest request,
            BindingResult result,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            User user = principal.getUser();
            List<DomaineRecherche> domaines = domaineService.findAll();
            List<User> users = user.isManagerOrAdmin() ? userService.findAll() : null;

            model.addAttribute("domaines", domaines);
            model.addAttribute("users", users);
            model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());

            return "projets/create";
        }

        Projet projet = projetService.create(request);
        redirectAttributes.addFlashAttribute("success", "Projet créé avec succès !");
        return "redirect:/projets/" + projet.getId();
    }

    /**
     * GET /projets/{id}/modifier - Formulaire de modification.
     */
    @GetMapping("/{id}/modifier")
    public String showEditForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal,
            Model model) {

        User user = principal.getUser();
        Projet projet = projetService.findById(id);
        List<DomaineRecherche> domaines = domaineService.findAll();
        List<User> users = user.isManagerOrAdmin() ? userService.findAll() : null;

        // Convertir Projet en ProjetRequest
        ProjetRequest request = new ProjetRequest();
        request.setTitre(projet.getTitre());
        request.setDescription(projet.getDescription());
        request.setDateDebut(projet.getDateDebut());
        request.setDateFin(projet.getDateFin());
        request.setStatut(projet.getStatut());
        request.setBudgetEstime(projet.getBudgetEstime());
        request.setInstitution(projet.getInstitution());
        request.setNiveauAvancement(projet.getNiveauAvancement());
        request.setDomaineId(projet.getDomaine().getId());

        model.addAttribute("projetRequest", request);
        model.addAttribute("projetId", id);
        model.addAttribute("domaines", domaines);
        model.addAttribute("users", users);
        model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());

        return "projets/edit";  // templates/projets/edit.html
    }

    /**
     * POST /projets/{id} - Mettre à jour un projet.
     */
    @PostMapping("/{id}")
    public String updateProjet(
            @PathVariable Long id,
            @Valid @ModelAttribute("projetRequest") ProjetRequest request,
            BindingResult result,
            @AuthenticationPrincipal UserPrincipal principal,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            User user = principal.getUser();
            List<DomaineRecherche> domaines = domaineService.findAll();
            List<User> users = user.isManagerOrAdmin() ? userService.findAll() : null;

            model.addAttribute("projetId", id);
            model.addAttribute("domaines", domaines);
            model.addAttribute("users", users);
            model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());

            return "projets/edit";
        }

        projetService.update(id, request);
        redirectAttributes.addFlashAttribute("success", "Projet mis à jour avec succès !");
        return "redirect:/projets/" + id;
    }

    /**
     * POST /projets/{id}/supprimer - Supprimer un projet (ADMIN uniquement).
     */
    @PostMapping("/{id}/supprimer")
    public String deleteProjet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projetService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Projet supprimé avec succès !");
        return "redirect:/projets";
    }
}