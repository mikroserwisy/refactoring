package pl.sages.platform.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ExceptionResponseBuilder exceptionResponseBuilder;

    @Autowired
    public GlobalExceptionHandler(ExceptionResponseBuilder exceptionResponseBuilder) {
        this.exceptionResponseBuilder = exceptionResponseBuilder;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception exception, Locale locale) {
        log.error(exception.getMessage(), exception);
        return exceptionResponseBuilder.buildResponse(exception, INTERNAL_SERVER_ERROR, locale);
    }

}
