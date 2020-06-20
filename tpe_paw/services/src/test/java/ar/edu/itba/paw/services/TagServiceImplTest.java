package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {

    private static final Long SNIPPET_ID = 10L;
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String NAME_1 = "ALGORITHM";
    private static final String NAME_2 = "JAVA";

    @InjectMocks
    private TagServiceImpl tagService = new TagServiceImpl();

    @Mock
    private TagDao mockTagDao;


    @Test
    public void testAddTagsToSnippetExistingTags() {
        // 1. Setup!
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_1))).thenReturn(Optional.of(new Tag(ID_1, NAME_1)));
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_2))).thenReturn(Optional.of(new Tag(ID_2, NAME_2)));

        // 2. Execute
        Collection<Tag> tagList = tagService.addTagsToSnippet(SNIPPET_ID, Arrays.asList(NAME_1, NAME_2));

        // 3. Asserts!
        Assert.assertNotNull(tagList);
        Assert.assertEquals(2, tagList.size());
        Assert.assertTrue(tagList.contains(new Tag(ID_1, NAME_1)));
        Assert.assertTrue(tagList.contains(new Tag(ID_2, NAME_2)));
    }

    @Test
    public void testAddTagsToSnippetNonExistingTags() {
        // 1. Setup!
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_1))).thenReturn(Optional.empty());
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_2))).thenReturn(Optional.empty());

        // 2. Execute
        Collection<Tag> tagList = tagService.addTagsToSnippet(SNIPPET_ID, Arrays.asList(NAME_1, NAME_2));

        // 3. Asserts!
        Assert.assertNotNull(tagList);
        Assert.assertTrue(tagList.isEmpty());
    }

    @Test
    public void testAddTagsToSnippetMix() {
        // 1. Setup!
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_1))).thenReturn(Optional.of(new Tag(ID_1, NAME_1)));
        Mockito.when(mockTagDao.findByName(Mockito.eq(NAME_2))).thenReturn(Optional.empty());

        // 2. Execute
        Collection<Tag> tagList = tagService.addTagsToSnippet(SNIPPET_ID, Arrays.asList(NAME_1, NAME_2));

        // 3. Asserts!
        Assert.assertNotNull(tagList);
        Assert.assertEquals(1, tagList.size());
        Assert.assertTrue(tagList.contains(new Tag(ID_1, NAME_1)));    }
}


