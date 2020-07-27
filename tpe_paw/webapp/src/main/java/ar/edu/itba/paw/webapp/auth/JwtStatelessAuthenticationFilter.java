package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtStatelessAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    private final JwtTokenHandlerService tokenHandlerService;
    private final UserDetailsService userDetailsService;

    public JwtLoginProcessingFilter(RequestMatcher matcher, JwtTokenHandlerService tokenHandlerService, UserDetailsService userDetailsService) {
        super(matcher);
        this.tokenHandlerService = tokenHandlerService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // Extracting the token from the request
        final String token = this.tokenHandlerService.extractTokenPayload(request);

        // Authentication variable to be used
        Authentication auth = null;

        if (token != null) {
            // Extracting the username from the token
            final String username = this.tokenHandlerService.getUsernameFromToken(token);

            if (username != null) {
                // Getting user details given the username
                final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Generating the authentication
                auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            }
        }

        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Getting the details based on the authentication
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authResult.getName());

        // Adding the token to the response
        this.tokenHandlerService.addAuthenticationToken(response, userDetails);

        // Adding auth to the context
        SecurityContextHolder.getContext().setAuthentication(authResult);
    }
}
