package com.ticketflow.eventmanager.event.exception.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventErrorCode {

    CATEGORY_NOT_FOUND("EVT_ERR_1"),
    ARTIST_ALREADY_REGISTERED("EVT_ERR_2");


    private final String code;

    public ErrorCode withParams(Object... parameters) {
        return new ErrorCode(this.code, parameters);
    }
}
