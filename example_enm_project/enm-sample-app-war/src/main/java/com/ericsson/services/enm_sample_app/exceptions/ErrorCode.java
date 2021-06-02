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
 * This enum provides the error codes supported by the application
 */
public enum ErrorCode {

    UNEXPECTED_EXCEPTION(-1),
    MANAGED_OBJECT_NOT_FOUND(1001),
    UNAUTHENTICATED_REQUEST(1002),
    ACCESS_DENIED(1003);

    ErrorCode(Integer code) {
        this.code = code;
    }

    private Integer code;

    /**
     * Integer error code corresponding to this enum value
     * @return integer error code
     */
    public Integer getCode() {
        return code;
    }
}
