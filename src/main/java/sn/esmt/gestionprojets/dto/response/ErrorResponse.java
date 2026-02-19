package sn.esmt.gestionprojets.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    /** Timestamp de l'erreur. */
    private LocalDateTime timestamp;

    /** Code HTTP (400, 404, 403, 500...). */
    private int status;

    /** Nom de l'erreur (Bad Request, Not Found...). */
    private String error;

    /** Message principal (celui de l'exception). */
    private String message;

    /** Chemin de la requête qui a échoué. */
    private String path;

    /** Détails supplémentaires (erreurs de validation par champ par exemple). */
    private List<String> details;

    // Constructeur vide
    public ErrorResponse() {
    }

    // Constructeur avec tous les paramètres
    public ErrorResponse(LocalDateTime timestamp, int status, String error,
                         String message, String path, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    // Getters et Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    // Builder pattern manuel (optionnel - peut être supprimé si vous préférez utiliser les setters)
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private List<String> details;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder details(List<String> details) {
            this.details = details;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(timestamp, status, error, message, path, details);
        }
    }
}