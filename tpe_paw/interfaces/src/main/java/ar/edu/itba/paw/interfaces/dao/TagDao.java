package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import java.util.Optional;

public interface TagDao {
    Optional<Tag> findById(long id);
    Tag addTag(String name);
}
