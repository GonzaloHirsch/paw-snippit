package ar.edu.itba.paw.webapp.auth;

import io.jsonwebtoken.Jwts;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtTokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenExtractor.class);

    // Name for the authorization token header
    private static final String JWT_TOKEN_HEADER_NAME = "Authorization";

    // Prefix for the authorization token
    private static final String TOKEN_PREFIX = "Bearer ";

    // TODO: check how to avoid making the stream every time
    @Value("classpath:authKey.key")
    private Resource resourceFile;

    public String extractTokenPayload(HttpServletRequest request){
        String header = request.getHeader(JWT_TOKEN_HEADER_NAME);
        if (header != null && header.startsWith(TOKEN_PREFIX)){
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String getUsernameFromToken(String token) throws IOException {
        byte[] secret = IOUtils.toByteArray(this.resourceFile.getInputStream());

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
