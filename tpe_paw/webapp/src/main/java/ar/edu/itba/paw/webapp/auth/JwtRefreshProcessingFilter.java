package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.LoginDto;
import ar.edu.itba.paw.webapp.exception.AuthenticationMethodNotSupportedException;
import ar.edu.itba.paw.webapp.exception.ExpiredAuthenticationTokenException;
import ar.edu.itba.paw.webapp.exception.InvalidAuthenticationTokenException;
import ar.edu.itba.paw.webapp.utility.Authorities;
import ar.edu.itba.paw.webapp.utility.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class JwtRefreshProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;
    private final JwtTokenExtractor tokenExtractor;
    private final UserDetailsService userDetailsService;

    /**
     * Catches the request to the given URL and attempts the authentication given the credentials
     *
     * @param defaultProcessUrl
     * @param successHandler
     * @param failureHandler
     * @param mapper
     * @param authenticationManager
     */
    public JwtRefreshProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                      AuthenticationFailureHandler failureHandler, ObjectMapper mapper, AuthenticationManager authenticationManager, JwtTokenExtractor tokenHandlerService, UserDetailsService userDetailsService) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
        this.setAuthenticationManager(authenticationManager);
        this.tokenExtractor = tokenHandlerService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        try {
            // Check if the method used is valid
            if (!HttpMethod.POST.name().equals(request.getMethod())) {
                LOGGER.debug("Authentication method not supported. Request method: {}", request.getMethod());
                throw new AuthenticationMethodNotSupportedException("Authentication method not supported");
            }

            // Extracting the token from the request
            final String token = this.tokenExtractor.extractTokenPayload(request);

            // Authentication variable to be used
            Authentication auth = new UsernamePasswordAuthenticationToken(null, null, Collections.singleton(Constants.REFRESH_AUTHORITY));

            if (token != null) {
                try {
                    // Extracting the username from the token
                    final Collection<GrantedAuthority> authorities = this.tokenExtractor.getAuthoritiesFromToken(token);

                    // Avoid making the refresh token useful for requests
                    if (authorities.stream().anyMatch(a -> a.getAuthority().equals(Constants.REFRESH_AUTHORITY.getAuthority()))) {
                        final String username = this.tokenExtractor.getUsernameFromToken(token);

                        if (username != null) {
                            // Getting user details given the username
                            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                            // Generating the authentication
                            auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), authorities);
                        }
                    }
                } catch (IOException e) {
                    auth = null;
                }
            }

            return this.getAuthenticationManager().authenticate(auth);
        }
        // On error throw corresponding token error exceptions for failure handler
        catch (ExpiredJwtException e) {
            throw new ExpiredAuthenticationTokenException(e.getMessage());
        } catch (JwtException e){
            throw new InvalidAuthenticationTokenException(e.getMessage());
        }
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
