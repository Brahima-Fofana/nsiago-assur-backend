package ci.nsiago.assur.service;

import ci.nsiago.assur.model.RoleEntity;
import ci.nsiago.assur.model.TypeRole;
import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.model.ValidationEntity;
import ci.nsiago.assur.repository.UtilisateurRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final RoleService roleService;

    public UtilisateurEntity createUser(String email, String password, String username) throws RuntimeException{
        UtilisateurEntity utilisateur = null;

        if (username == null || StringUtils.isBlank(username)){
            throw new RuntimeException("Veuillez renseigner le nom d'utilisateur !");
        }
        if (email == null || StringUtils.isBlank(email)){
            throw new RuntimeException("Veuillez renseigner l'email !");
        }
        if (password == null || StringUtils.isBlank(password)){
            throw new RuntimeException("Veuillez renseigner le mot de passe !");
        }
        if (password.length() < 3){
            throw new RuntimeException("Le mot de passe est trop court !");
        }
        if(!email.contains("@") || !email.contains(".")){
            throw new RuntimeException("l'email n'est pas valide");
        }

        utilisateur = utilisateurRepository.findByEmail(email).orElse(null);
        if(utilisateur != null){
            if(utilisateur.isActive()) throw new RuntimeException("L'email existe deja !");
            validationService.validation(utilisateur);
        }else{
            utilisateur = UtilisateurEntity.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .username(username)
                    .build();

            RoleEntity role = roleService.findLibelle(TypeRole.UTILISATEUR).orElseThrow(()->new RuntimeException("Erreur role n'existe pas !"));
            utilisateur.setRole(role);

            utilisateurRepository.save(utilisateur);
            validationService.validation(utilisateur);
        }
        return utilisateur;
    }

    public UtilisateurEntity activationCompte(@RequestBody String code) throws BadRequestException {
        if(code == null || StringUtils.isBlank(code)){
            throw new BadRequestException("Veuilez renseigner le code");
        }
        ValidationEntity validation = validationService.lireCode(code);
        if(Instant.now().isAfter(validation.getExpiration())){
            throw new BadRequestException("Le code est expire veuillez genere un nouveau code");
        }

        UtilisateurEntity user = utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()->new BadRequestException("Utilisateur non trouvé !"));
        user.setActive(true);
        utilisateurRepository.save(user);

        return utilisateurRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(username).orElseThrow( () -> new RuntimeException("L'utilisateur n'existe pas !"));
    }
}
