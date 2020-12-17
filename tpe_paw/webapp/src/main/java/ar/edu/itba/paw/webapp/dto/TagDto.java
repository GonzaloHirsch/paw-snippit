package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Tag;

import javax.ws.rs.core.UriInfo;

public class TagDto {
    private String name;
    private Long id;

    public static TagDto fromTag(Tag tag, UriInfo uriInfo){
        final TagDto dto = new TagDto();

        dto.id = tag.getId();
        dto.name = tag.getName();

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
