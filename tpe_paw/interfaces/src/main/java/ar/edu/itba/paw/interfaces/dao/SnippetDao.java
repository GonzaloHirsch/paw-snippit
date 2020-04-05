package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;

public interface SnippetDao {
    Collection<Snippet> getSnippetByTag(String tag);
}
