package sn.esmt.gestionprojets.mapper;

import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.dto.request.UserRegisterRequest;
import sn.esmt.gestionprojets.dto.request.UserUpdateRequest;
import sn.esmt.gestionprojets.dto.response.UserResponse;
import sn.esmt.gestionprojets.entity.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir User ↔ DTOs.
 *
 * Pourquoi ne pas utiliser MapStruct ?
 * → Pour un projet académique, du code manuel est plus pédagogique.
 * → MapStruct ajoute une dépendance et de la configuration supplémentaire.
 * → On garde le contrôle total de chaque conversion.
 */
@Component
public class UserMapper {

    /**
     * Convertit une entité User en UserResponse (pour l'API / templates).
     * N'expose JAMAIS le mot de passe.
     */
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .nomComplet(user.getNomComplet())
                .telephone(user.getTelephone())
                .institution(user.getInstitution())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * Convertit une liste d'entités en liste de DTOs.
     */
    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un UserRegisterRequest en entité User (pour la création).
     * Le mot de passe sera encodé dans le service.
     */
    public User toEntity(UserRegisterRequest request) {
        if (request == null) return null;

        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) // Sera encodé par le service
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .institution(request.getInstitution())
                .role(request.getRole()) // Peut être null → USER par défaut
                .build();
    }

    /**
     * Met à jour une entité User existante avec les données d'un UserUpdateRequest.
     * Ne touche PAS au mot de passe ni au rôle.
     */
    public void updateEntityFromRequest(User user, UserUpdateRequest request) {
        if (user == null || request == null) return;

        user.setEmail(request.getEmail());
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTelephone(request.getTelephone());
        user.setInstitution(request.getInstitution());
    }
}