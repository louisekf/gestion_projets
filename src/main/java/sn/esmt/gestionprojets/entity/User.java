package sn.esmt.gestionprojets.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------------
    // Identification
    // -------------------------------------------------------

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(nullable = false)
    private String password;

    // -------------------------------------------------------
    // Informations personnelles
    // -------------------------------------------------------

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 20)
    private String telephone;

    @Size(max = 200)
    @Column(length = 200)
    private String institution;

    // -------------------------------------------------------
    // Rôle et statut
    // -------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    // -------------------------------------------------------
    // Audit
    // -------------------------------------------------------

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // -------------------------------------------------------
    // Relations
    // -------------------------------------------------------

    @OneToMany(mappedBy = "responsable", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Projet> projetsResponsable = new ArrayList<>();

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Projet> projetsParticipant = new ArrayList<>();

    // -------------------------------------------------------
    // Hooks JPA
    // -------------------------------------------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    public boolean isManagerOrAdmin() {
        return this.role == Role.MANAGER || this.role == Role.ADMIN;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
