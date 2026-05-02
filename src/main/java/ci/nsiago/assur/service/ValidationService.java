package ci.nsiago.assur.service;

import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.model.ValidationEntity;
import ci.nsiago.assur.repository.ValidationRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@AllArgsConstructor
@Service
public class ValidationService {
    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void validation(UtilisateurEntity utilisateur){
        ValidationEntity valid = validationRepository.findByUtilisateur(utilisateur).orElse(null);
        Random random = new Random();
        String code = String.format("%06d",random.nextInt(999999));

        if(valid != null){
            Instant creation = Instant.now();
            valid.setCreation(creation);
            valid.setExpiration(creation.plus(10, ChronoUnit.MINUTES));
            valid.setCode(code);

            validationRepository.save(valid);
            notificationService.mailSend(valid);

            return;
        }

        ValidationEntity validation = new ValidationEntity();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);
        validation.setExpiration(creation.plus(10, ChronoUnit.MINUTES));
        validation.setCode(code);

        validationRepository.save(validation);
        notificationService.mailSend(validation);
    }

    public ValidationEntity lireCode(String code) throws BadRequestException {
        return this.validationRepository.findByCode(code).orElseThrow(() -> new BadRequestException("Le code est invalide"));
    }

}
