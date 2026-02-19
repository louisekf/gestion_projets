package sn.esmt.gestionprojets.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre du projet est obligatoire")
    @Size(min = 5, max = 200)
    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutProjet statut = StatutProjet.EN_COURS;

    @DecimalMin(value = "0.0", message = "Le budget ne peut pas être négatif")
    @Digits(integer = 15, fraction = 2)
    @Column(precision = 15, scale = 2)
    private BigDecimal budgetEstime;

    @Size(max = 200)
    @Column(length = 200)
    private String institution;

    @Min(value = 0, message = "Le niveau d'avancement ne peut pas être inférieur à 0")
    @Max(value = 100, message = "Le niveau d'avancement ne peut pas dépasser 100")
    @Column(nullable = false)
    private Integer niveauAvancement = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @NotNull(message = "Le domaine de recherche est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "domaine_id", nullable = false)
    private DomaineRecherche domaine;

    @NotNull(message = "Le responsable est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "responsable_id", nullable = false)
    private User responsable;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projet_participants",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    // Constructeurs
    public Projet() {
    }

    public Projet(String titre, LocalDate dateDebut, StatutProjet statut, DomaineRecherche domaine, User responsable) {
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.statut = statut;
        this.domaine = domaine;
        this.responsable = responsable;
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

    public DomaineRecherche getDomaine() {
        return domaine;
    }

    public void setDomaine(DomaineRecherche domaine) {
        this.domaine = domaine;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    // Hooks JPA
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Méthodes utilitaires
    public void addParticipant(User user) {
        if (!this.participants.contains(user)) {
            this.participants.add(user);
            user.getProjetsParticipant().add(this);
        }
    }

    public void removeParticipant(User user) {
        this.participants.remove(user);
        user.getProjetsParticipant().remove(this);
    }

    public boolean hasParticipant(User user) {
        return this.participants.contains(user);
    }

    public boolean implique(User user) {
        return this.responsable.equals(user) || this.participants.contains(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projet projet = (Projet) o;
        return Objects.equals(id, projet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", statut=" + statut +
                ", niveauAvancement=" + niveauAvancement +
                '}';
    }
}
