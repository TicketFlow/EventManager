package com.ticketflow.eventmanager.event.exception.handler;

import com.ticketflow.eventmanager.event.exception.util.ErrorMessage;
import com.ticketflow.eventmanager.event.exception.util.GeneralErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessage handleAuthenticationException(AuthenticationException ex, HttpServletResponse response) {
        log.error(ex.getMessage(), ex);

        GeneralErrorCode errorCode = GeneralErrorCode.ACCESS_DENIED_ERROR;
        String message = messageSource.getMessage(errorCode.name(), null, Locale.getDefault());
        String code = errorCode.getCode();

        return new ErrorMessage(code, message);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleDateTimeParseException(DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);

        GeneralErrorCode errorCode = GeneralErrorCode.INVALID_DATE_FORMAT;
        String message = messageSource.getMessage(errorCode.name(), null, Locale.getDefault());
        String code = errorCode.getCode();

        return new ErrorMessage(code, message);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage badRequestHandler(BadRequestException ex) {
        log.error(ex.getMessage(), ex);

        GeneralErrorCode errorCode = GeneralErrorCode.BAD_REQUEST_ERROR;
        String message = messageSource.getMessage(errorCode.name(), null, Locale.getDefault());
        String code = errorCode.getCode();

        return new ErrorMessage(code, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentHandler(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);

        GeneralErrorCode errorCode = GeneralErrorCode.ILLEGAL_ARGUMENT_ERROR;
        String message = messageSource.getMessage(errorCode.name(), null, Locale.getDefault());
        String code = errorCode.getCode();

        return new ErrorMessage(code, message);
    }
}