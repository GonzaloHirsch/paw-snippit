package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.security.acl.LastOwnerException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FollowingJpaDaoImpl implements FollowingDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        Optional<User> user = em.createQuery("SELECT u from User u JOIN FETCH u.followedTags WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList().stream().findFirst();
        return user.map(User::getFollowedTags).orElse(Collections.emptyList());
    }

    @Override
    public Collection<Tag> getMostPopularFollowedTagsForUser(long userId, int amount) {
        Query nativeQuery = this.em.createNativeQuery(
                "SELECT DISTINCT st.tag_id, COUNT(st.snippet_id) " +
                    "FROM follows AS f LEFT OUTER JOIN snippet_tags AS st ON f.tag_id = st.tag_id " +
                    "LEFT OUTER JOIN snippets AS s ON s.id = st.snippet_id " +
                    "WHERE f.user_id = :userId AND s.deleted = FALSE " +
                    "GROUP BY st.tag_id ORDER BY COUNT(st.snippet_id) DESC"
        )
                .setParameter("userId", userId)
                .setFirstResult(0)
                .setMaxResults(amount);

        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
        if (filteredIds.size() > 0) {
            final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds", Tag.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    @Override
    public void followTag(long userId, long tagId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Tag> tag = Optional.ofNullable(this.em.find(Tag.class, tagId));

        if (user.isPresent() && tag.isPresent() && !user.get().getFollowedTags().contains(tag.get())) {
            user.get().followTag(tag.get());
            this.em.persist(user.get());
        }
    }

    @Override
    public void unfollowTag(long userId, long tagId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Tag> tag = Optional.ofNullable(this.em.find(Tag.class, tagId));

        if (user.isPresent() && tag.isPresent() && user.get().getFollowedTags().contains(tag.get())) {
            user.get().unfollow(tag.get());
            this.em.persist(user.get());
        }
    }

    @Override
    public boolean userFollowsTag(long userId, long tagId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Tag> tag = Optional.ofNullable(this.em.find(Tag.class, tagId));

        if (user.isPresent() && tag.isPresent()) {
            return user.get().getFollowedTags().contains(tag.get());
        }
        return false;
    }
}
