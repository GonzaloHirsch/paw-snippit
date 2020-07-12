package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SignUpAuthentication {
    @Autowired
    private AuthenticationManager authenticationManager;

    public void setRegisterRedirect(HttpServletRequest request) {
        String referrer = request.getHeader(Constants.REFERER);
        if (referrer != null && !referrer.contains(Constants.LOGIN)) {
            request.getSession().setAttribute(Constants.REDIRECT_ATTRIBUTE, referrer);
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
            SavedRequest savedRequest = (SavedRequest)session.getAttribute(Constants.SAVED_REQUEST_ATTRIBUTE);
            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }
        return request.getContextPath() + Constants.HOME;
    }

    public String redirectionAuthenticationSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = this.getSavedRequestRedirectUrl(request);
            if (redirectUrl.compareTo(request.getContextPath() + Constants.HOME) != 0) {
                return redirectUrl;
            }
            redirectUrl = (String) session.getAttribute(Constants.REDIRECT_ATTRIBUTE);
            if (redirectUrl != null) {
                /* Remove the attribute from the session --> clean up */
                session.removeAttribute(Constants.REDIRECT_ATTRIBUTE);
                /* Don't want to redirect to any of these urls */
                if (!(redirectUrl.contains(Constants.LOGIN) || redirectUrl.contains(Constants.SIGNUP) || redirectUrl.contains(Constants.GOODBYE) || redirectUrl.contains(Constants.RESET_PASSWORD) || redirectUrl.contains(Constants.RECOVER_PASSWORD))) {
                    return redirectUrl;
                }
            }
        }
        return Constants.HOME;
    }
}