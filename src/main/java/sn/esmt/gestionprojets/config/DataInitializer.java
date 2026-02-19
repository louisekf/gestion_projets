package sn.esmt.gestionprojets.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.repository.DomaineRechercheRepository;
import sn.esmt.gestionprojets.service.UserService;

/**
 * DataInitializer : s'exécute AUTOMATIQUEMENT au démarrage de l'application.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DomaineRechercheRepository domaineRepository;
    private final UserService userService;

    // Constructeur explicite
    public DataInitializer(DomaineRechercheRepository domaineRepository, UserService userService) {
        this.domaineRepository = domaineRepository;
        this.userService = userService;
    }

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
            DomaineRecherche domaine = new DomaineRecherche();
            domaine.setCode(d[0]);
            domaine.setNom(d[1]);
            domaine.setDescription(d[2]);

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
                "Admin2026!",
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

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setInstitution("ESMT");
        user.setRole(role);
        user.setEnabled(true);

        userService.register(user);
        log.info("  → Utilisateur créé : {} ({})", email, role);
    }
}