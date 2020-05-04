package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagDao {
    Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);
    Tag addTag(String name);
    Collection<Tag> findTagsForSnippet(long snippetId);
    Collection<Tag> getAllTags();

    void addSnippetTag(long snippetId, long tagId);
    void addTags(List<String> tags);

    void removeTag(final long tagId);
}
