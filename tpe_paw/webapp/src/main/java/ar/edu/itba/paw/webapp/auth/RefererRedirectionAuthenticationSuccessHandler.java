package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class RefererRedirectionAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler {

    public RefererRedirectionAuthenticationSuccessHandler() {
        this.setDefaultTargetUrl("/");
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setUseReferer(true);
    }

    /* Will give me the url when the user was redirected to the login  */
    private String getRedirectUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            SavedRequest savedRequest = (SavedRequest)session.getAttribute(Constants.SAVED_REQUEST_ATTRIBUTE);
            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }
        return request.getContextPath() + Constants.HOME;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = this.getRedirectUrl(request);
            if (redirectUrl.compareTo(request.getContextPath() + Constants.HOME) == 0) {

                redirectUrl = (String) session.getAttribute(Constants.REDIRECT_ATTRIBUTE);
                if (redirectUrl != null) {
                    /* Remove the attribute from the session */
                    session.removeAttribute(Constants.REDIRECT_ATTRIBUTE);

                    /* Don't want to redirect to any of these urls */
                    if (!(redirectUrl.contains(Constants.LOGIN) || redirectUrl.contains(Constants.SIGNUP) || redirectUrl.contains(Constants.GOODBYE) || redirectUrl.contains(Constants.RESET_PASSWORD) || redirectUrl.contains(Constants.RECOVER_PASSWORD))) {
                        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                        return;
                    }
                }
            } else {
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                return;
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}