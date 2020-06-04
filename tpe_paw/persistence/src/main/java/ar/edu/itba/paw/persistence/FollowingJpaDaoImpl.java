package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
public class FollowingJpaDaoImpl implements FollowingDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        Optional<User> user = em.createQuery("SELECT u from User u JOIN FETCH u.followedTags", User.class).getResultList().stream().findFirst();
        return user.map(User::getFollowedTags).orElse(Collections.emptyList());

        //TODO Remove if sure
//        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
//        return user.map(User::getFollowedTags).orElse(Collections.emptyList());
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
