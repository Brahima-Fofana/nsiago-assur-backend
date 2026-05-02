package ci.nsiago.assur.service;

import ci.nsiago.assur.model.JwtEntity;
import ci.nsiago.assur.model.UtilisateurEntity;
import ci.nsiago.assur.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JwtService {
    private UtilisateurService utilisateurService;
    private JwtRepository jwtRepository;

    public JwtEntity tokenByValue(String token){
        return jwtRepository.findByValueAndExpirerAndDesactiver(
                                token,
                                false,
                                false
                ).orElseThrow(() -> new RuntimeException("Token n'existe pas !"));
    }
    public void disableToken(UtilisateurEntity user){
        List<JwtEntity> jwts = jwtRepository.findAllByUtilisateur(user);
        jwts.forEach(
                jwt -> {
                    jwt.setExpirer(true);
                    jwt.setDesactiver(true);
                }
        );
        jwtRepository.saveAll(jwts);
    }
    public String extractUsername(String token){
        return getClaim(token, Claims::getSubject);
    }
    public boolean isTokenExpired(String token){
        return getClaim(token, Claims::getExpiration).before(new Date());
    }
    public <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }
    private Claims getAllClaims(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }
    public String generate(String utilisateur){
        UtilisateurEntity user = (UtilisateurEntity) utilisateurService.loadUserByUsername(utilisateur);
        return generateJwt(user);
    }
    public String generateJwt(UtilisateurEntity utilisateur){
        disableToken(utilisateur);
        final Map<String, String> bodyClaim = Map.of(
                "username", utilisateur.getUsername(),
                "email", utilisateur.getEmail(),
                Claims.SUBJECT, utilisateur.getEmail()
        );
        final long currentTime = System.currentTimeMillis();
        final long expiration = currentTime + 30 * 60 * 1000;

        final String bearer = Jwts.builder()
                                  .claims(bodyClaim)
                                  .issuedAt(new Date(currentTime))
                                  .expiration(new Date(expiration))
                                  .subject(utilisateur.getEmail())
                                  .signWith(getKey())
                                  .compact();

        JwtEntity jwtEntity = JwtEntity.builder().value(bearer)
                                                .expirer(false)
                                                .desactiver(false)
                                                .utilisateur(utilisateur)
                                                .build();
        jwtRepository.save(jwtEntity);

        return bearer;
    }
    private SecretKey getKey(){
        String ENCODER_KEY = "b6ff95fbcf9a3b6a6a0279415c35ba7034fafe2bf6f4b78f07d2b6c72d0c10af";
        final byte[] key = Decoders.BASE64.decode(ENCODER_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public void deconnexion(){
        UtilisateurEntity utilisateur = (UtilisateurEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JwtEntity jwt = jwtRepository.findUtilisateurTokenValid(
                utilisateur.getEmail(),
                false,
                false
        ).orElseThrow(()->new RuntimeException("Le token est invalid"));
        jwt.setDesactiver(true);
        jwt.setExpirer(true);

        jwtRepository.save(jwt);
        return;
    }

    @Transactional
    @Scheduled(cron = "0 */39 * * * *")
    public void removeToken(){
        jwtRepository.deleteAllByExpirerAndDesactiver(true, true);
    }
}
