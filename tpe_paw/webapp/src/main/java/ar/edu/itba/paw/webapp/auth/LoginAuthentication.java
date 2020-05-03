package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class LoginAuthentication {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthentication.class);

    private String getLoggedInUsername() {
        final Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        }
        return null;
    }

    private String getLoggedInUsernameWithSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        final Object userDetails = ((SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        }
        return null;
    }

    public User getLoggedInUser() {
        final String username = this.getLoggedInUsername();
        if (username == null) {
            return null;
        }
        return this.findUser(username);
    }

    public User getLoggedInUser(HttpServletRequest request) {
        final String username = this.getLoggedInUsernameWithSession(request);
        if (username == null) {
            return null;
        }
        return this.findUser(username);
    }

    private User findUser(String username) {
        final Optional<User> user = userService.findUserByUsername(username);
        if (!user.isPresent()) {
            LOGGER.warn("Logged user {} not found in the database", username);
            return null;
        }
        LOGGER.debug("Logged user is {}", username);
        return user.get();
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        // Generate session if one doesn't already exist
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        request.getSession().setAttribute("PRINCIPAL_NAME_INDEX_NAME", username);

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
