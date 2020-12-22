package ar.edu.itba.paw.webapp.utility;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * Provider for the LocalDate Converter required to convert to and from LocalDate in DTOs
 */
@Provider
public class LocalDateConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
        if (aClass.equals(LocalDate.class)){
            return (ParamConverter<T>) new LocalDateConverter();
        }
        return null;
    }
}
