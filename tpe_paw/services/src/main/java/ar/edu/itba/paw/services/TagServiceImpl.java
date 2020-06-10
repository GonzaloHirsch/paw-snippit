package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private FollowingDao followingDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Collection<Tag> getAllTags(boolean showEmpty, int page, int pageSize) {
        return this.tagDao.getAllTags(showEmpty, page, pageSize);
    }

    @Override
    public Collection<Tag> getAllTags() {
        return this.tagDao.getAllTags();
    }

    @Override
    public int getAllTagsCountByName(String name, boolean showEmpty) {
        return this.tagDao.getAllTagsCountByName(name, showEmpty);
    }

    @Override
    public int getAllTagsCount(boolean showEmpty) {
        return this.tagDao.getAllTagsCount(showEmpty);
    }

    @Override
    public Collection<Tag> findTagsByName(String name, boolean showEmpty, int page, int pageSize) {
        return this.tagDao.findTagsByName(name, showEmpty, page, pageSize);
    }

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        return this.followingDao.getFollowedTagsForUser(userId);
    }

    @Override
    public Collection<Tag> getMostPopularFollowedTagsForUser(long userId, int amount) {
        return this.followingDao.getMostPopularFollowedTagsForUser(userId, amount);
    }

    @Override
    public Optional<Tag> findTagById(long tagId) {
        return this.tagDao.findById(tagId);
    }

    @Override
    public void followTag(long userId, long tagId) {
        this.followingDao.followTag(userId, tagId);
    }

    @Override
    public void unfollowTag(long userId, long tagId) {
        this.followingDao.unfollowTag(userId, tagId);
    }

    @Transactional
    @Override
    public Collection<Tag> addTagsToSnippet(Long snippetId, Collection<String> tagNameList){
        ArrayList<Tag> tagList = new ArrayList<>();

        if(tagNameList == null)
            return tagList;

        for(String tagName : tagNameList) {
            Optional<Tag> tag = tagDao.findByName(tagName);
            if(tag.isPresent()) {
                tagDao.addSnippetTag(snippetId, tag.get().getId());
                tagList.add(tag.get());
            }
        }
        return tagList;
    }

    @Transactional
    @Override
    public void addTags(List<String> tags) {
        this.tagDao.addTags(tags);
    }

    @Override
    public boolean tagExists(String tag) {
        return this.tagDao.findByName(tag).isPresent();
    }

    @Override
    public boolean tagExists(long tagId) {
        return this.tagDao.findById(tagId).isPresent();
    }

    @Transactional
    @Override
    public void removeTag(final long tagId) {
        this.tagDao.removeTag(tagId);
    }

    @Transactional
    @Override
    public void updateFollowing(long userId, long tagId, boolean followed) {
        if (followed) {
            this.followTag(userId, tagId);
        } else {
            this.unfollowTag(userId, tagId);
        }
    }

    @Override
    public boolean userFollowsTag(long userId, long tagId) {
        Optional<User> user = this.userDao.findUserById(userId);
        Optional<Tag> tag = this.tagDao.findById(tagId);

        if (user.isPresent() && tag.isPresent()) {
            return user.get().getFollowedTags().contains(tag.get());
        }
        return false;
    }
}