package com.kuhniverse.domain;

/**
 * Enum constans to represent captcha validation results
 *
 * Created by tillkuhn on 27.05.2015.
 */
public enum CaptchaValidationResult {

    VALID_IMAGE,
    FAILED_IMAGE (true),
    VALID_AUDIO,
    FAILED_AUDIO (true),
    MISSING_FIELD_NAME (true),
    MISSING_VALUE (true),
    INSUFFICIENT_OPTION_COUNT(true),
    OTHER_ERROR(true);

    boolean failed = false;
    CaptchaValidationResult(boolean failed) {
        this.failed = failed;
    }

    CaptchaValidationResult() {

    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isValid() {
        return ! failed;
    }

}
