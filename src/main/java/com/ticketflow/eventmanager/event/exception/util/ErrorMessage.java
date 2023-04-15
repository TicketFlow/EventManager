package com.ticketflow.eventmanager.event.exception.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private String code;

    private String message;

}
