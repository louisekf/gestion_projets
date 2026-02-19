package sn.esmt.gestionprojets.config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.Role;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.repository.DomaineRechercheRepository;
import sn.esmt.gestionprojets.service.UserService;

/**
 * DataInitializer : s'exécute AUTOMATIQUEMENT au démarrage de l'application.
 *
 * Implémente CommandLineRunner → la méthode run() est appelée une fois
 * que Spring Boot a fini de démarrer (bean, sécurité, DB tout prêts).
 *
 * Crée :
 *  - Les domaines de recherche par défaut
 *  - Un compte ADMIN par défaut (à changer en production !)
 *  - Un compte MANAGER par défaut
 *  - Un compte USER de test
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DomaineRechercheRepository domaineRepository;
    private final UserService userService;

    @Override
    public void run(String... args) {
        log.info("=== Initialisation des données de démarrage ===");
        initDomaines();
        initUsers();
        log.info("=== Initialisation terminée ===");
    }

    // -------------------------------------------------------
    // Domaines de recherche
    // -------------------------------------------------------

    private void initDomaines() {
        if (domaineRepository.count() > 0) {
            log.info("Domaines déjà présents en base, skip.");
            return;
        }

        log.info("Création des domaines de recherche...");

        String[][] domaines = {
                {"IA",       "Intelligence Artificielle",    "Apprentissage automatique, deep learning, NLP"},
                {"SANTE",    "Santé",                        "Médecine, biotechnologie, santé numérique"},
                {"ENERGIE",  "Énergie",                      "Énergies renouvelables, stockage, efficacité"},
                {"TELECOM",  "Télécommunications",            "Réseaux, 5G, protocoles de communication"},
                {"SECURITE", "Cybersécurité",                "Sécurité des systèmes, cryptographie"},
                {"DATA",     "Data Science",                 "Analyse de données, Big Data, visualisation"},
                {"AGRI",     "Agriculture",                  "AgriTech, durabilité, systèmes alimentaires"},
        };

        for (String[] d : domaines) {
            DomaineRecherche domaine = DomaineRecherche.builder()
                    .code(d[0])
                    .nom(d[1])
                    .description(d[2])
                    .build();
            domaineRepository.save(domaine);
            log.info("  → Domaine créé : {}", d[1]);
        }
    }

    // -------------------------------------------------------
    // Utilisateurs par défaut
    // -------------------------------------------------------

    private void initUsers() {
        createUserIfNotExists(
                "admin",
                "admin@esmt.sn",
                "Admin2026!",           // À changer ABSOLUMENT en production
                "ADMIN",
                "ESMT",
                Role.ADMIN
        );

        createUserIfNotExists(
                "gestionnaire",
                "manager@esmt.sn",
                "Manager2026!",
                "Faye",
                "Louise",
                Role.MANAGER
        );

        createUserIfNotExists(
                "chercheur1",
                "chercheur@esmt.sn",
                "User2026!",
                "Sambe",
                "Aziz",
                Role.USER
        );
    }

    private void createUserIfNotExists(String username, String email, String password,
                                       String nom, String prenom, Role role) {
        if (userService.emailExists(email)) {
            log.info("Utilisateur '{}' déjà présent, skip.", email);
            return;
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)  // sera encodé par UserService.register()
                .nom(nom)
                .prenom(prenom)
                .institution("ESMT")
                .role(role)
                .enabled(true)
                .build();

        userService.register(user);
        log.info("  → Utilisateur créé : {} ({})", email, role);
    }
}
