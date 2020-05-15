package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.TemplateService;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public String merge(String templateLocation, Map<String, Object> data, Locale locale) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }

        if (!data.containsKey("messages")) {
            data.put("messages", this.messageSource);
        }

        if (!data.containsKey("locale")) {
            data.put("locale", locale);
        }
        return VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine,
                        templateLocation, data);
    }
}