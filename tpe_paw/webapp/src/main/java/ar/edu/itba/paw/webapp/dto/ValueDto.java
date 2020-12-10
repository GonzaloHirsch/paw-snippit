package ar.edu.itba.paw.webapp.dto;

public class ValueDto {
    private int value;

    public static ValueDto fromValue(int value){
        final ValueDto dto = new ValueDto();

        dto.value = value;

        return dto;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
