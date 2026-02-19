package sn.esmt.gestionprojets.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gestionprojets.mapper.UserMapper;
import sn.esmt.gestionprojets.dto.request.UserRegisterRequest;
import sn.esmt.gestionprojets.dto.response.UserResponse;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.security.UserPrincipal;
import sn.esmt.gestionprojets.service.UserService;

/**
 * API REST pour l'authentification.
 * Endpoints publics pour l'inscription et la récupération du profil.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * POST /api/auth/register - Inscription d'un nouvel utilisateur.
     * Public (pas besoin d'être connecté).
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userMapper.toEntity(request);
        User registered = userService.register(user);
        UserResponse response = userMapper.toResponse(registered);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/auth/profile - Récupère le profil de l'utilisateur connecté.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }
}