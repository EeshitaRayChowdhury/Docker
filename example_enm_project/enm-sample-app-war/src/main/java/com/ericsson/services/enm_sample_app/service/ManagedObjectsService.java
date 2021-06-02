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
package com.ericsson.services.enm_sample_app.service;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.DeleteOptions;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.Restriction;
import com.ericsson.oss.itpf.datalayer.dps.query.TypeRestrictionBuilder;
import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.itpf.sdk.security.accesscontrol.annotation.Authorize;
import com.ericsson.services.enm_sample_app.api.dto.AttributeDTO;
import com.ericsson.services.enm_sample_app.api.dto.ManagedObjectDTO;
import com.ericsson.services.enm_sample_app.exceptions.ManagedObjectNotFoundException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer providing the CRUD and query operations for DPS
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManagedObjectsService {

    @EServiceRef
    private DataPersistenceService dps;

    public static final String MANAGED_OBJECTS_RESOURCE = "managed_objects";

    /**
     * Creates a new root Managed Object
     * @param managedObject managed object to be created
     * @return the created instance
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "create")
    public ManagedObjectDTO createRootManagedObject(final ManagedObjectDTO managedObject) {

        dps.setWriteAccess(true);
        final ManagedObject dpsManagedObject = dps.getLiveBucket().getMibRootBuilder()
                .name(managedObject.getName())
                .type(managedObject.getType())
                .namespace(managedObject.getNamespace())
                .addAttributes(getAttributesAsMap(managedObject.getAttributes()))
                .create();

        return convertDpsManagedObjectToDTO(dpsManagedObject);
    }

    /**
     * Creates a new Managed Object as having the MO with the given FDN as parent
     * @param parentFdn FDN of the parent MO
     * @param managedObject managed object to be created
     * @return the created instance
     * @throws ManagedObjectNotFoundException if the provided parentFdn does not match an existent Managed Object
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "create")
    public ManagedObjectDTO createChildManagedObject(final String parentFdn, final ManagedObjectDTO managedObject) {
        dps.setWriteAccess(true);
        final ManagedObject parent = getManagedObjectFromDPSByFDN(parentFdn);
        final ManagedObject dpsManagedObject = dps.getLiveBucket().getManagedObjectBuilder()
                .name(managedObject.getName())
                .type(managedObject.getType())
                .parent(parent)
                .addAttributes(getAttributesAsMap(managedObject.getAttributes()))
                .create();

        return convertDpsManagedObjectToDTO(dpsManagedObject);
    }

    /**
     * Deletes the Managed Object corresponding to the given FDN
     * @param fdn Managed object to be queried
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "delete")
    public void deleteManagedObject(final String fdn) {
        dps.setWriteAccess(true);

        final ManagedObject managedObject = getManagedObjectFromDPSByFDN(fdn);
        dps.getLiveBucket().deleteManagedObject(managedObject, DeleteOptions.defaultDelete());
    }

    /**
     * Updates the managed object attributes
     * @param managedObject managed object to be updated
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "update")
    public ManagedObjectDTO updateManagedObject(final ManagedObjectDTO managedObject) {
        dps.setWriteAccess(true);

        final ManagedObject moDPS = getManagedObjectFromDPSByFDN(managedObject.getFdn());
        moDPS.setAttributes(getAttributesAsMap(managedObject.getAttributes()));

        return convertDpsManagedObjectToDTO(moDPS);
    }

    /**
     * Retrieves the Managed Object with the given FDN
     * @param fdn the fdn to be queried
     * @return the object matching the fdn
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "read")
    public ManagedObjectDTO getManagedObject(final String fdn) {
        return convertDpsManagedObjectToDTO(getManagedObjectFromDPSByFDN(fdn));
    }

    /**
     * <p>Queries the database to retrieve the managed objects that match the namespace, type and attributes provided.</p>
     * @param namespace namespace to be queried
     * @param type type to be queried
     * @param limit if provided the number of results will be limited by the value provided here.
     * @param attributes the attributes names and values provided here will be used as simple equalTo restrictions
     * @return A collection of managed objects matching the search criteria
     */
    @Authorize(resource = MANAGED_OBJECTS_RESOURCE, action = "read")
    public Collection<ManagedObjectDTO> queryManagedObjects(final String namespace, final String type, final Integer limit, AttributeDTO...attributes) {

        final Query<TypeRestrictionBuilder> query = dps.getQueryBuilder().createTypeQuery(namespace, type);

        if (attributes != null && attributes.length > 0) {
            final List<Restriction> restrictions = new ArrayList<>();
            for (AttributeDTO attr : attributes) {
                restrictions.add(query.getRestrictionBuilder().equalTo(attr.getName(), attr.getValue()));
            }
            query.setRestriction(query.getRestrictionBuilder().allOf(restrictions.toArray(new Restriction[]{})));
        }

        final Iterator<PersistenceObject> poIterator = dps.getLiveBucket().getQueryExecutor().execute(query);
        final List<ManagedObjectDTO> result = new ArrayList<>();
        while (poIterator.hasNext()) {
            if (limit != null && result.size() == limit) {
                break;
            }
            final ManagedObject managedObject = (ManagedObject)poIterator.next();
            result.add(convertDpsManagedObjectToDTO(managedObject));
        }
        return result;
    }

    private ManagedObject getManagedObjectFromDPSByFDN(final String fdn) {
        final ManagedObject managedObject = dps.getLiveBucket().findMoByFdn(fdn);
        if (managedObject == null) {
            throw new ManagedObjectNotFoundException(String.format("No Managed Object was found with FDN: %s", fdn));
        }
        return managedObject;
    }

    private ManagedObjectDTO convertDpsManagedObjectToDTO (final ManagedObject managedObject) {
        final ManagedObjectDTO managedObjectDTO = new ManagedObjectDTO();
        managedObjectDTO.setId(Long.toString(managedObject.getPoId()));
        managedObjectDTO.setFdn(managedObject.getFdn());
        managedObjectDTO.setName(managedObject.getName());
        managedObjectDTO.setNamespace(managedObject.getNamespace());
        managedObjectDTO.setType(managedObject.getType());
        managedObjectDTO.setVersion(managedObject.getVersion());
        managedObjectDTO.setTimeCreated(managedObject.getCreatedTime().getTime());
        managedObjectDTO.setTimeLastUpdated(managedObject.getLastUpdatedTime().getTime());

        managedObjectDTO.setAttributes(managedObject.getAllAttributes().entrySet().stream()
                .map(entry -> new AttributeDTO(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString()))
                .collect(Collectors.toSet())
        );

        return managedObjectDTO;
    }

    private Map<String, Object> getAttributesAsMap(Set<AttributeDTO> attributes) {
        return attributes.stream()
                .collect(Collectors.toMap(AttributeDTO::getName, AttributeDTO::getValue));
    }

}
