package ar.edu.itba.paw.webapp.utility;


import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if (value == null)
            return null;
        return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
    }

    @Override
    public String toString(LocalDate value) {
        if (value == null)
            return null;
        return DateTimeFormatter.ISO_DATE.format(value);
    }

}
