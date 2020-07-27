package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.config.WebAuthConfig;

import javax.servlet.http.HttpServletRequest;

public class JwtTokenService {

    public String extractTokenPayload(HttpServletRequest request){
        return request.getHeader(WebAuthConfig.JWT_TOKEN_HEADER_NAME);
    }

    public
}
