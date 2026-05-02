package ci.nsiago.assur.dto;


public class UtilisateurDto {

    public static record UtilisateurInscriptionInput (
         String email,
         String username,
         String password
    ){}

    public static record UtilisateurInscriptionOuput (
        int id,
        String email,
        String username,
        String password,
        boolean active
    ){}
    public static record UtilisateurConnexionInput (
            String email,
            String password
    ){}
    public static record UtilisateurConnexionOuput (
            String token
    ){}
}
