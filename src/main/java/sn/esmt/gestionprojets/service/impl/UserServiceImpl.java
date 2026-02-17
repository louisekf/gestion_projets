package sn.esmt.gestionprojets.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.esmt.gestionprojets.entity.Role;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.repository.UserRepository;
import sn.esmt.gestionprojets.service.UserService;

import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email : " + email));
    }

    /**
     * Enregistre un nouvel utilisateur.
     * IMPORTANT : le mot de passe est encodé en BCrypt AVANT la sauvegarde.
     * On ne stocke JAMAIS un mot de passe en clair en base de données.
     */
    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("L'email '" + user.getEmail() + "' est déjà utilisé.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur '" + user.getUsername() + "' est déjà pris.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setEnabled(true);

        User saved = userRepository.save(user);
        log.info("Nouvel utilisateur enregistré : {} ({})", saved.getEmail(), saved.getRole());
        return saved;
    }

    /**
     * Met à jour le profil d'un utilisateur.
     * On ne touche PAS au mot de passe ni au rôle ici (méthodes séparées).
     */
    @Override
    public User update(Long id, User updatedUser) {
        User existing = findById(id);

        if (!existing.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new RuntimeException("L'email '" + updatedUser.getEmail() + "' est déjà utilisé.");
        }

        existing.setNom(updatedUser.getNom());
        existing.setPrenom(updatedUser.getPrenom());
        existing.setEmail(updatedUser.getEmail());
        existing.setTelephone(updatedUser.getTelephone());
        existing.setInstitution(updatedUser.getInstitution());

        log.info("Profil mis à jour pour l'utilisateur : {}", existing.getEmail());
        return userRepository.save(existing);
    }


    @Override
    public User changeRole(Long id, Role newRole) {
        User user = findById(id);
        Role oldRole = user.getRole();
        user.setRole(newRole);
        log.info("Rôle changé pour {} : {} → {}", user.getEmail(), oldRole, newRole);
        return userRepository.save(user);
    }

    @Override
    public User toggleEnabled(Long id) {
        User user = findById(id);
        user.setEnabled(!user.isEnabled());
        log.info("Compte {} : statut changé à enabled={}", user.getEmail(), user.isEnabled());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        log.info("Utilisateur supprimé : {}", user.getEmail());
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
