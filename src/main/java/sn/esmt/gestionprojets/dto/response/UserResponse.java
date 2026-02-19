package sn.esmt.gestionprojets.dto.response;

import sn.esmt.gestionprojets.entity.enums.Role;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un utilisateur.
 */
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String telephone;
    private String institution;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;

    // Constructeur vide
    public UserResponse() {
    }

    // Constructeur avec tous les paramètres
    public UserResponse(Long id, String username, String email, String nom, String prenom,
                        String nomComplet, String telephone, String institution, Role role,
                        boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.nomComplet = nomComplet;
        this.telephone = telephone;
        this.institution = institution;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}