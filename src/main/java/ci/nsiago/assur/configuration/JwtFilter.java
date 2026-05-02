package ci.nsiago.assur.configuration;

import ci.nsiago.assur.model.JwtEntity;
import ci.nsiago.assur.service.JwtService;
import ci.nsiago.assur.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Service
public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UtilisateurService utilisateurService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("authorization");
        String token = null;
        JwtEntity jwt = null;
        boolean isExpired = true;
        String username = null;
        log.info("header : "+header);

        if(header != null && header.startsWith("Bearer ")){
            token = header.substring(7);
            jwt = jwtService.tokenByValue(token);
            isExpired = jwtService.isTokenExpired(token);
            username = jwtService.extractUsername(token);
            log.info("username : "+username+" est expired : "+isExpired);
        }

        if(!isExpired &&
                jwt.getUtilisateur().getEmail().equals(username) &&
                SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
