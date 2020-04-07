package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;

public interface SnippetService {
    Collection<Snippet> getSnippetByTag(String tag);
}
