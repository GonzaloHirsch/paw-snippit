package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagService {
    Collection<Tag> getAllTags(int page, int pageSize);
    Collection<Tag> getAllTags();
    int getAllTagsCountByName(String name);
    int getAllTagsCount();
    Collection<Tag> findTagsByName(String name, int page, int pageSize);
    Collection<Tag> getFollowedTagsForUser(long userId);
    void followTag(long userId, long tagId);
    void unfollowTag(long userId, long tagId);
    Optional<Tag> findTagById(long tagId);
    Collection<Tag> addTagsToSnippet(Long snippetId, Collection<String> tagList);
    void addTags(List<String> tags);
    boolean tagExists(final String tag);
    boolean tagExists(final long tag);
    void removeTag(final long tagId);
    void updateFollowing(long userId, long tagId, boolean followed);
    boolean userFollowsTag(long userId, long tagId);
}