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
package com.ericsson.services.enm_sample_app.rest.resource;

import com.ericsson.services.enm_sample_app.api.dto.AttributeDTO;
import com.ericsson.services.enm_sample_app.api.dto.ManagedObjectDTO;
import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException;
import com.ericsson.services.enm_sample_app.service.ManagedObjectsService;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * REST endpoint for Managed Objects service
 */
@Path("/managed-object/v1")
@Produces("application/json")
@RequestScoped
public class ManagedObjectRestResource {

    @Inject
    private ManagedObjectsService service;

    @Inject
    private Logger logger;

    /**
     * This attribute will be set by the setUserId method when this request
     * bean is created by JAX-RS container with the user in the header
     */
    private String userId = null;

    @POST
    @Consumes("application/json")
    public Response createRootManagedObject(final ManagedObjectDTO managedObject) {
        validateUserHeader();
        return Response
                .status(Response.Status.CREATED)
                .entity(service.createRootManagedObject(managedObject))
                .build();
    }

    @POST
    @Path("/{fdn}")
    @Consumes("application/json")
    public Response createChildManagedObject(@PathParam("fdn") final String fdn, final ManagedObjectDTO managedObject) {
        validateUserHeader();
        return Response
                .status(Response.Status.CREATED)
                .entity(service.createChildManagedObject(fdn, managedObject))
                .build();
    }

    @PUT
    @Path("/{fdn}")
    @Consumes("application/json")
    public Response updateManagedObject(@PathParam("fdn") final String fdn, final ManagedObjectDTO managedObject) {
        validateUserHeader();
        managedObject.setFdn(fdn);
        return Response
                .ok(service.updateManagedObject(managedObject))
                .build();
    }

    @GET
    @Path("/{fdn}")
    public Response getManagedObject(@PathParam("fdn") final String fdn) {
        validateUserHeader();
        return Response
                .ok(service.getManagedObject(fdn))
                .build();
    }

    @DELETE
    @Path("/{fdn}")
    public Response deleteManagedObject(@PathParam("fdn") final String fdn) {
        validateUserHeader();
        service.deleteManagedObject(fdn);
        return Response.noContent().build();
    }

    @GET
    @Path("/{namespace}/{type}")
    public Response queryManagedObjects(@PathParam("namespace") final String namespace,
                                        @PathParam("type") final String type,
                                        @QueryParam("limit") final Integer limit) {

        validateUserHeader();
        return Response
                .ok(service.queryManagedObjects(namespace, type, limit))
                .build();
    }

    @POST
    @Path("/{namespace}/{type}")
    @Consumes("application/json")
    public Response queryManagedObjects(@PathParam("namespace") final String namespace,
                                        @PathParam("type") final String type,
                                        @QueryParam("limit") final Integer limit,
                                        final AttributeDTO[] attributes) {

        validateUserHeader();
        return Response
                .ok(service.queryManagedObjects(namespace, type, limit, attributes))
                .build();
    }

    /**
     * This setter is used by Resteasy to set the userId attribute with the vaue of the header X-Tor-UserID
     * @param userId {String}
     */
    @HeaderParam("X-Tor-userId")
    public void setUserId(final String userId) {
        logger.debug("User logged in is {}", userId);
        this.userId = userId.toLowerCase();
    }

    /**
     * If the User Header is empty, throws an exception
     */
    private void validateUserHeader() {
        if (userId == null) {
            throw new UnauthenticatedRequestException();
        }
    }

}
