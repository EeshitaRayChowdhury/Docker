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
 * Exception raised when the REST request is not authenticated
 */
public class UnauthenticatedRequestException extends MyApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.UNAUTHENTICATED_REQUEST;

    private static final String MESSAGE = "You need to be authenticated to use this service!";

    public UnauthenticatedRequestException() {
        super("Not authenticated", MESSAGE, ERROR_CODE);
    }
}
