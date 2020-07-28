package ar.edu.itba.paw.webapp.auth;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class JwtTokenAuthenticationService {

    // Name for the authorization token header
    private static final String JWT_TOKEN_HEADER_NAME = "Authorization";

    // Prefix for the authorization token
    private static final String TOKEN_PREFIX = "Bearer ";

    public void addAuthentication(HttpServletResponse res, String token){
        res.addHeader(JWT_TOKEN_HEADER_NAME, TOKEN_PREFIX + token);
    }
}
