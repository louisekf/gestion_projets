package sn.esmt.gestionprojets.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.dto.response.ProjetResponse;
import sn.esmt.gestionprojets.entity.Projet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour Project ↔ DTOs.
 * Injecte les autres mappers pour gérer les relations.
 */
@Component
@RequiredArgsConstructor
public class ProjetMapper {

    private final UserMapper userMapper;
    private final DomaineRechercheMapper domaineMapper;

    /**
     * Convertit Project → ProjectResponse (avec relations complètes).
     */
    public ProjetResponse toResponse(Projet project) {
        if (project == null) return null;

        return ProjetResponse.builder()
                .id(project.getId())
                .titre(project.getTitre())
                .description(project.getDescription())
                .dateDebut(project.getDateDebut())
                .dateFin(project.getDateFin())
                .statut(project.getStatut())
                .budgetEstime(project.getBudgetEstime())
                .institution(project.getInstitution())
                .niveauAvancement(project.getNiveauAvancement())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                // Relations
                .domaine(domaineMapper.toResponse(project.getDomaine()))
                .responsable(userMapper.toResponse(project.getResponsable()))
                .participants(userMapper.toResponseList(project.getParticipants()))
                .build();
    }

    public List<ProjetResponse> toResponseList(List<Projet> projects) {
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertit ProjectRequest → Project (création).
     * Les relations (domaine, responsable, participants) sont affectées dans le service.
     */
    public Projet toEntity(ProjetRequest request) {
        if (request == null) return null;

        return Projet.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .statut(request.getStatut())
                .budgetEstime(request.getBudgetEstime())
                .institution(request.getInstitution())
                .niveauAvancement(request.getNiveauAvancement() != null ? request.getNiveauAvancement() : 0)
                .build();
    }

    /**
     * Met à jour un Project existant avec les données du ProjectRequest.
     * Les relations sont gérées dans le service.
     */
    public void updateEntityFromRequest(Projet project, ProjetRequest request) {
        if (project == null || request == null) return;

        project.setTitre(request.getTitre());
        project.setDescription(request.getDescription());
        project.setDateDebut(request.getDateDebut());
        project.setDateFin(request.getDateFin());
        project.setStatut(request.getStatut());
        project.setBudgetEstime(request.getBudgetEstime());
        project.setInstitution(request.getInstitution());
        project.setNiveauAvancement(request.getNiveauAvancement());
    }

}
