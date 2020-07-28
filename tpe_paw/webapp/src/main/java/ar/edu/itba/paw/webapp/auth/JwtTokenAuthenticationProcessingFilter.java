package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
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

public class JwtTokenAuthenticationProcessingFilter extends GenericFilterBean {

    private final JwtTokenExtractor tokenExtractor;
    private final UserDetailsService userDetailsService;

    public JwtTokenAuthenticationProcessingFilter(JwtTokenExtractor tokenHandlerService, UserDetailsService userDetailsService) {
        this.tokenExtractor = tokenHandlerService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Extracting the token from the request
        final String token = this.tokenExtractor.extractTokenPayload((HttpServletRequest) request);

        // Authentication variable to be used
        Authentication auth = null;

        if (token != null) {
            // Extracting the username from the token
            final String username = this.tokenExtractor.getUsernameFromToken(token);

            if (username != null) {
                // Getting user details given the username
                final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Generating the authentication
                auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            }
        }

        // Adding the auth to the current context
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continuing the filter chain
        chain.doFilter(request, response);
    }
}
