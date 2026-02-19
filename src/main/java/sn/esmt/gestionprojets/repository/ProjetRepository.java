package sn.esmt.gestionprojets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.esmt.gestionprojets.entity.DomaineRecherche;
import sn.esmt.gestionprojets.entity.Projet;
import sn.esmt.gestionprojets.entity.enums.StatutProjet;
import sn.esmt.gestionprojets.entity.User;

import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {

    List<Projet> findByResponsable(User responsable);

    List<Projet> findByParticipantsContaining(User user);

    List<Projet> findByDomaine(DomaineRecherche domaine);

    List<Projet> findByStatut(StatutProjet statut);

    List<Projet> findByDomaineAndStatut(DomaineRecherche domaine, StatutProjet statut);

    long countByStatut(StatutProjet statut);

    long countByDomaine(DomaineRecherche domaine);


     // Nombre de projets par domaine, retourne une liste de tableaux [domaineName, count].

    @Query("SELECT d.nom, COUNT(p) FROM Projet p JOIN p.domaine d GROUP BY d.nom ORDER BY COUNT(p) DESC")
    List<Object[]> countProjectsGroupByDomaine();

    /**
     * Nombre de projets par statut, retourne une liste [statut, count].
     */
    @Query("SELECT p.statut, COUNT(p) FROM Projet p GROUP BY p.statut")
    List<Object[]> countProjectsGroupByStatut();

    /**
     * Nombre de projets démarrés par année.
     */
    @Query("SELECT YEAR(p.dateDebut), COUNT(p) FROM Projet p GROUP BY YEAR(p.dateDebut) ORDER BY YEAR(p.dateDebut)")
    List<Object[]> countProjectsGroupByYear();

    /**
     * Récupère TOUS les projets d'un utilisateur :
     * ceux dont il est responsable + ceux où il est participant.
     */
    @Query("SELECT DISTINCT p FROM Projet p " +
            "WHERE p.responsable = :user " +
            "OR :user MEMBER OF p.participants")
    List<Projet> findAllProjectsOfUser(@Param("user") User user);

    /**
     * Charge des participants et nombre de projets associés.
     * Retourne : [nomComplet, nombreDeProjets]
     */
    @Query("SELECT CONCAT(u.prenom, ' ', u.nom), COUNT(DISTINCT p) " +
            "FROM Projet p JOIN p.participants u " +
            "GROUP BY u.id, u.prenom, u.nom " +
            "ORDER BY COUNT(DISTINCT p) DESC")
    List<Object[]> countProjectsGroupByParticipant();

    /**
     * Budget total par domaine
     */
    @Query("SELECT d.nom, SUM(p.budgetEstime) FROM Projet p JOIN p.domaine d " +
            "WHERE p.budgetEstime IS NOT NULL " +
            "GROUP BY d.nom ORDER BY SUM(p.budgetEstime) DESC")
    List<Object[]> sumBudgetGroupByDomaine();

    /**
     * Taux moyen d'avancement sur tous les projets en cours.
     */
    @Query("SELECT AVG(p.niveauAvancement) FROM Projet p WHERE p.statut = 'EN_COURS'")
    Double findAverageAvancement();
}
