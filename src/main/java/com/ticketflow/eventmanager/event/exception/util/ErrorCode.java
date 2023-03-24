package com.ticketflow.eventmanager.event.exception.util;

import java.io.Serializable;

public record ErrorCode(String code, Object[] parameters) implements Serializable {

}