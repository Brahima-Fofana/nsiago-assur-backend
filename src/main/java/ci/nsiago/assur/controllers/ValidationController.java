package ci.nsiago.assur.controllers;

import ci.nsiago.assur.dto.UtilisateurDto;
import ci.nsiago.assur.dto.ValidationDto;
import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.service.JwtService;
import ci.nsiago.assur.service.UtilisateurService;
import ci.nsiago.assur.service.ValidationService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class ValidationController {
    private ValidationService validationService;
    private UtilisateurService utilisateurService;
    private JwtService jwtService;

    @PostMapping("/activation")
    public UtilisateurDto.UtilisateurConnexionOuput activeCompte(@RequestBody ValidationDto.ValidationInput input) throws BadRequestException {
        UtilisateurEntity user = utilisateurService.activationCompte(input.getCode());

        return new UtilisateurDto.UtilisateurConnexionOuput(jwtService.generateJwt(user));
    }
}
