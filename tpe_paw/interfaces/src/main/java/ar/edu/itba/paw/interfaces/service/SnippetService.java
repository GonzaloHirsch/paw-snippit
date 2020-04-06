package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Optional<Snippet> getSnippetById(String id);
    Optional<Collection<Snippet>> getSnippetByTag(String tag);
}
