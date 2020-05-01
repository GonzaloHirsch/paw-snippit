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
    private Object[] params;

    public String getQuery() {
        return this.query;
    }

    public Object[] getParams() {
        return this.params;
    }

    /**
     * Private constructor to follow the builder patter
     *
     * @param query  Query to be used in the database
     * @param params List with the parameters in order
     */
    private SnippetDeepSearchQuery(String query, List<Object> params) {
        this.query = query;
        this.params = params.toArray();
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
        private final List<Object> params = new ArrayList<>();

        public Builder() {
            this.query.append("SELECT DISTINCT s.id, s.user_id, s.username, s.reputation, s.code, s.title, s.description, s.language, s.date_created, s.icon, s.votes FROM complete_snippets AS s");
        }

        public Builder where(){
            this.query.append(" WHERE ");
            return this;
        }

        public Builder and(){
            this.query.append(" AND ");
            return this;
        }

        public Builder addDateRange(Calendar min, Calendar max){
            if (min != null && max != null){
                this.query.append(" s.date_created::date >= date ? AND s.date_created::date <= date ? ");
                this.params.add(min.getTime().getTime());
                this.params.add(max.getTime().getTime());
            } else if (min != null){
                this.query.append(" s.date_created::date >= date ? ");
                this.params.add(min.getTime().getTime());
            } else if (max != null){
                this.query.append(" s.date_created::date <= date ? ");
                this.params.add(max.getTime().getTime());
            }
            return this;
        }

        public Builder addReputationRange(Integer min, Integer max){
            if (min != null && max != null){
                this.query.append(" s.reputation >= ? AND s.reputation <= ? ");
                this.params.add(min);
                this.params.add(max);
            } else if (min != null){
                this.query.append(" s.reputation >= ? ");
                this.params.add(min);
            } else if (max != null){
                this.query.append(" s.reputation <= ? ");
                this.params.add(max);
            }
            return this;
        }

        public Builder addVotesRange(Integer min, Integer max){
            if (min != null && max != null){
                this.query.append(" s.votes >= ? AND s.votes <= ? ");
                this.params.add(min);
                this.params.add(max);
            } else if (min != null){
                this.query.append(" s.votes >= ? ");
                this.params.add(min);
            } else if (max != null){
                this.query.append(" s.votes <= ? ");
                this.params.add(max);
            }
            return this;
        }

        public Builder addLanguage(String language){
            if (language != null){
                this.query.append(" LOWER(s.language) = LOWER(?) ");
                this.params.add(language);
            }
            return this;
        }

        public Builder setOrder(){
            this.query.append(" ORDER BY s.id ASC");
            return this;
        }

        public Builder setPaging(int page, int pageSize){
            this.query.append(" LIMIT ? OFFSET ?");
            params.add(pageSize);
            params.add(pageSize * (page - 1));
            return this;
        }

        public SnippetDeepSearchQuery build() {
            return new SnippetDeepSearchQuery(this.query.toString(), this.params);
        }
    }
}
