package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;

import java.util.*;

/**
 * Class to generate a query to search Snippets, implements the Builder pattern
 */
public class SnippetSearchQuery {
    /**
     * Query to be used in the database, must have parametrized variables
     */
    private String query;
    /**
     * Parameters to be used in the query, in order
     */
    private Map<String, Object> params;

    public String getQuery(){ return this.query; }

    public Map<String, Object> getParams(){ return this.params; }

    /**
     * Private constructor to follow the builder patter
     * @param query Query to be used in the database
     * @param params List with the parameters in order
     */
    private SnippetSearchQuery(String query, Map<String, Object> params){
        this.query = query;
        this.params = params;
    }

    /**
     * Inner, static Builder class to implement the builder pattern
     */
    public static class Builder {
        private final String ALL_FIELDS = "DISTINCT sn.id, sn.title, sn.code, sn.user_id, sn.username, sn.language";
        private final String IGNORE_DELETED = "AND s.deleted = FALSE";

        /**
         * Map used to map types of search to the corresponding queries they translate to
         */
        private final Map<SnippetDao.Types, String> typeMap = new HashMap<SnippetDao.Types, String>(){{
            put(SnippetDao.Types.ALL, " AS s LEFT OUTER JOIN snippet_tags AS ts ON s.id = ts.snippet_id LEFT OUTER JOIN tags AS t ON t.id = ts.tag_id WHERE lower(t.name) LIKE lower(:term) OR lower(s.title) LIKE lower(:term) OR lower(s.code) LIKE lower(:term) OR lower(s.username) LIKE lower(:term) OR lower(s.language) LIKE lower(:term)");
            put(SnippetDao.Types.TAG, " AS s INNER JOIN snippet_tags AS ts ON s.id = ts.snippet_id INNER JOIN tags AS t ON t.id = ts.tag_id WHERE lower(t.name) LIKE lower(:term)");
            put(SnippetDao.Types.TITLE, " AS s WHERE lower(s.title) LIKE lower(:term)");
            put(SnippetDao.Types.CONTENT, " AS s WHERE lower(s.code) LIKE lower(:term)");
            put(SnippetDao.Types.USER, " AS s WHERE lower(s.username) LIKE lower(:term)");
            put(SnippetDao.Types.LANGUAGE, " AS s WHERE lower(s.language) LIKE lower(:term)");
        }};
        /**
         * Map used to map the locations or sources of snippets to be searched among to the corresponding queries they translate to
         */
        private Map<SnippetDao.Locations, String> locationsMap = new HashMap<SnippetDao.Locations, String>(){{
            put(SnippetDao.Locations.HOME, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.deleted = FALSE)");
            put(SnippetDao.Locations.USER, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.user_id = :userId AND sn.deleted = FALSE)");
            put(SnippetDao.Locations.LANGUAGES, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.language_id = :resourceId AND sn.deleted = FALSE)");
            put(SnippetDao.Locations.TAGS, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN snippet_tags AS st ON st.snippet_id = sn.id WHERE st.tag_id = :resourceId AND sn.deleted = FALSE)");
            put(SnippetDao.Locations.FAVORITES, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN favorites AS fav ON fav.snippet_id = sn.id WHERE fav.user_id = :userId)");
            put(SnippetDao.Locations.FOLLOWING, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN snippet_tags AS st ON st.snippet_id = sn.id INNER JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = :userId AND sn.deleted = FALSE)");
            put(SnippetDao.Locations.UPVOTED, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN votes_for AS vf ON vf.snippet_id = sn.id WHERE vf.user_id = :userId AND vf.type = 1)");
            put(SnippetDao.Locations.FLAGGED, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.flagged = 1)");
        }};
        /**
         * Map used to translate the given enum for order types into their query equivalent
         */
        private final Map<SnippetDao.Orders, String> ordersMap = new HashMap<SnippetDao.Orders, String>(){{
            put(SnippetDao.Orders.ASC, "ASC");
            put(SnippetDao.Orders.DESC, "DESC");
        }};
        /**
         * Map used to translate the types over which the order is done into their SQL equivalents
         */
        private final Map<SnippetDao.Types, String> orderTypesMap = new HashMap<SnippetDao.Types, String>(){{
            put(SnippetDao.Types.ALL, " s.title ");
            put(SnippetDao.Types.TAG, " s.title ");
            put(SnippetDao.Types.TITLE, " s.title ");
            put(SnippetDao.Types.CONTENT, " s.code ");
            put(SnippetDao.Types.USER, " s.username ");
            put(SnippetDao.Types.LANGUAGE, " s.language ");
        }};
        /**
         * StringBuilder in order to make the building of the query more performant
         */
        private final StringBuilder query = new StringBuilder();
        /**
         * Named parameter map for query
         */
        private final Map<String, Object> params = new HashMap<>();

        /**
         * Constructor for the Builder inner class
         * @param location Source of the snippets
         * @param userId ID of the user performing the operation, not needed if in HOME
         * @param type Type of search, over which parameter the search is going to be performed
         * @param term Term to be used for the search
         */
        public Builder(SnippetDao.Locations location, Long userId, SnippetDao.Types type, String term, Long resourceId){
            this.query
                    .append("SELECT DISTINCT s.id, s.title, s.code, s.user_id, s.username, s.language FROM ")
                    .append(this.locationsMap.get(location));
            if (userId != null){ params.put("userId", userId); }
            if (resourceId != null) { params.put("resourceId", resourceId);}
            this.query.append(this.typeMap.get(type));
            params.put("term", "%" + term + "%");
        }

        /**
         * Method to add order to the results
         * @param order Order of the results
         * @param type Field over which the order is going to be performed
         * @return this same instance of the Builder
         */
        public Builder setOrder(SnippetDao.Orders order, SnippetDao.Types type){
            if (!order.equals(SnippetDao.Orders.NO)){
                this.query.append(" ORDER BY")
                        .append(this.orderTypesMap.get(type))
                        .append(this.ordersMap.get(order));
            }
            return this;
        }

        public SnippetSearchQuery build(){
            return new SnippetSearchQuery(this.query.toString(), this.params);
        }
    }
}
