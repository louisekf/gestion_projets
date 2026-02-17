package sn.esmt.gestionprojets.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO regroupant toutes les statistiques du tableau de bord.
 * Passé directement au template Thymeleaf (ou retourné en JSON via l'API).
 *
 * Conçu pour être construit une fois par StatisticsService et utilisé
 * par les templates sans appel supplémentaire à la base de données.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDTO {

    // -------------------------------------------------------
    // Statistiques simples (chiffres bruts)
    // -------------------------------------------------------

    private long totalProjets;

    private double tauxMoyenAvancement;

    private long projetsEnCours;

    private long projetsTermines;

    private long projetsSuspendus;

    // -------------------------------------------------------
    // Données pour les graphiques
    // (Maps : clé = label, valeur = données numériques)
    // -------------------------------------------------------

    /**
     * Graphique 1 : Nombre de projets par domaine.
     */
    private Map<String, Long> projetsByDomaine;

    /**
     * Graphique 2 : Répartition par statut.
     */
    private Map<String, Long> projetsByStatut;

    /**
     * Graphique 3 : Évolution temporelle.
     */
    private Map<Integer, Long> projetsByYear;

    /**
     * Graphique 4 : Charge des participants.
     */
    private Map<String, Long> chargeByParticipant;

    /**
     * Budget total par domaine (pour graphique complémentaire).
     */
    private Map<String, BigDecimal> budgetByDomaine;
}
