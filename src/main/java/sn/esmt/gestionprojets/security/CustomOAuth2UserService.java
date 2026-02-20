package sn.esmt.gestionprojets.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.repository.UserRepository;
import java.util.UUID;

/**
 * Service OAuth2 personnalisé.
 * Intercepte le profil Google retourné, crée ou met à jour le compte
 * dans notre BDD, puis retourne un UserPrincipal standard.
 *
 * C'est ce service qui garantit que @AuthenticationPrincipal UserPrincipal
 * fonctionne aussi bien après login Google qu'après login/password classique.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Appel standard Google → récupère profil
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Extraire les attributs Google
        String email     = oAuth2User.getAttribute("email");
        String nom       = oAuth2User.getAttribute("family_name");
        String prenom    = oAuth2User.getAttribute("given_name");
        String googleSub = oAuth2User.getAttribute("sub"); // identifiant unique Google

        log.debug("OAuth2 login : email={}, sub={}", email, googleSub);

        // 3. Trouver ou créer l'utilisateur en BDD
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewOAuth2User(email, nom, prenom, googleSub));

        // Mettre à jour le googleId si première connexion OAuth2 sur un compte existant
        if (user.getGoogleId() == null) {
            user.setGoogleId(googleSub);
            user = userRepository.save(user);
            log.info("GoogleId associé au compte existant : {}", email);
        }

        // 4. Retourner un UserPrincipal → @AuthenticationPrincipal UserPrincipal fonctionnera
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User createNewOAuth2User(String email, String nom, String prenom, String googleSub) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setNom(nom != null ? nom : "");
        user.setPrenom(prenom != null ? prenom : "");
        user.setGoogleId(googleSub);
        user.setRole(Role.USER);
        user.setEnabled(true);
        // Pas de mot de passe — connexion OAuth2 uniquement
        user.setPassword(null);

        user.setPassword(UUID.randomUUID().toString());

        User saved = userRepository.save(user);
        log.info("Nouveau compte créé via OAuth2 Google : {}", email);
        return saved;
    }
}
