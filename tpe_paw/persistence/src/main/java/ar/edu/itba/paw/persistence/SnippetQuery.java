package ar.edu.itba.paw.persistence;

import java.util.ArrayList;
import java.util.List;

public class SnippetQuery {
    private String query;
    private Object[] params;

    public String getQuery(){ return this.query; }

    public Object[] getParams(){ return this.params; }

    private SnippetQuery(String query, List<Object> params){
        this.query = query;
        this.params = params.toArray();
    }

    public static class Builder {
        private static String QUERY_SELECT = "SELECT id, ownerId, code, title, description FROM ";

        private static String TAG_QUERY = " AS s INNER JOIN snippet_tags AS ts ON s.id = ts.snippetId INNER JOIN tags AS t WHERE t.id = ts.tagId AND t.name LIKE ?";
        private static String TITLE_QUERY = " AS s WHERE s.title LIKE ?";
        private static String CONTENT_QUERY = " AS s WHERE s.code LIKE ?";
        private static String HOME_SOURCE = "(SELECT * FROM snippets)";
        private static String FAVORITES_SOURCE = "(SELECT sn.id, sn.ownerId, sn.code, sn.title, sn.description FROM snippets AS sn JOIN favorites AS fav ON fav.snippet_id = sn.id WHERE fav.user_id = ?)";
        private static String FOLLOWING_SOURCE = "(SELECT sn.id, sn.ownerId, sn.code, sn.title, sn.description FROM snippets AS sn JOIN snippet_tags AS st.snippet_id = sn.id JOIN following AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?";

        private StringBuilder query;
        private List<Object> params = new ArrayList<>();

        public Builder(){
            this.query.append(QUERY_SELECT);
        }

        public Builder setType(String type, String term){
            switch (type){
                case "tag":
                    this.query.append(TAG_QUERY);
                    break;
                case "title":
                    this.query.append(TITLE_QUERY);
                    break;
                case "content":
                    this.query.append(CONTENT_QUERY);
                    break;
            }
            if (term != null){
                params.add("%" + term + "%");
            }
            return this;
        }

        public Builder setSource(String source, String userId){
            switch (source){
                case "home":
                    this.query.append(HOME_SOURCE);
                    break;
                case "favorites":
                    this.query.append(FAVORITES_SOURCE);
                    break;
                case "following":
                    this.query.append(FOLLOWING_SOURCE);
                    break;
            }
            if (userId != null){
                params.add(userId);
            }
            return this;
        }

        public SnippetQuery build(){
            return new SnippetQuery(this.query.toString(), this.params);
        }
    }
}
