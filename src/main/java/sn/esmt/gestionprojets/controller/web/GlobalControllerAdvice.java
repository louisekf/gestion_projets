package sn.esmt.gestionprojets.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injecte automatiquement l'URI courante dans le modèle de toutes les vues.
 * Utilisé par le layout Thymeleaf pour marquer le lien actif dans la sidebar.
 *
 * Remplace l'usage de #httpServletRequest.requestURI (supprimé en Thymeleaf 3.1+)
 * et de #request.requestURI (instable selon le contexte de fragment).
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}