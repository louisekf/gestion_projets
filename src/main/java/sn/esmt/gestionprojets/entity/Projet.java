package sn.esmt.gestionprojets.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------------
    // Informations principales
    // -------------------------------------------------------

    @NotBlank(message = "Le titre du projet est obligatoire")
    @Size(min = 5, max = 200, message = "Le titre doit avoir entre 5 et 200 caractères")
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
    @Builder.Default
    private StatutProjet statut = StatutProjet.EN_COURS;


    @DecimalMin(value = "0.0", message = "Le budget ne peut pas être négatif")
    private BigDecimal budgetEstime;

    @Size(max = 200)
    @Column(length = 200)
    private String institution;


    @Min(value = 0, message = "Le niveau d'avancement ne peut pas être inférieur à 0")
    @Max(value = 100, message = "Le niveau d'avancement ne peut pas dépasser 100")
    @Column(nullable = false)
    @Builder.Default
    private Integer niveauAvancement = 0;

    // -------------------------------------------------------
    // Audit
    // -------------------------------------------------------

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    // -------------------------------------------------------
    // Relations
    // -------------------------------------------------------

    @NotNull(message = "Le domaine de recherche est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "domaine_id", nullable = false)
    private DomaineRecherche domaine;


    @NotNull(message = "Le responsable est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "responsable_id", nullable = false)
    private User responsable;

    /**
     * Participants au projet (relation Many-to-Many).
     *
     * La table de jointure "projet_participants" contient :
     *  - projet_id (FK vers projets)
     *  - user_id   (FK vers users)
     *
     * FetchType.LAZY : on ne charge les participants que si on en a besoin
     * (évite des jointures inutiles lors des listes).
     *
     * CascadeType : AUCUN cascade → supprimer un projet ne supprime pas les users.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projet_participants",
            joinColumns = @JoinColumn(name = "projet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> participants = new ArrayList<>();

    // -------------------------------------------------------
    // Hooks JPA
    // -------------------------------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // -------------------------------------------------------
    // Méthodes utilitaires
    // -------------------------------------------------------

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

    /**
     * Vérifie si un utilisateur est responsable ou participant.
     * Utile dans le service pour vérifier les droits d'un USER.
     */
    public boolean implique(User user) {
        return this.responsable.equals(user) || this.participants.contains(user);
    }
}

