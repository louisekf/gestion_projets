package sn.esmt.gestionprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.esmt.gestionprojets.entity.DomaineRecherche;

import java.util.Optional;

@Repository
public interface DomaineRechercheRepository extends JpaRepository<DomaineRecherche, Long> {

    Optional<DomaineRecherche> findByCode(String code);

    Optional<DomaineRecherche> findByNom(String nom);

    boolean existsByCode(String code);

    boolean existsByNom(String nom);
}
