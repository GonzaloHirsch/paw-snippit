package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.exception.AuthenticationMethodNotSupportedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    /**
     * Catches the request to the given URL and attempts the authentication given the credentials
     * @param defaultProcessUrl
     * @param successHandler
     * @param failureHandler
     * @param mapper
     * @param authenticationManager
     */
    public JwtLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                    AuthenticationFailureHandler failureHandler, ObjectMapper mapper, AuthenticationManager authenticationManager) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // Check if the method used is valid
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            LOGGER.debug("Authentication method not supported. Request method: {}", request.getMethod());
            throw new AuthenticationMethodNotSupportedException("Authentication method not supported");
        }

        // Map the data from the request into a login DTO
        LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);

        // Verify there is information
        if ((loginDto.getUsername() == null || loginDto.getUsername().isEmpty()) || (loginDto.getPassword() == null || loginDto.getPassword().isEmpty())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generating the token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
