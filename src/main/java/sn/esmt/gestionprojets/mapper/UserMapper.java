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
 */
@Component
public class UserMapper {

    /**
     * Convertit une entité User en UserResponse.
     */
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setNomComplet(user.getNomComplet());
        response.setTelephone(user.getTelephone());
        response.setInstitution(user.getInstitution());
        response.setRole(user.getRole());
        response.setEnabled(user.isEnabled());
        response.setCreatedAt(user.getCreatedAt());

        return response;
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
     * Convertit un UserRegisterRequest en entité User.
     */
    public User toEntity(UserRegisterRequest request) {
        if (request == null) return null;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Sera encodé par le service
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setTelephone(request.getTelephone());
        user.setInstitution(request.getInstitution());
        user.setRole(request.getRole()); // Peut être null → USER par défaut

        return user;
    }

    /**
     * Met à jour une entité User existante.
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