package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.validations.UniqueTagOrLang;


public class AdminAddForm {
    private String[] languages;
    private String[] tags;

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
