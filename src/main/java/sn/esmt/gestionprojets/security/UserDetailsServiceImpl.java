package sn.esmt.gestionprojets.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

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

