package ci.nsiago.assur.controllers;

import ci.nsiago.assur.dto.UtilisateurDto;
import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.service.JwtService;
import ci.nsiago.assur.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/inscription")
    public UtilisateurDto.UtilisateurInscriptionOuput get(@RequestBody UtilisateurDto.UtilisateurInscriptionInput input){
        log.info(input.email());
        log.info(input.password());

        UtilisateurEntity newUser = utilisateurService.createUser(input.email(), input.password(), input.username());

         return new UtilisateurDto.UtilisateurInscriptionOuput(newUser.getId(),
                                                    newUser.getEmail(),
                                                    newUser.getUsername(),
                                                    newUser.getPassword(),
                                                    newUser.isActive());
    }

    @PostMapping("/connexion")
    public UtilisateurDto.UtilisateurConnexionOuput connexion(@RequestBody UtilisateurDto.UtilisateurConnexionInput input){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password())
        );

        final UtilisateurEntity utilisateur = (UtilisateurEntity) authentication.getPrincipal();

        return new UtilisateurDto.UtilisateurConnexionOuput(jwtService.generate(input.email()));
    }

    @PostMapping("/deconnexion")
    public void deconnexion(){
        jwtService.deconnexion();
    }
}
