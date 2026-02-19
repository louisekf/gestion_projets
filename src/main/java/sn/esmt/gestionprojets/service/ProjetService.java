package sn.esmt.gestionprojets.service;

import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.entity.Projet;

import java.util.List;

public interface ProjetService {

    List<Projet> findVisibleProjects();

    Projet findById(Long id);

    Projet create(ProjetRequest dto);

    Projet update(Long id, ProjetRequest dto);

    void delete(Long id);

    Projet addParticipant(Long projectId, Long userId);

    Projet removeParticipant(Long projectId, Long userId);
}
