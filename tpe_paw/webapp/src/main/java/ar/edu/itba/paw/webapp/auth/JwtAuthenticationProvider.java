package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
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

    /*@Autowired
    public JwtAuthenticationProvider(final UserService userService, final PasswordEncoder encoder, final RoleService roleService) {
        this.userService = userService;
        this.encoder = encoder;
        this.roleService = roleService;
    }*/

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extracting info from authentication
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // Recovering user, if no user, throw exception
        User user = this.userService.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Checking if password is ok
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or passowrd");
        }

        // Getting the user roles
        Collection<String> userRoles = this.roleService.getUserRoles(user.getId());

        // Checking if the user has roles
        if (userRoles == null || userRoles.isEmpty()) throw new InsufficientAuthenticationException("User has no roles assigned");

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
