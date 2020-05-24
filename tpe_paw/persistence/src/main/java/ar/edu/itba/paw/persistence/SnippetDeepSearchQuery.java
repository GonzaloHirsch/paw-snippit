package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;

import java.util.*;

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
    public static class Builder {
        /**
         * StringBuilder in order to make the building of the query more performant
         */
        private final StringBuilder query = new StringBuilder();
        /**
         * List of parameters to be used in the query
         */
        private final Map<String, Object> params = new HashMap<>();

        public Builder() {
            this.query.append("SELECT DISTINCT s.id, us.reputation, s.title, s.date_created, s.votes FROM snippets AS s INNER JOIN users AS us ON us.id = s.user_id LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = s.id ");
        }

        public Builder where(){
            this.query.append(" WHERE ");
            return this;
        }

        public Builder and(){
            this.query.append(" AND ");
            return this;
        }

        public Builder addUsername(String username){
            if (username != null && !username.isEmpty()){
                this.query.append(" LOWER(us.username) LIKE LOWER(:username) ");
                this.params.put("username", "%"+username+"%");
            }
            return this;
        }

        public Builder addTitle(String title){
            if (title != null && !title.isEmpty()){
                this.query.append(" LOWER(s.title) LIKE LOWER(:title) ");
                this.params.put("title", "%"+title+"%");
            }
            return this;
        }

        public Builder addTag(Long tagId){
            if (tagId != null){
                this.query.append(" st.tag_id = :tid ");
                this.params.put("tid", tagId);
            }
            return this;
        }

        public Builder addDateRange(String min, String max){
            if (min != null && max != null){
                this.query.append(" s.date_created::date >= :mindate::date AND s.date_created::date <= :maxdate::date ");
                this.params.put("mindate", min);
                this.params.put("maxdate", max);
            } else if (min != null){
                this.query.append(" s.date_created::date >= :mindate::date ");
                this.params.put("mindate", min);
            } else if (max != null){
                this.query.append(" s.date_created::date <= :maxdate::date ");
                this.params.put("maxdate", max);
            }
            return this;
        }

        public Builder addReputationRange(Integer min, Integer max){
            if (min != null && max != null){
                this.query.append(" us.reputation >= :repmin AND us.reputation <= :repmax ");
                this.params.put("repmin", min);
                this.params.put("repmax", max);
            } else if (min != null){
                this.query.append(" us.reputation >= :repmin ");
                this.params.put("repmin", min);
            } else if (max != null){
                this.query.append(" us.reputation <= :repmax ");
                this.params.put("repmax", max);
            }
            return this;
        }

        public Builder addVotesRange(Integer min, Integer max){
            if (min != null && max != null){
                this.query.append(" s.votes >= :votemin AND s.votes <= :votemax ");
                this.params.put("votemin", min);
                this.params.put("votemax", max);
            } else if (min != null){
                this.query.append(" s.votes >= :votemin ");
                this.params.put("votemin", min);
            } else if (max != null){
                this.query.append(" s.votes <= :votemax ");
                this.params.put("votemax", max);
            }
            return this;
        }

        public Builder addLanguage(Long languageId){
            if (languageId != null){
                this.query.append(" s.language_id = :lid ");
                this.params.put("lid", languageId);
            }
            return this;
        }

        public Builder addIncludeFlagged(boolean includeFlagged){
            if (!includeFlagged){
                this.query.append(" s.flagged = 0 ");
            }
            return this;
        }

        public Builder setOrder(String order, String sort){
            this.query.append(" ORDER BY ");
            switch(order.toLowerCase()){
                case "reputation":
                    this.query.append(" us.reputation ");
                    break;
                case "votes":
                    this.query.append(" s.votes ");
                    break;
                case "date":
                    this.query.append(" s.date_created ");
                    break;
                case "title":
                default:
                    this.query.append(" s.title ");
                    break;
            }
            switch(sort.toLowerCase()){
                case "desc":
                    this.query.append(" DESC ");
                    break;
                case "asc":
                default:
                    this.query.append(" ASC ");
                    break;
            }
            return this;
        }

        public SnippetDeepSearchQuery build() {
            return new SnippetDeepSearchQuery(this.query.toString(), this.params);
        }
    }
}
