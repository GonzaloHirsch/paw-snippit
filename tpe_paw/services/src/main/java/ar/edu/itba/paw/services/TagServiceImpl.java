package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private FollowingDao followingDao;

    @Override
    public Collection<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        return followingDao.getFollowedTagsForUser(userId);
    }

    @Override
    public void followTag(long userId, long tagId) {
        followingDao.followTag(userId, tagId);
    }

    @Override
    public void unfollowTag(long userId, long tagId) {
        followingDao.unfollowTag(userId, tagId);
    }

}