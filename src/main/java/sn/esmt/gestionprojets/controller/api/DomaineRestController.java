package sn.esmt.gestionprojets.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gestionprojets.mapper.DomaineRechercheMapper;
import sn.esmt.gestionprojets.dto.request.DomaineRechercheRequest;
import sn.esmt.gestionprojets.dto.response.DomaineRechercheResponse;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.service.DomaineRechercheService;

import java.util.List;

/**
 * API REST pour la gestion des domaines de recherche.
 * Lecture accessible à tous les utilisateurs authentifiés.
 * Création/Modification/Suppression réservées à l'ADMIN.
 */
@RestController
@RequestMapping("/api/domaines")
public class DomaineRestController {

    private final DomaineRechercheService domaineService;
    private final DomaineRechercheMapper domaineMapper;

    public DomaineRestController(DomaineRechercheService domaineService,
                                 DomaineRechercheMapper domaineMapper) {
        this.domaineService = domaineService;
        this.domaineMapper = domaineMapper;
    }

    /**
     * GET /api/domaines - Liste tous les domaines.
     * Accessible à tous les utilisateurs authentifiés (pour les formulaires).
     */
    @GetMapping
    public ResponseEntity<List<DomaineRechercheResponse>> getAllDomaines() {
        List<DomaineRecherche> domaines = domaineService.findAll();
        List<DomaineRechercheResponse> response = domaineMapper.toResponseList(domaines);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/domaines/{id} - Détails d'un domaine.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DomaineRechercheResponse> getDomaineById(@PathVariable Long id) {
        DomaineRecherche domaine = domaineService.findById(id);
        DomaineRechercheResponse response = domaineMapper.toResponse(domaine);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/domaines - Créer un nouveau domaine (ADMIN uniquement).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DomaineRechercheResponse> createDomaine(
            @Valid @RequestBody DomaineRechercheRequest request) {
        DomaineRecherche domaine = domaineMapper.toEntity(request);
        DomaineRecherche created = domaineService.create(domaine);
        DomaineRechercheResponse response = domaineMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/domaines/{id} - Mettre à jour un domaine (ADMIN uniquement).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DomaineRechercheResponse> updateDomaine(
            @PathVariable Long id,
            @Valid @RequestBody DomaineRechercheRequest request) {
        DomaineRecherche domaine = domaineMapper.toEntity(request);
        DomaineRecherche updated = domaineService.update(id, domaine);
        DomaineRechercheResponse response = domaineMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/domaines/{id} - Supprimer un domaine (ADMIN uniquement).
     * Échoue si des projets sont associés.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDomaine(@PathVariable Long id) {
        domaineService.delete(id);
    }
}