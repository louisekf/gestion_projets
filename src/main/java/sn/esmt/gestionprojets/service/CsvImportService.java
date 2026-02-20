package sn.esmt.gestionprojets.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.entity.enums.StatutProjet;
import sn.esmt.gestionprojets.repository.DomaineRechercheRepository;
import sn.esmt.gestionprojets.repository.ProjetRepository;
import sn.esmt.gestionprojets.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service d'import de projets depuis un fichier CSV.
 *
 * Format attendu (séparateur point-virgule) :
 * titre;description;domaine_code;statut;date_debut;date_fin;budget_estime;institution;niveau_avancement;responsable_email
 */
@Service
public class CsvImportService {

    private static final String CSV_SEPARATOR = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ProjetRepository projetRepository;
    private final DomaineRechercheRepository domaineRepository;
    private final UserRepository userRepository;

    public CsvImportService(ProjetRepository projetRepository,
                            DomaineRechercheRepository domaineRepository,
                            UserRepository userRepository) {
        this.projetRepository = projetRepository;
        this.domaineRepository = domaineRepository;
        this.userRepository = userRepository;
    }

    /**
     * Résultat de l'import CSV.
     */
    public static class ImportResult {
        private int totalLines;
        private int successCount;
        private int errorCount;
        private List<String> errors = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();

        public int getTotalLines()    { return totalLines; }
        public void setTotalLines(int t) { this.totalLines = t; }
        public int getSuccessCount()  { return successCount; }
        public void incrementSuccess() { this.successCount++; }
        public int getErrorCount()    { return errorCount; }
        public void incrementError()  { this.errorCount++; }
        public List<String> getErrors()   { return errors; }
        public List<String> getWarnings() { return warnings; }
        public void addError(String e)    { errors.add(e); errorCount++; }
        public void addWarning(String w)  { warnings.add(w); }

        public String getSummary() {
            return successCount + " projet(s) importé(s) avec succès, "
                    + errorCount + " erreur(s) sur " + totalLines + " ligne(s) traitée(s).";
        }
    }

    /**
     * Importe des projets depuis un fichier CSV multipart.
     *
     * @param file          le fichier CSV uploadé
     * @param skipErrors    si true, continue l'import en cas d'erreur sur une ligne
     * @param updateExisting si true, met à jour les projets dont le titre existe déjà
     */
    public ImportResult importFromCsv(MultipartFile file, boolean skipErrors, boolean updateExisting)
            throws IOException {

        ImportResult result = new ImportResult();

        if (file == null || file.isEmpty()) {
            result.addError("Fichier vide ou non fourni.");
            return result;
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".csv") && !filename.endsWith(".CSV"))) {
            result.addError("Format de fichier invalide. Seuls les fichiers .csv sont acceptés.");
            return result;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                result.addError("Fichier vide - aucun en-tête trouvé.");
                return result;
            }

            // Remove BOM if present
            headerLine = headerLine.replace("\uFEFF", "").trim();
            String[] headers = headerLine.split(CSV_SEPARATOR, -1);
            CsvHeader csvHeader = parseCsvHeader(headers);

            if (csvHeader.titreIndex < 0) {
                result.addError("Colonne 'titre' manquante dans l'en-tête du CSV.");
                return result;
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;

                result.setTotalLines(result.getTotalLines() + 1);

                try {
                    processLine(line, csvHeader, lineNumber, updateExisting, result);
                    result.incrementSuccess();
                } catch (Exception e) {
                    String errMsg = "Ligne " + lineNumber + " : " + e.getMessage();
                    if (skipErrors) {
                        result.addError(errMsg);
                    } else {
                        result.addError(errMsg);
                        return result; // Stop on first error if not skipErrors
                    }
                }
            }
        }

        return result;
    }

    private void processLine(String line, CsvHeader csvHeader, int lineNumber,
                             boolean updateExisting, ImportResult result) {

        String[] cols = line.split(CSV_SEPARATOR, -1);

        // -- titre (required)
        String titre = getCol(cols, csvHeader.titreIndex);
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Titre manquant ou vide");
        }

        // Check for existing project
        Projet projet;
        if (updateExisting) {
            projet = projetRepository.findByTitreIgnoreCase(titre)
                    .orElse(new Projet());
        } else {
            projet = new Projet();
        }

        projet.setTitre(titre);

        // -- description
        String description = getCol(cols, csvHeader.descriptionIndex);
        if (description != null && !description.isBlank()) {
            projet.setDescription(description);
        }

        // -- domaine_code
        String domaineCode = getCol(cols, csvHeader.domaineCodeIndex);
        if (domaineCode != null && !domaineCode.isBlank()) {
            DomaineRecherche domaine = (DomaineRecherche) domaineRepository.findByCodeIgnoreCase(domaineCode)
                    .orElse(null);
            if (domaine != null) {
                projet.setDomaine(domaine);
            } else {
                result.addWarning("Ligne " + lineNumber + " : domaine '" + domaineCode + "' introuvable, ignoré.");
            }
        }

        // -- statut
        String statutStr = getCol(cols, csvHeader.statutIndex);
        if (statutStr != null && !statutStr.isBlank()) {
            try {
                projet.setStatut(StatutProjet.valueOf(statutStr.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                result.addWarning("Ligne " + lineNumber + " : statut '" + statutStr + "' invalide, ignoré. " +
                        "Valeurs valides : PLANIFIE, EN_COURS, SUSPENDU, TERMINE, ANNULE.");
            }
        } else {
            projet.setStatut(StatutProjet.PLANIFIE); // default
        }

        // -- date_debut
        String dateDebutStr = getCol(cols, csvHeader.dateDebutIndex);
        if (dateDebutStr != null && !dateDebutStr.isBlank()) {
            try {
                projet.setDateDebut(LocalDate.parse(dateDebutStr.trim(), DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                result.addWarning("Ligne " + lineNumber + " : date_debut '" + dateDebutStr + "' invalide (format yyyy-MM-dd attendu), ignorée.");
            }
        }

        // -- date_fin
        String dateFinStr = getCol(cols, csvHeader.dateFinIndex);
        if (dateFinStr != null && !dateFinStr.isBlank()) {
            try {
                projet.setDateFin(LocalDate.parse(dateFinStr.trim(), DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                result.addWarning("Ligne " + lineNumber + " : date_fin '" + dateFinStr + "' invalide, ignorée.");
            }
        }

        // -- budget_estime
        String budgetStr = getCol(cols, csvHeader.budgetEstimeIndex);
        if (budgetStr != null && !budgetStr.isBlank()) {
            try {
                projet.setBudgetEstime(new BigDecimal(budgetStr.trim().replace(",", ".")));
            } catch (NumberFormatException e) {
                result.addWarning("Ligne " + lineNumber + " : budget_estime invalide '" + budgetStr + "', ignoré.");
            }
        }

        // -- institution
        String institution = getCol(cols, csvHeader.institutionIndex);
        if (institution != null && !institution.isBlank()) {
            projet.setInstitution(institution);
        }

        // -- niveau_avancement
        String avancementStr = getCol(cols, csvHeader.niveauAvancementIndex);
        if (avancementStr != null && !avancementStr.isBlank()) {
            try {
                int avancement = Integer.parseInt(avancementStr.trim());
                if (avancement < 0 || avancement > 100) {
                    result.addWarning("Ligne " + lineNumber + " : niveau_avancement doit être entre 0 et 100, ignoré.");
                } else {
                    projet.setNiveauAvancement(avancement);
                }
            } catch (NumberFormatException e) {
                result.addWarning("Ligne " + lineNumber + " : niveau_avancement invalide '" + avancementStr + "', ignoré.");
            }
        }

        // -- responsable_email
        String responsableEmail = getCol(cols, csvHeader.responsableEmailIndex);
        if (responsableEmail != null && !responsableEmail.isBlank()) {
            User responsable = userRepository.findByEmail(responsableEmail.trim())
                    .orElse(null);
            if (responsable != null) {
                projet.setResponsable(responsable);
            } else {
                result.addWarning("Ligne " + lineNumber + " : responsable '" + responsableEmail + "' introuvable, ignoré.");
            }
        }

        projetRepository.save(projet);
    }

    // ---- Helpers ----

    private String getCol(String[] cols, int index) {
        if (index < 0 || index >= cols.length) return null;
        String val = cols[index].trim();
        // Remove surrounding quotes if present
        if (val.startsWith("\"") && val.endsWith("\"")) {
            val = val.substring(1, val.length() - 1);
        }
        return val.isEmpty() ? null : val;
    }

    private CsvHeader parseCsvHeader(String[] headers) {
        CsvHeader h = new CsvHeader();
        for (int i = 0; i < headers.length; i++) {
            String name = headers[i].trim().toLowerCase().replace("\uFEFF", "");
            switch (name) {
                case "titre"              -> h.titreIndex = i;
                case "description"        -> h.descriptionIndex = i;
                case "domaine_code"       -> h.domaineCodeIndex = i;
                case "statut"             -> h.statutIndex = i;
                case "date_debut"         -> h.dateDebutIndex = i;
                case "date_fin"           -> h.dateFinIndex = i;
                case "budget_estime"      -> h.budgetEstimeIndex = i;
                case "institution"        -> h.institutionIndex = i;
                case "niveau_avancement"  -> h.niveauAvancementIndex = i;
                case "responsable_email"  -> h.responsableEmailIndex = i;
            }
        }
        return h;
    }

    private static class CsvHeader {
        int titreIndex = -1;
        int descriptionIndex = -1;
        int domaineCodeIndex = -1;
        int statutIndex = -1;
        int dateDebutIndex = -1;
        int dateFinIndex = -1;
        int budgetEstimeIndex = -1;
        int institutionIndex = -1;
        int niveauAvancementIndex = -1;
        int responsableEmailIndex = -1;
    }
}
