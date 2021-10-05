package com.raytotti.convertcurrency.commons.exception;

import lombok.Getter;

@Getter
public final class FieldRequestError {

    private final String field;
    private final String error;

    public FieldRequestError(String field, String error) {
        this.field = field;
        this.error = error;
    }

}
