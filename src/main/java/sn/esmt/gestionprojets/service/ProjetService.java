package sn.esmt.gestionprojets.service;

import sn.esmt.gestionprojets.dto.ProjetDTO;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.User;

import java.util.List;

public interface ProjetService {

    List<Projet> findVisibleProjects();

    Projet findById(Long id);

    Projet create(ProjetDTO dto);

    Projet update(Long id, ProjetDTO dto);

    void delete(Long id);

    Projet addParticipant(Long projectId, Long userId);

    Projet removeParticipant(Long projectId, Long userId);
}
