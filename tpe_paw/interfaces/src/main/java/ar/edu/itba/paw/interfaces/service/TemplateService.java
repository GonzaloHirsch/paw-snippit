package ar.edu.itba.paw.interfaces.service;

import java.util.Locale;
import java.util.Map;

public interface TemplateService {

    String merge(String templateLocation, Map<String, Object> data, Locale locale);
}
