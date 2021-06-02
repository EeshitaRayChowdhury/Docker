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
 * Exception raised when no managed object is found with the given key (id or FDN)
 */
public class ManagedObjectNotFoundException extends MyApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MANAGED_OBJECT_NOT_FOUND;

    public ManagedObjectNotFoundException(final String message) {
        super("Managed object not found", message, ERROR_CODE);
    }

}
