package sn.esmt.gestionprojets.mapper;

import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.dto.response.ProjetResponse;
import sn.esmt.gestionprojets.entity.Projet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour Project ↔ DTOs.
 */
@Component
public class ProjetMapper {

    private final UserMapper userMapper;
    private final DomaineRechercheMapper domaineMapper;

    // Constructeur explicite
    public ProjetMapper(UserMapper userMapper, DomaineRechercheMapper domaineMapper) {
        this.userMapper = userMapper;
        this.domaineMapper = domaineMapper;
    }

    /**
     * Convertit Project → ProjectResponse (avec relations complètes).
     */
    public ProjetResponse toResponse(Projet project) {
        if (project == null) return null;

        // Création de la réponse avec les setters (plus de builder)
        ProjetResponse response = new ProjetResponse();
        response.setId(project.getId());
        response.setTitre(project.getTitre());
        response.setDescription(project.getDescription());
        response.setDateDebut(project.getDateDebut());
        response.setDateFin(project.getDateFin());
        response.setStatut(project.getStatut());
        response.setBudgetEstime(project.getBudgetEstime());
        response.setInstitution(project.getInstitution());
        response.setNiveauAvancement(project.getNiveauAvancement());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        // Relations (via les autres mappers)
        response.setDomaine(domaineMapper.toResponse(project.getDomaine()));
        response.setResponsable(userMapper.toResponse(project.getResponsable()));
        response.setParticipants(userMapper.toResponseList(project.getParticipants()));

        return response;
    }

    public List<ProjetResponse> toResponseList(List<Projet> projects) {
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertit ProjectRequest → Project (création).
     */
    public Projet toEntity(ProjetRequest request) {
        if (request == null) return null;

        Projet projet = new Projet();
        projet.setTitre(request.getTitre());
        projet.setDescription(request.getDescription());
        projet.setDateDebut(request.getDateDebut());
        projet.setDateFin(request.getDateFin());
        projet.setStatut(request.getStatut());
        projet.setBudgetEstime(request.getBudgetEstime());
        projet.setInstitution(request.getInstitution());
        projet.setNiveauAvancement(request.getNiveauAvancement() != null ? request.getNiveauAvancement() : 0);

        return projet;
    }

    /**
     * Met à jour un Project existant avec les données du ProjectRequest.
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