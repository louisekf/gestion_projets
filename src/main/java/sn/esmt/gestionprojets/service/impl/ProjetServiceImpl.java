package sn.esmt.gestionprojets.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.exceptions.ResourceNotFoundException;
import sn.esmt.gestionprojets.exceptions.UnauthorizedException;
import sn.esmt.gestionprojets.repository.DomaineRechercheRepository;
import sn.esmt.gestionprojets.repository.ProjetRepository;
import sn.esmt.gestionprojets.repository.UserRepository;
import sn.esmt.gestionprojets.security.UserPrincipal;
import sn.esmt.gestionprojets.service.ProjetService;

import java.util.List;

@Service
@Transactional
public class ProjetServiceImpl implements ProjetService {

    private static final Logger log = LoggerFactory.getLogger(ProjetServiceImpl.class);

    private final ProjetRepository projectRepository;
    private final UserRepository userRepository;
    private final DomaineRechercheRepository domaineRepository;

    public ProjetServiceImpl(ProjetRepository projectRepository,
                             UserRepository userRepository,
                             DomaineRechercheRepository domaineRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.domaineRepository = domaineRepository;
    }

    // -------------------------------------------------------
    // Récupération utilisateur connecté
    // -------------------------------------------------------

    private User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getUser();
    }

    // -------------------------------------------------------
    // Lecture
    // -------------------------------------------------------

    @Override
    public List<Projet> findVisibleProjects() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.USER) {
            log.debug("Chargement des projets pour USER : {}", currentUser.getEmail());
            return projectRepository.findAllProjectsOfUser(currentUser);
        }
        log.debug("Chargement de tous les projets pour {} : {}", currentUser.getRole(), currentUser.getEmail());
        return projectRepository.findAll();
    }

    @Override
    public Projet findById(Long id) {
        User currentUser = getCurrentUser();
        Projet project = projectRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.project(id));
        checkReadPermission(project, currentUser);
        return project;
    }

    // -------------------------------------------------------
    // Création
    // -------------------------------------------------------

    @Override
    public Projet create(ProjetRequest dto) {
        User currentUser = getCurrentUser();

        User responsable;
        if (dto.getResponsableId() != null && currentUser.isManagerOrAdmin()) {
            responsable = userRepository.findById(dto.getResponsableId())
                    .orElseThrow(() -> ResourceNotFoundException.user(dto.getResponsableId()));
        } else {
            responsable = currentUser;
        }

        DomaineRecherche domaine = domaineRepository.findById(dto.getDomaineId())
                .orElseThrow(() -> ResourceNotFoundException.domaine(dto.getDomaineId()));

        Projet project = new Projet();
        project.setTitre(dto.getTitre());
        project.setDescription(dto.getDescription());
        project.setDateDebut(dto.getDateDebut());
        project.setDateFin(dto.getDateFin());
        project.setStatut(dto.getStatut());
        project.setBudgetEstime(dto.getBudgetEstime());
        project.setInstitution(dto.getInstitution());
        project.setNiveauAvancement(dto.getNiveauAvancement() != null ? dto.getNiveauAvancement() : 0);
        project.setDomaine(domaine);
        project.setResponsable(responsable);

        if (dto.getParticipantIds() != null && currentUser.isManagerOrAdmin()) {
            List<User> participants = userRepository.findAllById(dto.getParticipantIds());
            participants.forEach(project::addParticipant);
        }

        Projet saved = projectRepository.save(project);
        log.info("Projet créé : '{}' par {}", saved.getTitre(), currentUser.getEmail());
        return saved;
    }

    // -------------------------------------------------------
    // Mise à jour
    // -------------------------------------------------------

    @Override
    public Projet update(Long id, ProjetRequest dto) {
        User currentUser = getCurrentUser();

        Projet project = projectRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.project(id));
        checkWritePermission(project, currentUser);

        project.setTitre(dto.getTitre());
        project.setDescription(dto.getDescription());
        project.setDateDebut(dto.getDateDebut());
        project.setDateFin(dto.getDateFin());
        project.setStatut(dto.getStatut());
        project.setBudgetEstime(dto.getBudgetEstime());
        project.setInstitution(dto.getInstitution());
        project.setNiveauAvancement(dto.getNiveauAvancement());

        if (dto.getDomaineId() != null) {
            DomaineRecherche domaine = domaineRepository.findById(dto.getDomaineId())
                    .orElseThrow(() -> ResourceNotFoundException.domaine(dto.getDomaineId()));
            project.setDomaine(domaine);
        }

        // Changer le responsable si MANAGER/ADMIN et responsableId fourni
        if (currentUser.isManagerOrAdmin() && dto.getResponsableId() != null) {
            User nouveauResponsable = userRepository.findById(dto.getResponsableId())
                    .orElseThrow(() -> ResourceNotFoundException.user(dto.getResponsableId()));
            project.setResponsable(nouveauResponsable);
        }

        if (currentUser.isManagerOrAdmin() && dto.getParticipantIds() != null) {
            project.getParticipants().clear();
            List<User> participants = userRepository.findAllById(dto.getParticipantIds());
            participants.forEach(project::addParticipant);
        }

        log.info("Projet '{}' mis à jour par {}", project.getTitre(), currentUser.getEmail());
        return projectRepository.save(project);
    }

    // -------------------------------------------------------
    // Suppression
    // -------------------------------------------------------

    @Override
    public void delete(Long id) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw UnauthorizedException.adminOnly();
        }
        Projet project = projectRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.project(id));
        projectRepository.delete(project);
        log.info("Projet '{}' supprimé par l'admin {}", project.getTitre(), currentUser.getEmail());
    }

    // -------------------------------------------------------
    // Participants
    // -------------------------------------------------------

    @Override
    public Projet addParticipant(Long projectId, Long userId) {
        User currentUser = getCurrentUser();
        if (!currentUser.isManagerOrAdmin()) {
            throw UnauthorizedException.managerOrAdminOnly();
        }
        Projet project = projectRepository.findById(projectId)
                .orElseThrow(() -> ResourceNotFoundException.project(projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.user(userId));
        project.addParticipant(user);
        log.info("Participant {} ajouté au projet '{}'", user.getEmail(), project.getTitre());
        return projectRepository.save(project);
    }

    @Override
    public Projet removeParticipant(Long projectId, Long userId) {
        User currentUser = getCurrentUser();
        if (!currentUser.isManagerOrAdmin()) {
            throw UnauthorizedException.managerOrAdminOnly();
        }
        Projet project = projectRepository.findById(projectId)
                .orElseThrow(() -> ResourceNotFoundException.project(projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.user(userId));
        project.removeParticipant(user);
        log.info("Participant {} retiré du projet '{}'", user.getEmail(), project.getTitre());
        return projectRepository.save(project);
    }

    // -------------------------------------------------------
    // Permissions
    // -------------------------------------------------------

    private void checkReadPermission(Projet project, User currentUser) {
        if (currentUser.getRole() == Role.USER && !project.implique(currentUser)) {
            log.warn("Accès refusé en lecture au projet {} pour {}", project.getId(), currentUser.getEmail());
            throw UnauthorizedException.cannotReadProject();
        }
    }

    private void checkWritePermission(Projet project, User currentUser) {
        if (currentUser.getRole() == Role.USER && !project.getResponsable().equals(currentUser)) {
            log.warn("Accès refusé en écriture au projet {} pour {}", project.getId(), currentUser.getEmail());
            throw UnauthorizedException.cannotModifyProject();
        }
    }
}