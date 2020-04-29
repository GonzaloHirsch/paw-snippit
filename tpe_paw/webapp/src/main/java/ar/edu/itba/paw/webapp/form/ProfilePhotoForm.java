package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.File;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ProfilePhotoForm {

    @File(maxSize = 1048576)
    @NotNull
    private MultipartFile file;

    public MultipartFile getFile() {return this.file; }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
