package sn.esmt.gestionprojets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.dto.StatisticsDTO;
import sn.esmt.gestionprojets.entity.enums.StatutProjet;
import sn.esmt.gestionprojets.repository.ProjetRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service dédié au calcul des statistiques pour le tableau de bord.
 */
@Service
public class StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private final ProjetRepository projectRepository;

    // Constructeur explicite
    public StatisticsService(ProjetRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Calcule et retourne l'ensemble des statistiques en une seule fois.
     */
    public StatisticsDTO computeAll() {
        log.debug("Calcul des statistiques globales...");

        StatisticsDTO stats = new StatisticsDTO();

        // -- Chiffres bruts --
        stats.setTotalProjets(projectRepository.count());
        stats.setProjetsEnCours(projectRepository.countByStatut(StatutProjet.EN_COURS));
        stats.setProjetsTermines(projectRepository.countByStatut(StatutProjet.TERMINE));
        stats.setProjetsSuspendus(projectRepository.countByStatut(StatutProjet.SUSPENDU));
        stats.setTauxMoyenAvancement(getAverageAvancement());

        // -- Données graphiques --
        stats.setProjetsByDomaine(getProjetsByDomaine());
        stats.setProjetsByStatut(getProjetsByStatut());
        stats.setProjetsByYear(getProjetsByYear());
        stats.setChargeByParticipant(getChargeByParticipant());
        stats.setBudgetByDomaine(getBudgetByDomaine());

        return stats;
    }

    // -------------------------------------------------------
    // Méthodes privées : transformation des résultats bruts
    // -------------------------------------------------------

    /**
     * Taux moyen d'avancement des projets EN_COURS.
     */
    private double getAverageAvancement() {
        Double avg = projectRepository.findAverageAvancement();
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    /**
     * Graphique 1 – Projets par domaine.
     */
    private Map<String, Long> getProjetsByDomaine() {
        List<Object[]> results = projectRepository.countProjectsGroupByDomaine();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    /**
     * Graphique 2 – Répartition par statut.
     */
    private Map<String, Long> getProjetsByStatut() {
        List<Object[]> results = projectRepository.countProjectsGroupByStatut();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            StatutProjet statut = (StatutProjet) row[0];
            String label = switch (statut) {

                case PLANIFIE   -> "Planifie";
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
     * Budget total par domaine.
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