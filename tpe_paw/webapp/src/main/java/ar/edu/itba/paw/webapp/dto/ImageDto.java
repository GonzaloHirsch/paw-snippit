package ar.edu.itba.paw.webapp.dto;

public class ImageDto {

    private byte[] data;

    public static ImageDto fromImage(final byte[] image){
        final ImageDto dto = new ImageDto();

        dto.data = image;

        return dto;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
