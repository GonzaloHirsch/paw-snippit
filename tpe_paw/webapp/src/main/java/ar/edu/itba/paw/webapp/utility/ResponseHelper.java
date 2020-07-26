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

    /**
     * Adds the links to the FIRST, LAST, NEXT and PREV pages, depending on the total amount of pages and the current page
     * @param builder ResponseBuilder to be returned by the controller
     * @param uriInfo UriInfo holding the context of the request
     * @param currentPage Number of the current page
     * @param totalPages Total number of pages
     */
    public static void AddLinkAttributes(Response.ResponseBuilder builder, UriInfo uriInfo, int currentPage, int totalPages){
        // Adding the FIRST page
        if(currentPage > 1){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, 1).build(), LINK_FIRST);
        }

        // Adding the PREV page
        if (currentPage > 2){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, currentPage - 1).build(), LINK_PREV);
        }

        // Adding the NEXT page
        if (currentPage < totalPages - 1){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, currentPage + 1).build(), LINK_NEXT);
        }

        // Adding the LAST page
        if (currentPage < totalPages){
            builder.link(uriInfo.getAbsolutePathBuilder().queryParam(QUERY_PARAM_PAGE, totalPages).build(), LINK_LAST);
        }
    }
}
