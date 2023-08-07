package com.freedomfm.singer.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConstraintError implements SubError{
    private String field;
    private String message;

    ConstraintError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
