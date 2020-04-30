package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private static final Logger LOGGER = LoggerFactory.getLogger(PawUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));

        final Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        if(user.getUsername().equals("admin")){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            LOGGER.debug("Granting authority ROLE_ADMIN");
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            LOGGER.debug("Granting authority ROLE_USER");
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
