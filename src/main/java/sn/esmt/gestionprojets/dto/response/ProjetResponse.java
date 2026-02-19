package sn.esmt.gestionprojets.dto.response;

import sn.esmt.gestionprojets.entity.enums.StatutProjet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjetResponse {

    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutProjet statut;
    private BigDecimal budgetEstime;
    private String institution;
    private Integer niveauAvancement;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relations
    private DomaineRechercheResponse domaine;
    private UserResponse responsable;
    private List<UserResponse> participants;

    // Constructeur vide
    public ProjetResponse() {
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutProjet getStatut() {
        return statut;
    }

    public void setStatut(StatutProjet statut) {
        this.statut = statut;
    }

    public BigDecimal getBudgetEstime() {
        return budgetEstime;
    }

    public void setBudgetEstime(BigDecimal budgetEstime) {
        this.budgetEstime = budgetEstime;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Integer getNiveauAvancement() {
        return niveauAvancement;
    }

    public void setNiveauAvancement(Integer niveauAvancement) {
        this.niveauAvancement = niveauAvancement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DomaineRechercheResponse getDomaine() {
        return domaine;
    }

    public void setDomaine(DomaineRechercheResponse domaine) {
        this.domaine = domaine;
    }

    public UserResponse getResponsable() {
        return responsable;
    }

    public void setResponsable(UserResponse responsable) {
        this.responsable = responsable;
    }

    public List<UserResponse> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserResponse> participants) {
        this.participants = participants;
    }
}