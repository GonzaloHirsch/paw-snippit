package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;

import java.util.*;

public class SnippetSearchQuery {
    private String query;
    private Object[] params;

    public String getQuery(){ return this.query; }

    public Object[] getParams(){ return this.params; }

    private SnippetSearchQuery(String query, List<Object> params){
        this.query = query;
        this.params = params.toArray();
    }

    public static class Builder {
        private Map<SnippetDao.Types, String> typeMap = new HashMap<SnippetDao.Types, String>(){{
            put(SnippetDao.Types.TAG, " AS s INNER JOIN snippet_tags AS ts ON s.id = ts.snippet_id INNER JOIN tags AS t ON t.id = ts.tag_id WHERE t.name ILIKE ?");
            put(SnippetDao.Types.TITLE, " AS s WHERE s.title ILIKE ?");
            put(SnippetDao.Types.CONTENT, " AS s WHERE s.code ILIKE ?");
        }};
        private Map<SnippetDao.Locations, String> locationsMap = new HashMap<SnippetDao.Locations, String>(){{
            put(SnippetDao.Locations.HOME, "(SELECT * FROM snippets)");
            put(SnippetDao.Locations.FAVORITES, "(SELECT sn.id, sn.user_id, sn.code, sn.title, sn.description FROM snippets AS sn JOIN favorites AS fav ON fav.snippet_id = sn.id WHERE fav.user_id = ?)");
            put(SnippetDao.Locations.FOLLOWING, "(SELECT sn.id, sn.user_id, sn.code, sn.title, sn.description FROM snippets AS sn JOIN snippet_tags AS st.snippet_id = sn.id JOIN following AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?");
        }};
        private Map<SnippetDao.Orders, String> ordersMap = new HashMap<SnippetDao.Orders, String>(){{
            put(SnippetDao.Orders.ASC, "ASC");
            put(SnippetDao.Orders.DESC, "DESC");
        }};
        private Map<SnippetDao.Types, String> orderTypesMap = new HashMap<SnippetDao.Types, String>(){{
            put(SnippetDao.Types.TAG, " t.name ");
            put(SnippetDao.Types.TITLE, " s.title ");
            put(SnippetDao.Types.CONTENT, " s.code ");
        }};
        private StringBuilder query = new StringBuilder();
        private List<Object> params = new ArrayList<>();

        public Builder(SnippetDao.Locations location, Long userId){
            this.query
                    .append("SELECT s.id, s.user_id, s.code, s.title, s.description FROM ")
                    .append(this.locationsMap.get(location));
            if (userId != null){ params.add(userId); }
        }

        public Builder setType(SnippetDao.Types type, String term){
            this.query.append(this.typeMap.get(type));
            params.add("%" + term + "%");
            return this;
        }

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
