/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.services.enm_sample_app.exceptions;

/**
 * Base exception for this service. All application exceptions should inherit this one
 */
public class MyApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String title;

    MyApplicationException(final String title, final String message, final ErrorCode errorCode) {
        this(title, message, null, errorCode);
    }

    MyApplicationException(final String title, final String message, final Throwable cause, final ErrorCode errorCode) {
        super(message, cause);
        this.title = title;
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode.getCode();
    }

    public String getTitle() {
        return title;
    }
}
