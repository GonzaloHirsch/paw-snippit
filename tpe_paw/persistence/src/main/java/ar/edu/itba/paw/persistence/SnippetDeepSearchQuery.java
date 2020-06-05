package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.*;
import java.util.spi.CalendarNameProvider;

public class SnippetDeepSearchQuery {
    /**
     * Query to be used in the database, must have parametrized variables
     */
    private String query;
    /**
     * Parameters to be used in the query, in order
     */
    private Map<String, Object> params;

    public String getQuery() {
        return this.query;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    /**
     * Private constructor to follow the builder patter
     *
     * @param query  Query to be used in the database
     * @param params List with the parameters in order
     */
    private SnippetDeepSearchQuery(String query, Map<String, Object> params) {
        this.query = query;
        this.params = params;
    }

    /**
     * Inner, static Builder class to implement the builder pattern
     */
    public static class Builder{
        /**
         * StringBuilder in order to make the building of the query more performant
         */
        private final StringBuilder query = new StringBuilder();
        /**
         * List of parameters to be used in the query
         */
        private final Map<String, Object> params = new HashMap<>();
        /**
         * Variable to determine if the query has more than 1 field to check
         */
        private boolean isFirst = true;

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
            final Map<SnippetDao.Types, String> orderType = new HashMap<>();
            orderType.put(SnippetDao.Types.REPUTATION, " s.reputation ");
            orderType.put(SnippetDao.Types.DATE, " s.date_created ");
            orderType.put(SnippetDao.Types.TITLE, " s.title ");
            orderType.put(SnippetDao.Types.VOTES, " s.votes ");
            orderTypesMap = Collections.unmodifiableMap(orderType);
        }

        public Builder() {
            this.query.append("SELECT DISTINCT s.id, s.reputation, s.title, s.date_created, s.votes FROM complete_snippets AS s LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = s.id");
        }

        /**
         * Includes a WHERE clause in the query to be built
         * @return Instance of Builder
         */
        private Builder where(){
            this.query.append(" WHERE ");
            return this;
        }

        /**
         * Includes an AND clause in the query to be built
         * @return Instance of Builder
         */
        private Builder and(){
            this.query.append(" AND ");
            return this;
        }

        /**
         * Adds a username condition to be taken into account in the query
         * @param username Username to be searched
         * @return Instance of Builder
         */
        public Builder addUsername(String username){
            if (username != null && !username.isEmpty()){
                this.checkIfFirst();
                this.query.append("LOWER(s.username) LIKE LOWER(:username)");
                this.params.put("username", "%"+username+"%");
            }
            return this;
        }

        /**
         * Adds a snippet title condition to be taken into account in the query
         * @param title Snippet title to be searched
         * @return Instance of Builder
         */
        public Builder addTitle(String title){
            if (title != null && !title.isEmpty()){
                this.checkIfFirst();
                this.query.append("LOWER(s.title) LIKE LOWER(:title)");
                this.params.put("title", "%"+title+"%");
            }
            return this;
        }

        /**
         * Adds a specific tag id condition to be taken into account in the query
         * @param tagId ID of the tag to be searched
         * @return Instance of builder
         */
        public Builder addTag(Long tagId){
            if (tagId != null){
                this.checkIfFirst();
                this.query.append("st.tag_id = :tid");
                this.params.put("tid", tagId);
            }
            return this;
        }

        /**
         * Adds a specific date range condition to be taken into account in the query
         * @param min Minimum date in string format to be searched
         * @param max Maximum date in string format to be searched
         * @return Instance of Builder
         */
        public Builder addDateRange(Calendar min, Calendar max){
            if (min != null || max != null){
                this.checkIfFirst();
            }
            // Escaping the :: to be able to cast objects to date
            if (min != null && max != null){
                this.query.append("s.date_created\\:\\:date >= :mindate AND s.date_created\\:\\:date <= :maxdate");
                this.params.put("mindate", min);
                this.params.put("maxdate", max);
            } else if (min != null){
                this.query.append("s.date_created\\:\\:date >= :mindate");
                this.params.put("mindate", min);
            } else if (max != null){
                this.query.append("s.date_created\\:\\:date <= :maxdate");
                this.params.put("maxdate", max);
            }
            return this;
        }

        /**
         * Adds a specific user reputation condition to be taken into account in the query
         * @param min Minimum user reputation to be searched
         * @param max Maximum user reputation to be searched
         * @return Instance of Builder
         */
        public Builder addReputationRange(Integer min, Integer max){
            if (min != null || max != null){
                this.checkIfFirst();
            }
            if (min != null && max != null){
                this.query.append("s.reputation >= :repmin AND s.reputation <= :repmax");
                this.params.put("repmin", min);
                this.params.put("repmax", max);
            } else if (min != null){
                this.query.append("s.reputation >= :repmin");
                this.params.put("repmin", min);
            } else if (max != null){
                this.query.append("s.reputation <= :repmax");
                this.params.put("repmax", max);
            }
            return this;
        }

        /**
         * Adds a specific snippet vote count condition to be taken into account in the query
         * @param min Minimum snippet vote count to be searched
         * @param max Maximum snippet vote count to be searched
         * @return Instance of Builder
         */
        public Builder addVotesRange(Integer min, Integer max){
            if (min != null || max != null){
                this.checkIfFirst();
            }
            if (min != null && max != null){
                this.query.append("s.votes >= :votemin AND s.votes <= :votemax");
                this.params.put("votemin", min);
                this.params.put("votemax", max);
            } else if (min != null){
                this.query.append("s.votes >= :votemin");
                this.params.put("votemin", min);
            } else if (max != null){
                this.query.append("s.votes <= :votemax");
                this.params.put("votemax", max);
            }
            return this;
        }

        /**
         * Adds a specific language id condition to be taken into account in the query
         * @param languageId ID of the language to be searched
         * @return Instance of Builder
         */
        public Builder addLanguage(Long languageId){
            if (languageId != null){
                this.checkIfFirst();
                this.query.append("s.language_id = :lid");
                this.params.put("lid", languageId);
            }
            return this;
        }

        /**
         * Adds the condition for flagged snippets to be taken into account or not in the query
         * @param includeFlagged Boolean representing if flagged snippets are included in the search
         * @return Instance of Builder
         */
        public Builder addIncludeFlagged(boolean includeFlagged){
            if (!includeFlagged){
                this.checkIfFirst();
                this.query.append("s.flagged = FALSE");
            }
            return this;
        }

        public Builder removeDeleted(){
            this.checkIfFirst();
            this.query.append("s.flagged = FALSE");
            return this;
        }

        /**
         * Adds an order to the query
         * @param type Type of order, field by which the order is done
         * @param order Order used, in which manner the order is done
         * @return Instance of Builder
         */
        public Builder setOrder(SnippetDao.Types type, SnippetDao.Orders order){
            if (order != null && type != null){
                if (!order.equals(SnippetDao.Orders.NO)){
                    this.query.append(" ORDER BY ");
                    if (orderTypesMap.containsKey(type)){
                        this.query.append(orderTypesMap.get(type));
                    } else {
                        this.query.append(orderTypesMap.get(SnippetDao.Types.TITLE));
                    }
                    if (ordersMap.containsKey(order)){
                        this.query.append(ordersMap.get(order));
                    } else {
                        this.query.append(ordersMap.get(SnippetDao.Orders.ASC));
                    }
                }
            }
            return this;
        }

        /**
         * Determines if the condition to be added is the first condition in order to include a WHERE clause or an AND clause
         */
        private void checkIfFirst(){
            if (this.isFirst){
                this.where();
                this.isFirst = false;
            } else {
                this.and();
            }
        }

        public SnippetDeepSearchQuery build() {
            return new SnippetDeepSearchQuery(this.query.toString(), this.params);
        }
    }
}
