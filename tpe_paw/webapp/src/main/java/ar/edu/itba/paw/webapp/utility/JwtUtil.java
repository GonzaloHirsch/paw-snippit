package ar.edu.itba.paw.webapp.utility;

import javax.servlet.http.HttpServletResponse;

public final class JwtUtil {

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private JwtUtil() {
        throw new AssertionError();
    }

    // Name for the authorization token header
    private static final String JWT_TOKEN_HEADER_NAME = "Authorization";

    // Prefix for the authorization token
    private static final String TOKEN_PREFIX = "Bearer ";

    public static void AddAuthentication(HttpServletResponse res, String token){
        res.addHeader(JWT_TOKEN_HEADER_NAME, TOKEN_PREFIX + token);
    }

    public static String PrepareTokenForUsage(String token){
        return TOKEN_PREFIX + token;
    }
}
