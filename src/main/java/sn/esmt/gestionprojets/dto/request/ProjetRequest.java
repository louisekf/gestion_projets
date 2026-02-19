package sn.esmt.gestionprojets.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.esmt.gestionprojets.entity.StatutProjet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO pour la création/modification d'un projet.
 * Remplace l'ancien ProjectDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 5, max = 200, message = "Le titre doit avoir entre 5 et 200 caractères")
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

    @NotNull(message = "Le domaine est obligatoire")
    private Long domaineId;

    // IDs des participants (MANAGER/ADMIN uniquement)
    private List<Long> participantIds;
}
