package sn.esmt.gestionprojets.dto.response;


/**
 * DTO de réponse pour un domaine de recherche.
 */

public class DomaineRechercheResponse {

    private Long id;
    private String nom;
    private String code;
    private String description;
    private Integer nombreProjets;

    public DomaineRechercheResponse() {
    }

    public DomaineRechercheResponse(Long id, String nom, String code, String description, Integer nombreProjets) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.description = description;
        this.nombreProjets = nombreProjets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getNombreProjets() {
        return nombreProjets;
    }

    public void setNombreProjets(Integer nombreProjets) {
        this.nombreProjets = nombreProjets;
    }
}
