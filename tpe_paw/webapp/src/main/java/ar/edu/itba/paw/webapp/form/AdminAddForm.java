package ar.edu.itba.paw.webapp.form;

import java.util.List;

public class AdminAddForm {
    private List<String> languages;
    private List<String> tags;

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
