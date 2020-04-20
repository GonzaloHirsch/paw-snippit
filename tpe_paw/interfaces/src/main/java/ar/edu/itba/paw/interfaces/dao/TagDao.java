package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.Optional;

public interface TagDao {
    Optional<Tag> findById(long id);
    Tag addTag(String name);
    Collection<Tag> findTagsForSnippet(long snippetId);
    void addSnippetTag(Snippet snippet, Tag tag);
}
