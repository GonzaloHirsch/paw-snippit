package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Collection<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        return followingDao.getFollowedTagsForUser(userId);
    }

    @Override
    public Optional<Tag> findTagById(long tagId) {
        return tagDao.findTagById(tagId);
    }

    @Override
    public void followTag(long userId, long tagId) {
        followingDao.followTag(userId, tagId);
    }

    @Override
    public void unfollowTag(long userId, long tagId) {
        followingDao.unfollowTag(userId, tagId);
    }

    @Override
    public Collection<Tag> addTagsToSnippet(Long snippetId, Collection<Long> tagIdList){
        ArrayList<Tag> tagList = new ArrayList<>();

        if(tagIdList == null)
            return tagList;

        for(Long tagId : tagIdList) {
            Optional<Tag> tag = tagDao.findTagById(tagId);
            if(tag.isPresent()) {
                tagDao.addSnippetTag(snippetId, tagId);
                tagList.add(tagDao.findTagById(tagId).get());
            }
        }
        return tagList;
    }

    @Override
    public void addTags(List<String> tags) {

    }


}