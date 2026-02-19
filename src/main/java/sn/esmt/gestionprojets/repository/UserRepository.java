package sn.esmt.gestionprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.esmt.gestionprojets.entity.enums.Role;
import sn.esmt.gestionprojets.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByRole(Role role);

    long countByRole(Role role);

    /**
     * Charge les participants d'un projet spécifique.
     * Requête JPQL (pas SQL) : utilise les noms des entités Java.
     */
    @Query("SELECT u FROM User u JOIN u.projetsParticipant p WHERE p.id = :projectId")
    List<User> findParticipantsByProjectId(Long projectId);
}
