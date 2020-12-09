package ar.edu.itba.paw.webapp.utility;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;

public final class UserHelper {
    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private UserHelper() {
        throw new AssertionError();
    }

    public static long GetLoggedUserId(LoginAuthentication auth){
        User u = auth.getLoggedInUser();
        return u == null ? -1 : u.getId();
    }

}
