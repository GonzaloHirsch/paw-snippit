package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Optional<Snippet> getSnippetById(String id);
    Optional<Collection<Snippet>> getSnippetByTag(String tag);
    Collection<Snippet> getSnippetByName(String name);
    Collection<Snippet> getSnippetByContent(String code);
}
