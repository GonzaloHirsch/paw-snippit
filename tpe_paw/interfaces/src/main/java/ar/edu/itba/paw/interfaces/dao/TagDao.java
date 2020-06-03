package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagDao {
    Optional<Tag> findById(final long id);
    Optional<Tag> findByName(final String name);
    Tag addTag(final String name);
    Collection<Tag> findTagsForSnippet(final long snippetId);
    Collection<Tag> getAllTags(boolean showEmpty, int page, int pageSize);
    Collection<Tag> getAllTags();
    int getAllTagsCountByName(String name, boolean showEmpty);
    int getAllTagsCount(boolean showEmpty);

    Collection<Tag> findSpecificTagsByName(Collection<String> tags);

    Collection<Tag> findTagsByName(String name, boolean showEmpty, int page, int pageSize);
    void addSnippetTag(final long snippetId, final long tagId);
    void addTags(final List<String> tags);
    void removeTag(final long tagId);
}
