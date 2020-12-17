package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Context for the authentication process for the token
 */
public class UserContext {
    private Long id;
    private String username;
    private Collection<GrantedAuthority> authorities;
    private boolean canReport;

    private UserContext() {

    }

    public static UserContext create(Long id, String username, Collection<GrantedAuthority> authorities, boolean canReport) {
        UserContext ctx = new UserContext();

        ctx.id = id;
        ctx.username = username;
        ctx.authorities = authorities;
        ctx.canReport = canReport;

        return ctx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCanReport() {
        return canReport;
    }

    public void setCanReport(boolean canReport) {
        this.canReport = canReport;
    }
}
