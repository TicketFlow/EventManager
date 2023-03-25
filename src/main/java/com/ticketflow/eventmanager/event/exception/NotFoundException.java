package com.ticketflow.eventmanager.event.exception;

import com.ticketflow.eventmanager.event.exception.util.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends ConfigException {

    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
