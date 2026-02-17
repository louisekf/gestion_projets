package sn.esmt.gestionprojets.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "domaines_recherche")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    // -------------------------------------------------------
    // Relations
    // -------------------------------------------------------

    /**
     * Projets appartenant à ce domaine.
     * mappedBy = "domaine" : la FK est du côté Project.
     */
    @OneToMany(mappedBy = "domaine", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Projet> projets = new ArrayList<>();
}
