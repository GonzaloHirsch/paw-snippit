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
        private final static String ALL_FIELDS = "DISTINCT sn.id, sn.title, sn.code, sn.user_id, sn.username, sn.language";

        /**
         * Map used to map types of search to the corresponding queries they translate to
         */
        private final static Map<SnippetDao.Types, String> typeMap;
        static {
            final Map<SnippetDao.Types, String> types = new HashMap<>();
            types.put(SnippetDao.Types.ALL, " AS s LEFT OUTER JOIN snippet_tags AS ts ON s.id = ts.snippet_id LEFT OUTER JOIN tags AS t ON t.id = ts.tag_id WHERE " + SearchUtils.TranslateField("t.name") + " LIKE "+SearchUtils.TranslateField(":term")+" OR " + SearchUtils.TranslateField("s.title") + " LIKE "+SearchUtils.TranslateField(":term")+" OR " + SearchUtils.TranslateField("s.code") + " LIKE "+SearchUtils.TranslateField(":term")+" OR " + SearchUtils.TranslateField("s.username") + " LIKE "+SearchUtils.TranslateField(":term")+" OR " + SearchUtils.TranslateField("s.language") + " LIKE " + SearchUtils.TranslateField(":term"));
            types.put(SnippetDao.Types.TAG, " AS s INNER JOIN snippet_tags AS ts ON s.id = ts.snippet_id INNER JOIN tags AS t ON t.id = ts.tag_id WHERE " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term"));
            types.put(SnippetDao.Types.TITLE, " AS s WHERE " + SearchUtils.TranslateField("s.title") + " LIKE " + SearchUtils.TranslateField(":term"));
            types.put(SnippetDao.Types.CONTENT, " AS s WHERE " + SearchUtils.TranslateField("s.code") + " LIKE " + SearchUtils.TranslateField(":term"));
            types.put(SnippetDao.Types.USER, " AS s WHERE " + SearchUtils.TranslateField("s.username") + " LIKE " + SearchUtils.TranslateField(":term"));
            types.put(SnippetDao.Types.LANGUAGE, " AS s WHERE " + SearchUtils.TranslateField("s.language") + " LIKE " + SearchUtils.TranslateField(":term"));
            typeMap = Collections.unmodifiableMap(types);
        }

        /**
         * Map used to map the locations or sources of snippets to be searched among to the corresponding queries they translate to
         */
        private final static Map<SnippetDao.Locations, String> locationsMap;
        static {
            final Map<SnippetDao.Locations, String> locations = new HashMap<>();
            locations.put(SnippetDao.Locations.HOME, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.USER, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.user_id = :userId AND sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.DELETED, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.user_id = :userId AND sn.deleted = TRUE)");
            locations.put(SnippetDao.Locations.LANGUAGES, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.language_id = :resourceId AND sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.TAGS, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN snippet_tags AS st ON st.snippet_id = sn.id WHERE st.tag_id = :resourceId AND sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.FAVORITES, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN favorites AS fav ON fav.snippet_id = sn.id WHERE fav.user_id = :userId)");
            locations.put(SnippetDao.Locations.FOLLOWING, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN snippet_tags AS st ON st.snippet_id = sn.id INNER JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = :userId AND sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.UPVOTED, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn INNER JOIN votes_for AS vf ON vf.snippet_id = sn.id WHERE vf.user_id = :userId AND vf.is_positive = TRUE AND sn.deleted = FALSE)");
            locations.put(SnippetDao.Locations.FLAGGED, "(SELECT " + ALL_FIELDS + " FROM complete_snippets AS sn WHERE sn.flagged = TRUE)");
            locationsMap = Collections.unmodifiableMap(locations);
        }
        /**
         * Map used to translate the given enum for order types into their query equivalent
         */
        private final static Map<SnippetDao.Orders, String> ordersMap;
        static {
            final Map<SnippetDao.Orders, String> orders = new HashMap<>();
            orders.put(SnippetDao.Orders.ASC, "ASC");
            orders.put(SnippetDao.Orders.DESC, "DESC");
            ordersMap = Collections.unmodifiableMap(orders);
        }
        /**
         * Map used to translate the types over which the order is done into their SQL equivalents
         */
        private final static Map<SnippetDao.Types, String> orderTypesMap;
        static {
            final Map<SnippetDao.Types, String> orderTypes = new HashMap<>();
            orderTypes.put(SnippetDao.Types.ALL, " s.title ");
            orderTypes.put(SnippetDao.Types.TAG, " s.title ");
            orderTypes.put(SnippetDao.Types.TITLE, " s.title ");
            orderTypes.put(SnippetDao.Types.CONTENT, " s.code ");
            orderTypes.put(SnippetDao.Types.USER, " s.username ");
            orderTypes.put(SnippetDao.Types.LANGUAGE, " s.language ");
            orderTypesMap = Collections.unmodifiableMap(orderTypes);
        }
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
                    .append(locationsMap.get(location));
            if (userId != null){ params.put("userId", userId); }
            if (resourceId != null) { params.put("resourceId", resourceId);}
            this.query.append(typeMap.get(type));
            params.put("term", "%" + SearchUtils.EscapeSpecialCharacters(term) + "%");
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
                        .append(orderTypesMap.get(type))
                        .append(ordersMap.get(order));
            }
            return this;
        }

        public SnippetSearchQuery build(){
            return new SnippetSearchQuery(this.query.toString(), this.params);
        }
    }
}
