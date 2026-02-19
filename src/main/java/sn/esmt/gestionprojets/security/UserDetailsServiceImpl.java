package sn.esmt.gestionprojets.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.esmt.gestionprojets.entity.User;
import sn.esmt.gestionprojets.repository.UserRepository;

/**
 * UserDetailsService AMÉLIORÉ avec exceptions personnalisées.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    // Constructeur explicite
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Tentative de chargement de l'utilisateur avec l'email : {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Aucun utilisateur trouvé pour l'email : {}", email);
                    return new UsernameNotFoundException("Identifiants incorrects");
                });

        log.debug("Utilisateur chargé avec succès : {} (rôle: {})", user.getEmail(), user.getRole());
        return new UserPrincipal(user);
    }
}