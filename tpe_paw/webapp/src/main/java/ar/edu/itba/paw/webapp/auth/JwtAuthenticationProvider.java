package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.utility.Authorities;
import ar.edu.itba.paw.webapp.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    // Refresh authority to check if the token contains
    private static final GrantedAuthority REFRESH_AUTHORITY = new SimpleGrantedAuthority(Authorities.REFRESH.getValue());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extracting info from authentication
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // Flag to know if it is a refresh token or normal token
        boolean isRefresh = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Constants.REFRESH_AUTHORITY.getAuthority()));

        if (isRefresh && username == null && password == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Recovering user, if no user, throw exception
        User user = this.userService.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // If we are authenticating normally, we have to test the values
        // In case we are refreshing, we are already authenticated
        if (!isRefresh) {
            // Checking if password is ok
            if (!encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
        }

        // Getting the user roles
        Collection<String> userRoles = this.roleService.getUserRoles(user.getId());

        // Checking if the user has roles
        if (userRoles == null || userRoles.isEmpty())
            throw new InsufficientAuthenticationException("User has no roles assigned");

        // Generating a list of authorities for the user
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Creating the user context
        UserContext userContext = UserContext.create(user.getId(), user.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
