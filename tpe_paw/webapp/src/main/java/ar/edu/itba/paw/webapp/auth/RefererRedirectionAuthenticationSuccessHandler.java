package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
            SavedRequest savedRequest = (SavedRequest)session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if(savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }
        return request.getContextPath() + "/";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = this.getRedirectUrl(request);
            if (redirectUrl.compareTo(request.getContextPath() + "/") == 0) {

                redirectUrl = (String) session.getAttribute("url_prior_login");
                if (redirectUrl != null) {
                    /* Remove the attribute from the session */
                    session.removeAttribute("url_prior_login");

                    /* Don't want to redirect to any of these urls */
                    if (!(redirectUrl.contains("login") || redirectUrl.contains("signUp") || redirectUrl.contains("goodbye") || redirectUrl.contains("reset-password") || redirectUrl.contains("recover-password"))) {
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