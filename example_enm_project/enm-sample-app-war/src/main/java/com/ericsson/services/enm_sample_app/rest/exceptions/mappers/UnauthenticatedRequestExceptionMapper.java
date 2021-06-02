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
package com.ericsson.services.enm_sample_app.rest.exceptions.mappers;

import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException;

import javax.ws.rs.core.Response;

/**
 * This exception mapper build the response for {@link UnauthenticatedRequestException}
 */
public class UnauthenticatedRequestExceptionMapper extends AbstractApplicationExceptionMapper<UnauthenticatedRequestException> {

    /**
     * For this exception we should return Unauthorized (401) status code
     * @return Unauthorized HTTP status code
     */
    @Override
    protected Integer getStatusCode() {
        return Response.Status.UNAUTHORIZED.getStatusCode();
    }
}
