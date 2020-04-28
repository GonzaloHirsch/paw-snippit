package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ProfilePhotoForm {

    @NotNull
    private MultipartFile file;

    public MultipartFile getFile() {return this.file; }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
