package com.ticketflow.eventmanager.event.exception.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventErrorCode {

    ;

    private final String code;

    public ErrorCode withParams(Object... parameters) {
        return new ErrorCode(this.code, parameters);
    }
}
