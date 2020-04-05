package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;

public interface SnippetDao {
    Snippet getSnippetById(String id);
    Collection<Snippet> getSnippetByTag(String tag);
}
