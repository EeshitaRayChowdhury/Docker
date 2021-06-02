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

import com.ericsson.oss.itpf.sdk.security.accesscontrol.SecurityViolationException;
import com.ericsson.services.enm_sample_app.exceptions.ErrorCode;
import com.ericsson.services.enm_sample_app.rest.exceptions.ErrorMessageResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This exception mapper build the response for {@link SecurityViolationException}
 */
public class SecurityViolationExceptionMapper implements ExceptionMapper<SecurityViolationException> {

    @Override
    public Response toResponse(final SecurityViolationException exception) {

        final ErrorMessageResponse errorResponse =
                new ErrorMessageResponse("Access Denied", exception.getMessage(), ErrorCode.ACCESS_DENIED.getCode());

        return Response.status(Response.Status.FORBIDDEN)
                .header("Content-Type", "application/json")
                .entity(errorResponse).build();
    }
}
