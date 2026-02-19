package sn.esmt.gestionprojets.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.service.DomaineRechercheService;
import sn.esmt.gestionprojets.service.UserService;

import java.util.List;

/**
 * Controller web pour l'administration (ADMIN uniquement).
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final UserService userService;
    private final DomaineRechercheService domaineService;

    public AdminWebController(UserService userService, DomaineRechercheService domaineService) {
        this.userService = userService;
        this.domaineService = domaineService;
    }

    /**
     * GET /admin - Page d'accueil admin.
     */
    @GetMapping
    public String adminHome(Model model) {
        long totalUsers = userService.findAll().size();
        long totalDomaines = domaineService.findAll().size();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalDomaines", totalDomaines);

        return "admin/index";  // templates/admin/index.html
    }

    // ==================== GESTION UTILISATEURS ====================

    /**
     * GET /admin/users - Liste des utilisateurs.
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";  // templates/admin/users.html
    }

    /**
     * POST /admin/users/{id}/toggle-enabled - Activer/désactiver un compte.
     */
    @PostMapping("/users/{id}/toggle-enabled")
    public String toggleUserEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.toggleEnabled(id);
        String status = user.isEnabled() ? "activé" : "désactivé";
        redirectAttributes.addFlashAttribute("success", "Compte " + status + " avec succès !");
        return "redirect:/admin/users";
    }

    /**
     * POST /admin/users/{id}/change-role - Changer le rôle d'un utilisateur.
     */
    @PostMapping("/users/{id}/change-role")
    public String changeUserRole(
            @PathVariable Long id,
            @RequestParam Role newRole,
            RedirectAttributes redirectAttributes) {
        userService.changeRole(id, newRole);
        redirectAttributes.addFlashAttribute("success", "Rôle modifié avec succès !");
        return "redirect:/admin/users";
    }

    /**
     * POST /admin/users/{id}/delete - Supprimer un utilisateur.
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès !");
        return "redirect:/admin/users";
    }

    // ==================== GESTION DOMAINES ====================

    /**
     * GET /admin/domaines - Liste des domaines.
     */
    @GetMapping("/domaines")
    public String listDomaines(Model model) {
        List<DomaineRecherche> domaines = domaineService.findAll();
        model.addAttribute("domaines", domaines);
        model.addAttribute("nouveauDomaine", new DomaineRecherche());
        return "admin/domaines";  // templates/admin/domaines.html
    }

    /**
     * POST /admin/domaines - Créer un nouveau domaine.
     */
    @PostMapping("/domaines")
    public String createDomaine(
            @ModelAttribute DomaineRecherche domaine,
            RedirectAttributes redirectAttributes) {
        domaineService.create(domaine);
        redirectAttributes.addFlashAttribute("success", "Domaine créé avec succès !");
        return "redirect:/admin/domaines";
    }

    /**
     * POST /admin/domaines/{id}/delete - Supprimer un domaine.
     */
    @PostMapping("/domaines/{id}/delete")
    public String deleteDomaine(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            domaineService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Domaine supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de supprimer ce domaine car des projets y sont associés.");
        }
        return "redirect:/admin/domaines";
    }
}