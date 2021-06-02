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

import com.ericsson.services.enm_sample_app.exceptions.ErrorCode;
import com.ericsson.services.enm_sample_app.rest.exceptions.ErrorMessageResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This is a generic exception mapper that will be used to capture any unhandled exception
 */
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {

        final ErrorMessageResponse errorResponse =
                new ErrorMessageResponse("Unknown Exception", exception.getMessage(), ErrorCode.UNEXPECTED_EXCEPTION.getCode());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .entity(errorResponse).build();
    }

}
