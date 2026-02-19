package sn.esmt.gestionprojets.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "domaines_recherche")
public class DomaineRecherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du domaine est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "domaine", fetch = FetchType.LAZY)
    private List<Projet> projets = new ArrayList<>();

    // Constructeurs
    public DomaineRecherche() {
    }

    public DomaineRecherche(String nom, String code, String description) {
        this.nom = nom;
        this.code = code;
        this.description = description;
    }

    // Getters et Setters
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

    public List<Projet> getProjets() {
        return projets;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomaineRecherche that = (DomaineRecherche) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DomaineRecherche{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
