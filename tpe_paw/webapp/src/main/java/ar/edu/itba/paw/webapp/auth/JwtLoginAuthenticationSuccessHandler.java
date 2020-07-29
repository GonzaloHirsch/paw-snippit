package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtTokenFactory tokenFactory;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Getting the user context
        UserContext context = (UserContext) authentication.getPrincipal();

        // Creating the TOKEN and REFRESH TOKEN
        String accessToken = tokenFactory.createAccessJwtToken(context);
        String refreshToken = tokenFactory.createRefreshToken(context);

        // Adding the token to the response
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", JwtUtil.PrepareTokenForUsage(accessToken));
        tokenMap.put("refreshToken", JwtUtil.PrepareTokenForUsage(refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), tokenMap);
        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     *
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}