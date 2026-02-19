package sn.esmt.gestionprojets.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création/modification d'un domaine de recherche.
 */
public class DomaineRechercheRequest {

    @NotBlank(message = "Le nom du domaine est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 20)
    private String code;

    @Size(max = 500)
    private String description;

    // Constructeur vide (obligatoire pour la désérialisation JSON)
    public DomaineRechercheRequest() {
    }

    // Constructeur pratique (optionnel)
    public DomaineRechercheRequest(String nom, String code, String description) {
        this.nom = nom;
        this.code = code;
        this.description = description;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}