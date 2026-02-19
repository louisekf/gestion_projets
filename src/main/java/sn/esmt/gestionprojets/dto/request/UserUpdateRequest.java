package sn.esmt.gestionprojets.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO pour la mise à jour du profil utilisateur.
 */
public class UserUpdateRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Pattern(regexp = "^[0-9+\\s()-]{7,20}$", message = "Format de téléphone invalide")
    private String telephone;

    private String institution;

    // Constructeur vide
    public UserUpdateRequest() {
    }

    // Constructeur avec tous les paramètres
    public UserUpdateRequest(String email, String nom, String prenom, String telephone, String institution) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.institution = institution;
    }

    // Getters et Setters
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
}