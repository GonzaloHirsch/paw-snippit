package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.LoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class JwtTokenFactory {

    // Property for the issuer of the token
    private static final String TOKEN_ISSUER = "snippit.paw.itba";

    // TODO: check how to avoid making the stream every time
    @Value("classpath:authKey.key")
    private Resource resourceFile;

    // Time of expiration for the token
    private final static int DAYS_TO_EXPIRE = 1;

    // Time of expiration for the token
    private final static int DAYS_TO_EXPIRE_REFRESH = 7;

    public String createAccessJwtToken(LoginDto loginDto) {
        if (StringUtils.isBlank(loginDto.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        // Adding username as subject
        Claims claims = Jwts.claims().setSubject(loginDto.getUsername());

        // Variable for the now part of the token
        Instant now = Instant.now();

        byte[] secret = this.getSecret();

        // Generating the token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(TOKEN_ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(DAYS_TO_EXPIRE, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String createRefreshToken(LoginDto loginDto) {
        if (StringUtils.isBlank(loginDto.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        // Adding username as subject
        Claims claims = Jwts.claims().setSubject(loginDto.getUsername());

        // Variable for the now part of the token
        Instant now = Instant.now();

        // TODO: CHECK CLAIMS
        //claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

        byte[] secret = this.getSecret();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(TOKEN_ISSUER)
                .setId(UUID.randomUUID().toString().replace("-", ""))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(DAYS_TO_EXPIRE_REFRESH, ChronoUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private byte[] getSecret() {
        try {
            return IOUtils.toByteArray(this.resourceFile.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }
}