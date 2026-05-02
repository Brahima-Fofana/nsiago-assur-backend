package ci.nsiago.assur.service;

import ci.nsiago.assur.model.ValidationEntity;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    private final JavaMailSender javaMailSender;

    public void mailSend(ValidationEntity validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@nsiago-assur");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Mail de validation");
        String texte = String.format("M/Mlle/Mme %s, Votre code de validation est le : %s",validation.getUtilisateur().getUsername(),validation.getCode());

        message.setText(texte);
        javaMailSender.send(message);
    }

}
