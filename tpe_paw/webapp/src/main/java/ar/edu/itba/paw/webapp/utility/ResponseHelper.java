package ar.edu.itba.paw.webapp.utility;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static ar.edu.itba.paw.webapp.utility.Constants.*;

public final class ResponseHelper {

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private ResponseHelper() {
        throw new AssertionError();
    }

    public static void AddLinkAttributes(Response.ResponseBuilder builder, UriInfo uriInfo, int currentPage, int totalPages){
        // Adding the FIRST and LAST pages
        builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, 1).build(), LINK_FIRST).link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, totalPages).build(), LINK_LAST);

        // Adding the PREV page if possible
        if (currentPage > 1){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, currentPage - 1).build(), LINK_PREV);
        }

        // Adding the NEXT page if possible
        if (currentPage < totalPages){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, currentPage + 1).build(), LINK_NEXT);
        }
    }
}
