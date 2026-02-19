package sn.esmt.gestionprojets.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.esmt.gestionprojets.dto.StatisticsDTO;
import sn.esmt.gestionprojets.service.StatisticsService;

/**
 * API REST pour les statistiques.
 * Accessible uniquement par MANAGER et ADMIN.
 */
@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class StatisticsRestController {

    private final StatisticsService statisticsService;

    public StatisticsRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * GET /api/statistics - Retourne toutes les statistiques.
     */
    @GetMapping
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO stats = statisticsService.computeAll();
        return ResponseEntity.ok(stats);
    }
}