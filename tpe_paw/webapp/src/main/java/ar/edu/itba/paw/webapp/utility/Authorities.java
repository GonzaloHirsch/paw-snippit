package ar.edu.itba.paw.webapp.utility;

public enum Authorities {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), REFRESH("REFRESH");

    private final String value;

    private Authorities(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
