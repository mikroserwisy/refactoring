package pl.sages.platform.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TextSource {

    private static final String LANGUAGE_SEPARATOR = "_";

    @NonNull
    private final MessageSource messageSource;
    @NonNull
    private final TemplateEngine templateEngine;
    @Value("${platform.available-languages}")
    @Setter
    private List<String> availableLanguages;

    public String getText(String key, String language) {
        Locale locale = language != null ? new Locale(language) : Locale.getDefault();
        return messageSource.getMessage(key, null, locale);
    }

    public String getTextFromTemplate(String templateName, Map<String, Object> variables, String language) {
        return buildFromTemplate(templateName, variables, language);
    }

    private String buildFromTemplate(String templateBaseName, Map<String, Object> variables, String language) {
        Context context = createContext(variables);
        String templateName = getTemplateName(templateBaseName, language);
        return templateEngine.process(templateName, context);
    }

    private Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return context;
    }

    private String getTemplateName(String templateBaseName, String language) {
        return availableLanguages.contains(language) ? templateBaseName + LANGUAGE_SEPARATOR + language : templateBaseName;
    }

}
