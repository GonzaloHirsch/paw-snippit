package ar.edu.itba.paw.webapp.utility;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static ar.edu.itba.paw.webapp.utility.Constants.MAX_SEARCH_QUERY_SIZE;
import static ar.edu.itba.paw.webapp.utility.Constants.SNIPPET_PAGE_SIZE;

public final class SearchHelper {

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private final static Map<String, SnippetDao.Types> typesMap;
    static {
        final Map<String, SnippetDao.Types> types = new HashMap<>();
        types.put(null, SnippetDao.Types.ALL);
        types.put("all", SnippetDao.Types.ALL);
        types.put("tag", SnippetDao.Types.TAG);
        types.put("title", SnippetDao.Types.TITLE);
        types.put("content", SnippetDao.Types.CONTENT);
        types.put("username", SnippetDao.Types.USER);
        types.put("language", SnippetDao.Types.LANGUAGE);
        typesMap = Collections.unmodifiableMap(types);
    }

    private final static Map<String, SnippetDao.Orders> ordersMap;
    static {
        final Map<String, SnippetDao.Orders> orders = new HashMap<>();
        orders.put("asc", SnippetDao.Orders.ASC);
        orders.put("desc", SnippetDao.Orders.DESC);
        orders.put("no", SnippetDao.Orders.NO);
        ordersMap = Collections.unmodifiableMap(orders);
    }

    private SearchHelper() {
        throw new AssertionError();
    }

    public static Collection<Snippet> FindByCriteria(SnippetService snippetService, String type, String query, SnippetDao.Locations location, String sort, Long userId, Long resourceId, int page) {
//        if (query.length() > MAX_SEARCH_QUERY_SIZE) {
//            throw new URITooLongException(messageSource.getMessage("error.414.search", null, LocaleContextHolder.getLocale()));
//        }
        if (!typesMap.containsKey(type) || !ordersMap.containsKey(sort)) {
            return Collections.emptyList();
        } else {
            return snippetService.findSnippetByCriteria(
                    typesMap.get(type),
                    query == null ? "" : query,
                    location,
                    ordersMap.get(sort),
                    userId,
                    resourceId,
                    page,
                    SNIPPET_PAGE_SIZE);
        }
    }

    public static int GetSnippetByCriteriaCount(SnippetService snippetService, String type, String sort, String query, SnippetDao.Locations location, Long userId, Long resourceId) {
        if (!typesMap.containsKey(type) || !ordersMap.containsKey(sort)) {
            return 0;
        } else {
            return snippetService.getSnippetByCriteriaCount(
                    typesMap.get(type),
                    query == null ? "" : query,
                    location,
                    userId,
                    resourceId);
        }
    }
}
