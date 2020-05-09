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

        public Builder(boolean isCount) {
            if (isCount){
                this.query.append("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = s.id ");
            } else {
                this.query.append("SELECT DISTINCT s.id, s.user_id, s.username, s.reputation, s.lang, s.region, s.verified, s.code, s.title, s.description, s.language, s.date_created, s.icon, s.votes, s.language_id, s.flagged FROM complete_snippets AS s LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = s.id ");
            }
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
            if (username != null){
                this.query.append(" LOWER(s.username) LIKE LOWER(?) ");
                this.params.add("%"+username+"%");
            }
            return this;
        }

        public Builder addTitle(String title){
            if (title != null){
                this.query.append(" LOWER(s.title) LIKE LOWER(?) ");
                this.params.add("%"+title+"%");
            }
            return this;
        }

        public Builder addTag(Long tagId){
            if (tagId != null){
                this.query.append(" st.tag_id = ? ");
                this.params.add(tagId);
            }
            return this;
        }

        public Builder addDateRange(String min, String max){
            if (min != null && max != null){
                this.query.append(" s.date_created::date >= ?::date AND s.date_created::date <= ?::date ");
                this.params.add(min);
                this.params.add(max);
            } else if (min != null){
                this.query.append(" s.date_created::date >= ?::date ");
                this.params.add(min);
            } else if (max != null){
                this.query.append(" s.date_created::date <= ?::date ");
                this.params.add(max);
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

        public Builder addLanguage(Long languageId){
            if (languageId != null){
                this.query.append(" s.language_id = ? ");
                this.params.add(languageId);
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
                    this.query.append(" s.reputation ");
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
