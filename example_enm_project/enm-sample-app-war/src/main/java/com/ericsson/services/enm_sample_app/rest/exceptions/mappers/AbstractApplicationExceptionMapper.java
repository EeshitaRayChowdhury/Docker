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

import com.ericsson.services.enm_sample_app.exceptions.MyApplicationException;
import com.ericsson.services.enm_sample_app.rest.exceptions.ErrorMessageResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * <p>This is an abstract implementation of RestEasy {@link ExceptionMapper} to build a REST response
 * for any application exception (specializations of {@link MyApplicationException}.</p>
 * <p>The {@link #toResponse(MyApplicationException)} method will be called once any non caught exception of type T is raised</p>
 * @param <T> The specialized type of {@link MyApplicationException} to be mapped
 */
public abstract class AbstractApplicationExceptionMapper<T extends MyApplicationException> implements ExceptionMapper<T> {

    /**
     * This method defines the status code for the response. By default we assume Server Error.
     * You can override this method in your specialized Mappers to provided the proper status code
     * @return HTTP status code to be used in the response
     */
    protected Integer getStatusCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

    /**
     * This method takes the specialized exception and build a JSON response using the errorCode, title and message provided.
     * @param exception exception to be parsed
     * @return REST response
     */
    @Override
    public Response toResponse(final T exception) {

        final ErrorMessageResponse errorResponse =
                new ErrorMessageResponse(exception);

        return Response.status(getStatusCode())
                .header("Content-Type", "application/json")
                .entity(errorResponse).build();
    }
}
