package sn.esmt.gestionprojets.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.exceptions.BusinessException;
import sn.esmt.gestionprojets.exceptions.ResourceNotFoundException;
import sn.esmt.gestionprojets.repository.DomaineRechercheRepository;
import sn.esmt.gestionprojets.service.DomaineRechercheService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DomaineRechercheServiceImpl implements DomaineRechercheService {

    private final DomaineRechercheRepository domaineRepository;

    @Override
    public List<DomaineRecherche> findAll() {
        return domaineRepository.findAll();
    }

    @Override
    public DomaineRecherche findById(Long id) {
        return domaineRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.domaine(id));
    }

    @Override
    public DomaineRecherche findByCode(String code) {
        return domaineRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Domaine introuvable avec le code : " + code));
    }

    @Override
    public DomaineRecherche create(DomaineRecherche domaine) {
        if (domaineRepository.existsByCode(domaine.getCode())) {
            throw BusinessException.codeAlreadyExists(domaine.getCode());
        }
        if (domaineRepository.existsByNom(domaine.getNom())) {
            throw new BusinessException("Le nom '" + domaine.getNom() + "' est déjà utilisé.");
        }

        domaine.setCode(domaine.getCode().toUpperCase().trim());

        DomaineRecherche saved = domaineRepository.save(domaine);
        log.info("Domaine créé : {} ({})", saved.getNom(), saved.getCode());
        return saved;
    }

    @Override
    public DomaineRecherche update(Long id, DomaineRecherche updated) {
        DomaineRecherche existing = findById(id);

        if (!existing.getCode().equals(updated.getCode())
                && domaineRepository.existsByCode(updated.getCode())) {
            throw BusinessException.codeAlreadyExists(updated.getCode());
        }

        if (!existing.getNom().equals(updated.getNom())
                && domaineRepository.existsByNom(updated.getNom())) {
            throw new BusinessException("Le nom '" + updated.getNom() + "' est déjà utilisé.");
        }

        existing.setCode(updated.getCode().toUpperCase().trim());
        existing.setNom(updated.getNom());
        existing.setDescription(updated.getDescription());

        log.info("Domaine mis à jour : {} ({})", existing.getNom(), existing.getCode());
        return domaineRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        DomaineRecherche domaine = findById(id);

        if (!domaine.getProjets().isEmpty()) {
            throw BusinessException.cannotDeleteDomaine(domaine.getNom(), domaine.getProjets().size());
        }

        domaineRepository.delete(domaine);
        log.info("Domaine supprimé : {} ({})", domaine.getNom(), domaine.getCode());
    }

    @Override
    public boolean codeExists(String code) {
        return domaineRepository.existsByCode(code.toUpperCase().trim());
    }
}
