package com.ticketflow.eventmanager.event.exception.handler;


import com.ticketflow.eventmanager.event.exception.CategoryException;
import com.ticketflow.eventmanager.event.exception.EventException;
import com.ticketflow.eventmanager.event.exception.NotFoundException;
import com.ticketflow.eventmanager.event.exception.util.ErrorCode;
import com.ticketflow.eventmanager.event.exception.util.ErrorMessage;
import com.ticketflow.eventmanager.event.service.JwtUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    private final JwtUserAuthenticationService jwtUserAuthenticationService;

    private static final String LOG_ERROR_STRING = "ErrorCode {}";


    @ExceptionHandler(EventException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleEventException(EventException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String code = errorCode.code();
        String message = resolveErrorMessage(code, errorCode.parameters());

        log.error(LOG_ERROR_STRING, ex.getMessage(), ex);

        return new ErrorMessage(code, message);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEventException(NotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String code = errorCode.code();
        String message = resolveErrorMessage(code, errorCode.parameters());

        log.error(LOG_ERROR_STRING, ex.getMessage(), ex);

        return new ErrorMessage(code, message);
    }

    @ExceptionHandler(CategoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCategoryException(CategoryException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String code = errorCode.code();
        String message = resolveErrorMessage(code, errorCode.parameters());

        log.error(LOG_ERROR_STRING, ex.getMessage(), ex);

        return new ErrorMessage(code, message);
    }

    private String resolveErrorMessage(String code, Object[] parameters) {
        Locale locale = jwtUserAuthenticationService.getCurrentUserLocale();
        return messageSource.getMessage(code, parameters, locale);
    }
}