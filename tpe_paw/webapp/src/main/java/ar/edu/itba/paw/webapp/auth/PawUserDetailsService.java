package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found!"));

        final Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        //TODO check which ones to add based on the user
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority(" ROLE_ANONYMOUS"));

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
