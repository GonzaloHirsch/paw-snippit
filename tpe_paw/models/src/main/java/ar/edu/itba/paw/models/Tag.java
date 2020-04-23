package ar.edu.itba.paw.models;

public class Tag {

    private long id;
    private String name;

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public long getId() { return id; }

    public String getName() { return name; }

    
}
