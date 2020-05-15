package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SignUpAuthentication {
    @Autowired
    private AuthenticationManager authenticationManager;

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        // Generate session if one doesn't already exist
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        Authentication authentication = this.authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String redirectionAuthenticationSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null) {
                /* Remove the attribute from the session --> clean up */
                session.removeAttribute("url_prior_login");
                /* Don't want to redirect to any of these urls */
                if (!(redirectUrl.contains("login") || redirectUrl.contains("signup") || redirectUrl.contains("goodbye") || redirectUrl.contains("reset-password") || redirectUrl.contains("recover-password"))) {
                    return redirectUrl;
                }
            }
        }
        return "/";
    }
}