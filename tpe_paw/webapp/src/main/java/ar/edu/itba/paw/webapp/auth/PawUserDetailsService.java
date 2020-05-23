package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleService roleService;

    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private static final Logger LOGGER = LoggerFactory.getLogger(PawUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));

        final Collection<GrantedAuthority> authorities = new HashSet<>();

        Collection<String> roles = roleService.getUserRoles(user.getId());
        if (roles.isEmpty()) {
            this.roleService.assignUserRole(user.getId());
            roles.add(this.roleService.getUserRoleName());
        }
        
        if(roles.contains(this.roleService.getAdminRoleName())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            LOGGER.debug("Granting authority ROLE_ADMIN to user {}", username);
        }
        else if (roles.contains(this.roleService.getUserRoleName())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            LOGGER.debug("Granting authority ROLE_USER to user {}", username);
        }

        final String password;
        if(!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {
            password = encoder.encode(user.getPassword());
            userService.changePassword(user.getEmail(), password);
        } else {
            password = user.getPassword();
        }

        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }

}
