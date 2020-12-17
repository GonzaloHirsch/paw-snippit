package ar.edu.itba.paw.webapp.utility;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Converter to and from LocalDate for the DTOs
 */
public class LocalDateConverter implements ParamConverter<LocalDate> {
    // We expect ISO format dates
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public LocalDate fromString(String s) {
        if (s == null || s.isEmpty()) return null;
        return LocalDate.parse(s, DF);
    }

    @Override
    public String toString(LocalDate localDate) {
        if (localDate == null) return null;
        return DF.format(localDate);
    }
}
