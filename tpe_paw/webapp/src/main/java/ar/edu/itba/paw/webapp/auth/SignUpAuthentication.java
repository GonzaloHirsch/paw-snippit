package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SignUpAuthentication {
    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String REDIRECT_ATTRIBUTE = "url_prior_login";
    private static final String SAVED_REQUEST_ATTRIBUTE = "SPRING_SECURITY_SAVED_REQUEST";

    public void setRegisterRedirect(HttpServletRequest request) {
        String referrer = request.getHeader(Constants.REFERER);
        if (referrer != null && !referrer.contains("login")) {
            request.getSession().setAttribute(REDIRECT_ATTRIBUTE, referrer);
        } else {
            request.getSession().setAttribute(REDIRECT_ATTRIBUTE, Constants.HOME);
        }
    }

    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        // Generate session if one doesn't already exist
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        Authentication authentication = this.authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getSavedRequestRedirectUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            SavedRequest savedRequest = (SavedRequest)session.getAttribute(SAVED_REQUEST_ATTRIBUTE);
            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }
        return request.getContextPath() + "/";
    }

    public String redirectionAuthenticationSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = this.getSavedRequestRedirectUrl(request);
            if (redirectUrl.compareTo(request.getContextPath() + "/") != 0) {
                return redirectUrl;
            }
            redirectUrl = (String) session.getAttribute(REDIRECT_ATTRIBUTE);
            if (redirectUrl != null) {
                /* Remove the attribute from the session --> clean up */
                session.removeAttribute(REDIRECT_ATTRIBUTE);
                /* Don't want to redirect to any of these urls */
                if (!(redirectUrl.contains("login") || redirectUrl.contains("signup") || redirectUrl.contains("goodbye") || redirectUrl.contains("reset-password") || redirectUrl.contains("recover-password"))) {
                    return redirectUrl;
                }
            }
        }
        return "/";
    }
}