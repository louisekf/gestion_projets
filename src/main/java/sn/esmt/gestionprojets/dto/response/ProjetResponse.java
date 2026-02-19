package sn.esmt.gestionprojets.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.esmt.gestionprojets.entity.StatutProjet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetResponse {

    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutProjet statut;
    private BigDecimal budgetEstime;
    private String institution;
    private Integer niveauAvancement;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relations (informations simplifiées)
    private DomaineRechercheResponse domaine;
    private UserResponse responsable;
    private List<UserResponse> participants;
}
