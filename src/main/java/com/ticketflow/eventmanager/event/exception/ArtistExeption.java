package com.ticketflow.eventmanager.event.exception;

import com.ticketflow.eventmanager.event.exception.util.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ArtistExeption extends ConfigException {

    public ArtistExeption(final ErrorCode errorCode) {
        super(errorCode);
    }
}
