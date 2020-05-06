package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagDao {
    int PAGE_SIZE = 20;
    Optional<Tag> findById(final long id);
    Optional<Tag> findByName(final String name);
    Tag addTag(final String name);
    Collection<Tag> findTagsForSnippet(final long snippetId);
    Collection<Tag> getAllTags(int page);
    Collection<Tag> getAllTags();
    int getAllTagsCountByName(String name);
    int getAllTagsCount();
    Collection<Tag> findTagsByName(String name, int page);
    void addSnippetTag(final long snippetId, final long tagId);
    void addTags(final List<String> tags);
    void removeTag(final long tagId);
}
