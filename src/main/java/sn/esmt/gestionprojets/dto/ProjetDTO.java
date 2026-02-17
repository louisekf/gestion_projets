package sn.esmt.gestionprojets.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import sn.esmt.gestionprojets.entity.StatutProjet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetDTO {

    // Non fourni lors de la création, rempli lors de l'édition
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 5, max = 200)
    private String titre;

    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull(message = "Le statut est obligatoire")
    private StatutProjet statut;

    @DecimalMin(value = "0.0", message = "Le budget ne peut pas être négatif")
    private BigDecimal budgetEstime;

    @Size(max = 200)
    private String institution;

    @Min(0) @Max(100)
    private Integer niveauAvancement;

    // L'ID du domaine sélectionné dans le formulaire
    @NotNull(message = "Le domaine est obligatoire")
    private Long domaineId;

    // IDs des participants sélectionnés (champ multi-sélection dans le formulaire)
    private List<Long> participantIds;
}
