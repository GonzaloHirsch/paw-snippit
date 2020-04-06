package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetDao {
    Optional<Snippet> getSnippetById(String id);
    Optional<Collection<Snippet>> getSnippetByTag(String tag);
}
