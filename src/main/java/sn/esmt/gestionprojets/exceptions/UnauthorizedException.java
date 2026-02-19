package sn.esmt.gestionprojets.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    // Helpers
    public static UnauthorizedException cannotReadProject() {
        return new UnauthorizedException("Accès refusé : vous n'êtes pas impliqué dans ce projet.");
    }

    public static UnauthorizedException cannotModifyProject() {
        return new UnauthorizedException("Accès refusé : vous n'êtes pas le responsable de ce projet.");
    }

    public static UnauthorizedException adminOnly() {
        return new UnauthorizedException("Seul un administrateur peut effectuer cette action.");
    }

    public static UnauthorizedException managerOrAdminOnly() {
        return new UnauthorizedException("Seuls les gestionnaires et administrateurs peuvent effectuer cette action.");
    }
}
