package com.ticketflow.eventmanager.event.exception.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorMessage {

    private String code;

    private String message;

}
