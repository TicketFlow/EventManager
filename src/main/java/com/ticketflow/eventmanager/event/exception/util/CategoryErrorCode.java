package com.ticketflow.eventmanager.event.exception.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {

    CATEGORY_NOT_FOUND("EVT_CAT_ERR_1"),
    CATEGORY_ID_REQUIRED("EVT_CAT_ERR_2"),
    CATEGORY_NAME_DUPLICATED("EVT_CAT_ERR_3"),
    CATEGORY_BEING_USED("EVT_CAT_ERR_4");

    private final String code;

    public ErrorCode withParams(Object... parameters) {
        return new ErrorCode(this.code, parameters);
    }
}