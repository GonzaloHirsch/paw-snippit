package ar.edu.itba.paw.models;

public class Role {
    private long id;
    private String name;

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }

    public String getName() { return name; }
}
