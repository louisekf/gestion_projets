package sn.esmt.gestionprojets.controller.web;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.esmt.gestionprojets.service.CsvImportService;

import java.io.IOException;

/**
 * Controller pour l'import CSV de projets (ADMIN uniquement).
 */
@Controller
@RequestMapping("/admin/import")
@PreAuthorize("hasRole('ADMIN')")
public class CsvImportWebController {

    private final sn.esmt.gestionprojets.service.CsvImportService csvImportService;

    public CsvImportWebController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    /**
     * GET /admin/import - Page d'import CSV.
     */
    @GetMapping
    public String showImportPage(Model model) {
        return "admin/import";
    }

    /**
     * POST /admin/import - Traitement du fichier CSV uploadé.
     */
    @PostMapping
    public String handleImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "skipErrors",     defaultValue = "false") boolean skipErrors,
            @RequestParam(value = "updateExisting", defaultValue = "false") boolean updateExisting,
            RedirectAttributes redirectAttributes) {

        try {
            CsvImportService.ImportResult result =
                    csvImportService.importFromCsv(file, skipErrors, updateExisting);

            if (result.getSuccessCount() > 0) {
                redirectAttributes.addFlashAttribute("importSuccess", result.getSummary());
            }

            if (!result.getErrors().isEmpty() || !result.getWarnings().isEmpty()) {
                // Combine errors and warnings for display
                java.util.List<String> allMessages = new java.util.ArrayList<>();
                allMessages.addAll(result.getErrors());
                allMessages.addAll(result.getWarnings());
                redirectAttributes.addFlashAttribute("importErrors", allMessages);
            }

            if (result.getSuccessCount() == 0 && result.getErrors().isEmpty()) {
                redirectAttributes.addFlashAttribute("importErrors",
                        java.util.List.of("Aucune ligne valide trouvée dans le fichier."));
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("importErrors",
                    java.util.List.of("Erreur de lecture du fichier : " + e.getMessage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("importErrors",
                    java.util.List.of("Erreur inattendue : " + e.getMessage()));
        }

        return "redirect:/admin/import";
    }
}
