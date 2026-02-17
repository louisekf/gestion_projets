package sn.esmt.gestionprojets.service;

import sn.esmt.gestionprojets.entity.DomaineRecherche;

import java.util.List;

public interface DomaineRechercheService {
    List<DomaineRecherche> findAll();

    DomaineRecherche findById(Long id);

    DomaineRecherche findByCode(String code);

    DomaineRecherche create(DomaineRecherche domaine);

    DomaineRecherche update(Long id, DomaineRecherche domaine);

    /** Supprime un domaine (ADMIN uniquement, uniquement si aucun projet associé). */
    void delete(Long id);

    boolean codeExists(String code);
}
