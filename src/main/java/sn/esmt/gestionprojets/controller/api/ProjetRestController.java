package sn.esmt.gestionprojets.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gestionprojets.mapper.ProjetMapper;
import sn.esmt.gestionprojets.dto.request.ProjetRequest;
import sn.esmt.gestionprojets.dto.response.ProjetResponse;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.service.ProjetService;

import java.util.List;

/**
 * API REST pour la gestion des projets.
 * Toutes les exceptions sont gérées par GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/projets")  // ✅ Changé ici !
public class ProjetRestController {

    private final ProjetService projetService;
    private final ProjetMapper projetMapper;

    public ProjetRestController(ProjetService projetService, ProjetMapper projetMapper) {
        this.projetService = projetService;
        this.projetMapper = projetMapper;
    }

    // Le reste du code reste identique...
    @GetMapping
    public ResponseEntity<List<ProjetResponse>> getAllProjets() {
        List<Projet> projets = projetService.findVisibleProjects();
        List<ProjetResponse> response = projetMapper.toResponseList(projets);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponse> getProjetById(@PathVariable Long id) {
        Projet projet = projetService.findById(id);
        ProjetResponse response = projetMapper.toResponse(projet);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProjetResponse> createProjet(@Valid @RequestBody ProjetRequest request) {
        Projet projet = projetService.create(request);
        ProjetResponse response = projetMapper.toResponse(projet);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetResponse> updateProjet(
            @PathVariable Long id,
            @Valid @RequestBody ProjetRequest request) {
        Projet projet = projetService.update(id, request);
        ProjetResponse response = projetMapper.toResponse(projet);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjet(@PathVariable Long id) {
        projetService.delete(id);
    }

    @PostMapping("/{id}/participants/{userId}")
    public ResponseEntity<ProjetResponse> addParticipant(
            @PathVariable Long id,
            @PathVariable Long userId) {
        Projet projet = projetService.addParticipant(id, userId);
        ProjetResponse response = projetMapper.toResponse(projet);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/participants/{userId}")
    public ResponseEntity<ProjetResponse> removeParticipant(
            @PathVariable Long id,
            @PathVariable Long userId) {
        Projet projet = projetService.removeParticipant(id, userId);
        ProjetResponse response = projetMapper.toResponse(projet);
        return ResponseEntity.ok(response);
    }
}