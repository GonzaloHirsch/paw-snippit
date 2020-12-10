package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.utility.Constants;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.json.Json;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class JwtTokenAuthenticationProcessingFilter extends GenericFilterBean {

    private final JwtTokenExtractor tokenExtractor;
    private final UserDetailsService userDetailsService;

    public JwtTokenAuthenticationProcessingFilter(JwtTokenExtractor tokenHandlerService, UserDetailsService userDetailsService) {
        this.tokenExtractor = tokenHandlerService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // Extracting the token from the request
            final String token = this.tokenExtractor.extractTokenPayload((HttpServletRequest) request);

            // Authentication variable to be used
            Authentication auth = null;

            if (token != null) {
                // Extracting the username from the token
                try {
                    final Collection<GrantedAuthority> authorities = this.tokenExtractor.getAuthoritiesFromToken(token);

                    // Avoid requests with the refresh token as the token
                    if (authorities.stream().noneMatch(a -> a.getAuthority().equals(Constants.REFRESH_AUTHORITY.getAuthority()))) {
                        // Extracting the username from the token
                        final String username = this.tokenExtractor.getUsernameFromToken(token);

                        if (username != null) {
                            // Getting user details given the username
                            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                            // Generating the authentication
                            auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                        }
                    }
                } catch (IOException e) {
                    auth = null;
                }
            }

            // Adding the auth to the current context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Continuing the filter chain
            chain.doFilter(request, response);
        } catch (JwtException e){
            response.getWriter().write(convertErrorMessageToJson(e.getMessage()));
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private String convertErrorMessageToJson(String message) {
        return Json.createObjectBuilder()
                .add("TokenError", message)
                .build()
                .toString();
    }
}
