package sn.esmt.gestionprojets.exceptions;

/**
 * Exception pour les violations de règles métier.
 * Ex: email déjà utilisé, domaine avec projets associés, etc.
 *
 * Permet de distinguer les erreurs métier (400) des erreurs système (500).
 */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  // Helpers pour les cas courants
  public static BusinessException emailAlreadyExists(String email) {
    return new BusinessException("L'email '" + email + "' est déjà utilisé.");
  }

  public static BusinessException usernameAlreadyExists(String username) {
    return new BusinessException("Le nom d'utilisateur '" + username + "' est déjà pris.");
  }

  public static BusinessException codeAlreadyExists(String code) {
    return new BusinessException("Le code '" + code + "' est déjà utilisé.");
  }

  public static BusinessException cannotDeleteDomaine(String nom, int projectCount) {
    return new BusinessException(
            "Impossible de supprimer le domaine '" + nom +
                    "' : " + projectCount + " projet(s) y sont associés."
    );
  }
}
