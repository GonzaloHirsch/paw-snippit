package ar.edu.itba.paw.webapp.auth;

import io.jsonwebtoken.Jwts;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public class JwtTokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenExtractor.class);

    // Name for the authorization token header
    private static final String JWT_TOKEN_HEADER_NAME = "Authorization";

    // Prefix for the authorization token
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("classpath:authKey.key")
    private Resource resourceFile;

    private byte[] getSecret() throws IOException {
        return IOUtils.toByteArray(this.resourceFile.getInputStream());
    }

    public String extractTokenPayload(HttpServletRequest request) {
        String header = request.getHeader(JWT_TOKEN_HEADER_NAME);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String getUsernameFromToken(String token) throws IOException {
        byte[] secret = this.getSecret();

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Collection<GrantedAuthority> getAuthoritiesFromToken(String token) throws IOException {
        byte[] secret = this.getSecret();

        Collection<LinkedHashMap<String, String>> rawAuthorities = (Collection<LinkedHashMap<String, String>>) Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get(JwtTokenFactory.getTokenAuthorities());

        return rawAuthorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());
    }
}
