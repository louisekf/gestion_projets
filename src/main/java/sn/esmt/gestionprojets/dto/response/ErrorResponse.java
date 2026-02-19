package sn.esmt.gestionprojets.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
