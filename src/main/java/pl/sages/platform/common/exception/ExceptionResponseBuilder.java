package pl.sages.platform.common.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ExceptionResponseBuilder {

    private MessageSource messageSource;

    @Autowired
    public ExceptionResponseBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ResponseEntity<ExceptionTransferObject> buildResponse(Exception exception, HttpStatus status, Locale locale) {
        String exceptionName = exception.getClass().getSimpleName();
        String description;
        try {
            description = messageSource.getMessage(exception.getClass().getSimpleName(), null, locale);
        } catch (NoSuchMessageException ex) {
            description = exceptionName;
        }
        return ResponseEntity.status(status).body(new ExceptionTransferObject(description));
    }

}
