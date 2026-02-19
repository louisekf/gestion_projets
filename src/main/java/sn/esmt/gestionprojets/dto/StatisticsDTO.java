package sn.esmt.gestionprojets.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO regroupant toutes les statistiques du tableau de bord.
 */
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
    // -------------------------------------------------------

    private Map<String, Long> projetsByDomaine;
    private Map<String, Long> projetsByStatut;
    private Map<Integer, Long> projetsByYear;
    private Map<String, Long> chargeByParticipant;
    private Map<String, BigDecimal> budgetByDomaine;

    // Constructeur vide
    public StatisticsDTO() {
    }

    // Constructeur avec tous les paramètres
    public StatisticsDTO(long totalProjets, double tauxMoyenAvancement, long projetsEnCours,
                         long projetsTermines, long projetsSuspendus, Map<String, Long> projetsByDomaine,
                         Map<String, Long> projetsByStatut, Map<Integer, Long> projetsByYear,
                         Map<String, Long> chargeByParticipant, Map<String, BigDecimal> budgetByDomaine) {
        this.totalProjets = totalProjets;
        this.tauxMoyenAvancement = tauxMoyenAvancement;
        this.projetsEnCours = projetsEnCours;
        this.projetsTermines = projetsTermines;
        this.projetsSuspendus = projetsSuspendus;
        this.projetsByDomaine = projetsByDomaine;
        this.projetsByStatut = projetsByStatut;
        this.projetsByYear = projetsByYear;
        this.chargeByParticipant = chargeByParticipant;
        this.budgetByDomaine = budgetByDomaine;
    }

    // Getters et Setters
    public long getTotalProjets() {
        return totalProjets;
    }

    public void setTotalProjets(long totalProjets) {
        this.totalProjets = totalProjets;
    }

    public double getTauxMoyenAvancement() {
        return tauxMoyenAvancement;
    }

    public void setTauxMoyenAvancement(double tauxMoyenAvancement) {
        this.tauxMoyenAvancement = tauxMoyenAvancement;
    }

    public long getProjetsEnCours() {
        return projetsEnCours;
    }

    public void setProjetsEnCours(long projetsEnCours) {
        this.projetsEnCours = projetsEnCours;
    }

    public long getProjetsTermines() {
        return projetsTermines;
    }

    public void setProjetsTermines(long projetsTermines) {
        this.projetsTermines = projetsTermines;
    }

    public long getProjetsSuspendus() {
        return projetsSuspendus;
    }

    public void setProjetsSuspendus(long projetsSuspendus) {
        this.projetsSuspendus = projetsSuspendus;
    }

    public Map<String, Long> getProjetsByDomaine() {
        return projetsByDomaine;
    }

    public void setProjetsByDomaine(Map<String, Long> projetsByDomaine) {
        this.projetsByDomaine = projetsByDomaine;
    }

    public Map<String, Long> getProjetsByStatut() {
        return projetsByStatut;
    }

    public void setProjetsByStatut(Map<String, Long> projetsByStatut) {
        this.projetsByStatut = projetsByStatut;
    }

    public Map<Integer, Long> getProjetsByYear() {
        return projetsByYear;
    }

    public void setProjetsByYear(Map<Integer, Long> projetsByYear) {
        this.projetsByYear = projetsByYear;
    }

    public Map<String, Long> getChargeByParticipant() {
        return chargeByParticipant;
    }

    public void setChargeByParticipant(Map<String, Long> chargeByParticipant) {
        this.chargeByParticipant = chargeByParticipant;
    }

    public Map<String, BigDecimal> getBudgetByDomaine() {
        return budgetByDomaine;
    }

    public void setBudgetByDomaine(Map<String, BigDecimal> budgetByDomaine) {
        this.budgetByDomaine = budgetByDomaine;
    }
}