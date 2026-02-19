package sn.esmt.gestionprojets.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création/modification d'un domaine de recherche.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomaineRechercheRequest {

    @NotBlank(message = "Le nom du domaine est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 20)
    private String code;

    @Size(max = 500)
    private String description;
}
