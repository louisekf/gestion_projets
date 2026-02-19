package sn.esmt.gestionprojets.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.exceptions.BusinessException;
import sn.esmt.gestionprojets.exceptions.ResourceNotFoundException;
import sn.esmt.gestionprojets.repository.UserRepository;
import sn.esmt.gestionprojets.service.UserService;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructeur explicite
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
                .orElseThrow(() -> ResourceNotFoundException.user(id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.userByEmail(email));
    }

    @Override
    public User register(User user) {
        // Vérifications métier
        if (userRepository.existsByEmail(user.getEmail())) {
            throw BusinessException.emailAlreadyExists(user.getEmail());
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw BusinessException.usernameAlreadyExists(user.getUsername());
        }

        // Encodage du mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Valeurs par défaut
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setEnabled(true);

        User saved = userRepository.save(user);
        log.info("Utilisateur enregistré : {} ({})", saved.getEmail(), saved.getRole());
        return saved;
    }

    @Override
    public User update(Long id, User updatedUser) {
        User existing = findById(id);

        // Vérification email uniquement si changé
        if (!existing.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw BusinessException.emailAlreadyExists(updatedUser.getEmail());
        }

        existing.setEmail(updatedUser.getEmail());
        existing.setNom(updatedUser.getNom());
        existing.setPrenom(updatedUser.getPrenom());
        existing.setTelephone(updatedUser.getTelephone());
        existing.setInstitution(updatedUser.getInstitution());

        log.info("Profil mis à jour : {}", existing.getEmail());
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
        log.info("Compte {} : enabled={}", user.getEmail(), user.isEnabled());
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