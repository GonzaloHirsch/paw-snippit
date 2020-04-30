package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagDao {
    Optional<Tag> findById(long id);
    Tag addTag(String name);
    Collection<Tag> findTagsForSnippet(long snippetId);
    Collection<Tag> getAllTags();

    Optional<Tag> findTagById(long tagId);
    void addSnippetTag(long snippetOd, long tagId);
    void addTags(List<String> tags);
    boolean isUniqueTag(String tag);
}
