package ar.edu.itba.paw.webapp.dto;

public class BooleanDto {
    private boolean aBoolean;

    public static BooleanDto fromBoolean(boolean aBoolean){
        final BooleanDto dto = new BooleanDto();

        dto.aBoolean = aBoolean;

        return dto;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
