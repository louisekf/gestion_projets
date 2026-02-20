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

    // ── Liste ────────────────────────────────────────────────────────────────
    @GetMapping
    public String listProjets(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("projets", projetService.findVisibleProjects());
        model.addAttribute("user", principal.getUser());
        return "projets/list";
    }

    // ── Détail ───────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public String viewProjet(@PathVariable Long id,
                             @AuthenticationPrincipal UserPrincipal principal,
                             Model model) {
        model.addAttribute("projet", projetService.findById(id));
        // Nécessaire pour la modale "Ajouter participant" (MANAGER/ADMIN uniquement)
        if (principal.getUser().isManagerOrAdmin()) {
            model.addAttribute("tousLesUsers", userService.findAll());
        }
        return "projets/view";
    }

    // ── Création ─────────────────────────────────────────────────────────────
    @GetMapping("/nouveau")
    public String showCreateForm(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = principal.getUser();
        model.addAttribute("projetRequest", new ProjetRequest());
        model.addAttribute("domaines", domaineService.findAll());
        model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());
        if (user.isManagerOrAdmin()) {
            model.addAttribute("users", userService.findAll());
        }
        return "projets/create";
    }

    @PostMapping
    public String createProjet(@Valid @ModelAttribute("projetRequest") ProjetRequest request,
                               BindingResult result,
                               @AuthenticationPrincipal UserPrincipal principal,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            User user = principal.getUser();
            model.addAttribute("domaines", domaineService.findAll());
            model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());
            if (user.isManagerOrAdmin()) model.addAttribute("users", userService.findAll());
            return "projets/create";
        }
        Projet projet = projetService.create(request);
        redirectAttributes.addFlashAttribute("success", "Projet créé avec succès !");
        return "redirect:/projets/" + projet.getId();
    }

    // ── Édition ──────────────────────────────────────────────────────────────
    @GetMapping("/{id}/modifier")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal UserPrincipal principal,
                               Model model) {
        User user = principal.getUser();
        Projet projet = projetService.findById(id);

        ProjetRequest request = new ProjetRequest();
        request.setTitre(projet.getTitre());
        request.setDescription(projet.getDescription());
        request.setDateDebut(projet.getDateDebut());
        request.setDateFin(projet.getDateFin());
        request.setStatut(projet.getStatut());
        request.setBudgetEstime(projet.getBudgetEstime());
        request.setInstitution(projet.getInstitution());
        request.setNiveauAvancement(projet.getNiveauAvancement());
        request.setDomaineId(projet.getDomaine() != null ? projet.getDomaine().getId() : null);
        // ← pré-remplir le responsable pour le select
        request.setResponsableId(projet.getResponsable() != null ? projet.getResponsable().getId() : null);

        model.addAttribute("projetRequest", request);
        model.addAttribute("projetId", id);
        model.addAttribute("domaines", domaineService.findAll());
        model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());
        if (user.isManagerOrAdmin()) model.addAttribute("users", userService.findAll());
        return "projets/edit";
    }

    @PostMapping("/{id}")
    public String updateProjet(@PathVariable Long id,
                               @Valid @ModelAttribute("projetRequest") ProjetRequest request,
                               BindingResult result,
                               @AuthenticationPrincipal UserPrincipal principal,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            User user = principal.getUser();
            model.addAttribute("projetId", id);
            model.addAttribute("domaines", domaineService.findAll());
            model.addAttribute("isManagerOrAdmin", user.isManagerOrAdmin());
            if (user.isManagerOrAdmin()) model.addAttribute("users", userService.findAll());
            return "projets/edit";
        }
        projetService.update(id, request);
        redirectAttributes.addFlashAttribute("success", "Projet mis à jour avec succès !");
        return "redirect:/projets/" + id;
    }

    // ── Participants ─────────────────────────────────────────────────────────

    /**
     * POST /projets/{id}/participants
     * Formulaire modale "Ajouter un participant" dans view.html
     */
    @PostMapping("/{id}/participants")
    public String addParticipant(@PathVariable Long id,
                                 @RequestParam Long userId,
                                 RedirectAttributes redirectAttributes) {
        try {
            projetService.addParticipant(id, userId);
            redirectAttributes.addFlashAttribute("success", "Participant ajouté !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible d'ajouter ce participant.");
        }
        return "redirect:/projets/" + id;
    }

    /**
     * POST /projets/{id}/participants/{uid}/retirer
     * Bouton ✕ dans view.html (workaround car les forms HTML ne supportent pas DELETE)
     */
    @PostMapping("/{id}/participants/{uid}/retirer")
    public String removeParticipant(@PathVariable Long id,
                                    @PathVariable Long uid,
                                    RedirectAttributes redirectAttributes) {
        try {
            projetService.removeParticipant(id, uid);
            redirectAttributes.addFlashAttribute("success", "Participant retiré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de retirer ce participant.");
        }
        return "redirect:/projets/" + id;
    }

    // ── Suppression ──────────────────────────────────────────────────────────
    @PostMapping("/{id}/supprimer")
    public String deleteProjet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projetService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Projet supprimé avec succès !");
        return "redirect:/projets";
    }
}