package com.kuhniverse.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tillkuhn on 27.05.2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST )
public class CaptchaValidationException extends RuntimeException{

    private final CaptchaValidationResult validationResult;

    public CaptchaValidationException(final CaptchaValidationResult validationResult, String message) {
        super(message);
        this.validationResult = validationResult;
    }

    public CaptchaValidationResult getValidationResult() {
        return validationResult;
    }
}
