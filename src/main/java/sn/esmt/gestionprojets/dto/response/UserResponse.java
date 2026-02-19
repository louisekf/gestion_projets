package sn.esmt.gestionprojets.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.esmt.gestionprojets.entity.Role;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un utilisateur.
 * Utilisé dans les API REST et les templates Thymeleaf.
 *
 * N'expose JAMAIS le mot de passe (sécurité).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String nom;
    private String prenom;
    private String nomComplet; // prenom + nom
    private String telephone;
    private String institution;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;

}
