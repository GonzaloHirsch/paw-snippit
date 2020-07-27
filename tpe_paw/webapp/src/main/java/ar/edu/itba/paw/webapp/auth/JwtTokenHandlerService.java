package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.config.WebAuthConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenHandlerService {

    // Name for the authorization token header
    public static final String JWT_TOKEN_HEADER_NAME = "X-Authorization";

    // Property for the secret key to be used
    private final byte[] secret;

    // Time of expiration for the token
    private final static int DAYS_TO_EXPIRE = 1;

    @Autowired
    public JwtTokenHandlerService() throws IOException {
        ClassPathResource keyResource = new ClassPathResource("authKey.key");
        this.secret = IOUtils.toByteArray(keyResource.getInputStream());
    }

    public String extractTokenPayload(HttpServletRequest request){
        return request.getHeader(JWT_TOKEN_HEADER_NAME);
    }

    private String generateToken(final String username){
        // Instant for dates
        Instant now = Instant.now();
        Instant expiration = now.plus(DAYS_TO_EXPIRE, ChronoUnit.DAYS);

        // ID of the token
        String id = UUID.randomUUID().toString().replace("-", "");

        return Jwts.builder()
                .setId(id)
                .setSubject(username)
                .setExpiration(Date.from(expiration))
                .setIssuedAt(Date.from(now))
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    public void addAuthenticationToken(final HttpServletResponse response, final UserDetails userDetails){
        // Generating the token
        final String token = this.generateToken(userDetails.getUsername());

        // Adding the header to the response
        response.addHeader(JWT_TOKEN_HEADER_NAME, token);
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
