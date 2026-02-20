package sn.esmt.gestionprojets.controller.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.repository.UserRepository;
import sn.esmt.gestionprojets.security.UserPrincipal;

@Controller
@RequestMapping("/profil")
public class ProfilWebController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfilWebController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** GET /profil — afficher la page profil */
    @GetMapping
    public String showProfil(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        model.addAttribute("user", principal.getUser());
        return "profil";
    }

    /** POST /profil — dispatcher selon _action */
    @PostMapping
    public String handlePost(
            @RequestParam String _action,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String institution,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User user = principal.getUser();

        if ("updateInfo".equals(_action)) {
            user.setPrenom(prenom);
            user.setNom(nom);
            user.setEmail(email);
            user.setTelephone(telephone);
            user.setInstitution(institution);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Informations mises à jour !");

        } else if ("changePassword".equals(_action)) {
            // Vérifier mot de passe actuel
            if (user.getPassword() == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mot de passe actuel incorrect.");
                return "redirect:/profil";
            }
            // Vérifier confirmation
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Les nouveaux mots de passe ne correspondent pas.");
                return "redirect:/profil";
            }
            if (newPassword.length() < 8) {
                redirectAttributes.addFlashAttribute("error", "Le mot de passe doit faire au moins 8 caractères.");
                return "redirect:/profil";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Mot de passe modifié avec succès !");
        }

        return "redirect:/profil";
    }
}
