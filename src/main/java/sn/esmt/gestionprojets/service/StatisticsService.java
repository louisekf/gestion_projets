package sn.esmt.gestionprojets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.dto.StatisticsDTO;
import sn.esmt.gestionprojets.entity.StatutProjet;
import sn.esmt.gestionprojets.repository.ProjetRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service dédié au calcul des statistiques pour le tableau de bord.
 *
 * Accédé uniquement par les MANAGER et ADMIN (contrôle fait dans le controller).
 *
 * Toutes les données viennent des requêtes JPQL définies dans ProjectRepository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final ProjetRepository projectRepository;

    /**
     * Calcule et retourne l'ensemble des statistiques en une seule fois.
     * Le résultat est encapsulé dans un StatisticsDTO prêt à être
     * passé au template Thymeleaf ou retourné en JSON.
     */
    public StatisticsDTO computeAll() {
        log.debug("Calcul des statistiques globales...");

        return StatisticsDTO.builder()
                // -- Chiffres bruts --
                .totalProjets(projectRepository.count())
                .projetsEnCours(projectRepository.countByStatut(StatutProjet.EN_COURS))
                .projetsTermines(projectRepository.countByStatut(StatutProjet.TERMINE))
                .projetsSuspendus(projectRepository.countByStatut(StatutProjet.SUSPENDU))
                .tauxMoyenAvancement(getAverageAvancement())

                // -- Données graphiques --
                .projetsByDomaine(getProjetsByDomaine())
                .projetsByStatut(getProjetsByStatut())
                .projetsByYear(getProjetsByYear())
                .chargeByParticipant(getChargeByParticipant())
                .budgetByDomaine(getBudgetByDomaine())

                .build();
    }

    // -------------------------------------------------------
    // Méthodes privées : transformation des résultats bruts
    // en Maps utilisables dans les templates / graphiques
    // -------------------------------------------------------

    /**
     * Taux moyen d'avancement des projets EN_COURS.
     * Retourne 0.0 si aucun projet en cours (évite NullPointerException).
     */
    private double getAverageAvancement() {
        Double avg = projectRepository.findAverageAvancement();
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0; // arrondi à 1 décimale
    }

    /**
     * Graphique 1 – Projets par domaine.
     * Transforme List<Object[]> en Map<String, Long>
     * Object[0] = nomDomaine (String), Object[1] = count (Long)
     */
    private Map<String, Long> getProjetsByDomaine() {
        List<Object[]> results = projectRepository.countProjectsGroupByDomaine();
        Map<String, Long> map = new LinkedHashMap<>(); // LinkedHashMap conserve l'ordre d'insertion
        for (Object[] row : results) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    /**
     * Graphique 2 – Répartition par statut.
     * Object[0] = StatutProjet (Enum), Object[1] = count (Long)
     * On convertit l'enum en String lisible.
     */
    private Map<String, Long> getProjetsByStatut() {
        List<Object[]> results = projectRepository.countProjectsGroupByStatut();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            StatutProjet statut = (StatutProjet) row[0];
            String label = switch (statut) {
                case EN_COURS   -> "En cours";
                case TERMINE    -> "Terminé";
                case SUSPENDU   -> "Suspendu";
            };
            map.put(label, (Long) row[1]);
        }
        return map;
    }

    /**
     * Graphique 3 – Évolution temporelle.
     * Object[0] = année (Integer), Object[1] = count (Long)
     */
    private Map<Integer, Long> getProjetsByYear() {
        List<Object[]> results = projectRepository.countProjectsGroupByYear();
        Map<Integer, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put(((Number) row[0]).intValue(), (Long) row[1]);
        }
        return map;
    }

    /**
     * Graphique 4 – Charge des participants.
     * Object[0] = nomComplet (String), Object[1] = count (Long)
     */
    private Map<String, Long> getChargeByParticipant() {
        List<Object[]> results = projectRepository.countProjectsGroupByParticipant();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    /**
     * Budget total par domaine (graphique complémentaire).
     * Object[0] = nomDomaine (String), Object[1] = budgetTotal (BigDecimal)
     */
    private Map<String, BigDecimal> getBudgetByDomaine() {
        List<Object[]> results = projectRepository.sumBudgetGroupByDomaine();
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (BigDecimal) row[1]);
        }
        return map;
    }

}
