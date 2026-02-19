package sn.esmt.gestionprojets.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructeurs helper pour éviter la répétition
    public static ResourceNotFoundException user(Long id) {
        return new ResourceNotFoundException("Utilisateur introuvable avec l'ID : " + id);
    }

    public static ResourceNotFoundException userByEmail(String email) {
        return new ResourceNotFoundException("Utilisateur introuvable avec l'email : " + email);
    }

    public static ResourceNotFoundException project(Long id) {
        return new ResourceNotFoundException("Projet introuvable avec l'ID : " + id);
    }

    public static ResourceNotFoundException domaine(Long id) {
        return new ResourceNotFoundException("Domaine introuvable avec l'ID : " + id);
    }
}
