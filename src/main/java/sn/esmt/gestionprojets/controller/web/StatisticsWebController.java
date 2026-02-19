package sn.esmt.gestionprojets.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.esmt.gestionprojets.dto.StatisticsDTO;
import sn.esmt.gestionprojets.service.StatisticsService;

/**
 * Controller web pour l'affichage des statistiques (MANAGER/ADMIN uniquement).
 */
@Controller
@RequestMapping("/statistics")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class StatisticsWebController {

    private final StatisticsService statisticsService;

    public StatisticsWebController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * GET /statistics - Tableau de bord des statistiques.
     */
    @GetMapping
    public String showStatistics(Model model) {
        StatisticsDTO stats = statisticsService.computeAll();

        model.addAttribute("stats", stats);
        model.addAttribute("totalProjets", stats.getTotalProjets());
        model.addAttribute("tauxMoyenAvancement", stats.getTauxMoyenAvancement());
        model.addAttribute("projetsEnCours", stats.getProjetsEnCours());
        model.addAttribute("projetsTermines", stats.getProjetsTermines());
        model.addAttribute("projetsSuspendus", stats.getProjetsSuspendus());

        // Données pour les graphiques (seront utilisées par Chart.js)
        model.addAttribute("projetsByDomaine", stats.getProjetsByDomaine());
        model.addAttribute("projetsByStatut", stats.getProjetsByStatut());
        model.addAttribute("projetsByYear", stats.getProjetsByYear());
        model.addAttribute("chargeByParticipant", stats.getChargeByParticipant());
        model.addAttribute("budgetByDomaine", stats.getBudgetByDomaine());

        return "statistics/dashboard";  // templates/statistics/dashboard.html
    }
}