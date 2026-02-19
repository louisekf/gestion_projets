package sn.esmt.gestionprojets.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gestionprojets.mapper.UserMapper;
import sn.esmt.gestionprojets.dto.request.UserRegisterRequest;
import sn.esmt.gestionprojets.dto.request.UserUpdateRequest;
import sn.esmt.gestionprojets.dto.response.UserResponse;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.service.UserService;

import java.util.List;

/**
 * API REST pour la gestion des utilisateurs.
 * Accessible uniquement par MANAGER et ADMIN.
 */
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * GET /api/users - Liste tous les utilisateurs.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponse> response = userMapper.toResponseList(users);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/{id} - Détails d'un utilisateur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/users - Créer un nouvel utilisateur (ADMIN uniquement).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = userMapper.toEntity(request);
        User created = userService.register(user);
        UserResponse response = userMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/users/{id} - Mettre à jour un utilisateur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        User user = new User();
        userMapper.updateEntityFromRequest(user, request);
        User updated = userService.update(id, user);
        UserResponse response = userMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/users/{id}/role - Changer le rôle d'un utilisateur (ADMIN uniquement).
     */
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestParam Role newRole) {
        User user = userService.changeRole(id, newRole);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/users/{id}/toggle-enabled - Activer/désactiver un compte (ADMIN uniquement).
     */
    @PutMapping("/{id}/toggle-enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> toggleEnabled(@PathVariable Long id) {
        User user = userService.toggleEnabled(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/users/{id} - Supprimer un utilisateur (ADMIN uniquement).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}