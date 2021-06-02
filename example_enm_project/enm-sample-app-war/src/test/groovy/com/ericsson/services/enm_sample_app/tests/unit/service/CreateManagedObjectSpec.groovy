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
package com.ericsson.services.enm_sample_app.tests.unit.service

import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService
import com.ericsson.services.enm_sample_app.api.dto.AttributeDTO
import com.ericsson.services.enm_sample_app.api.dto.ManagedObjectDTO
import com.ericsson.services.enm_sample_app.exceptions.ManagedObjectNotFoundException
import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException
import com.ericsson.services.enm_sample_app.rest.resource.ManagedObjectRestResource
import com.ericsson.services.enm_sample_app.tests.unit.BaseSpecification

import javax.inject.Inject

/**
 * Test specification for the CREATE flow of Managed Objects REST endpoint
 */
class CreateManagedObjectSpec extends BaseSpecification {

    @ObjectUnderTest
    ManagedObjectRestResource dpsRest

    @Inject
    DataPersistenceService dps

    def "create a root managed object"() {

        given: "a sample managed object"
            def managedObject = new ManagedObjectDTO(name: "my-mo-name", type: "MeContext",
                    namespace: "OSS_TOP", version: "1.0.0", attributes:
                    [new AttributeDTO(name: "my-attribute", value: "my-attribute-value")] as Set)

        and: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create the managed object"
           def createdResponse = dpsRest.createRootManagedObject(managedObject)

        then: "status code should be 201 (Created)"
            createdResponse.status == 201

        when: "extract the payload entity"
            def created = createdResponse.entity as ManagedObjectDTO

        then: "created and last updated dates should not be null"
            created.timeCreated != null
            created.timeLastUpdated != null

        and: "FDN should match the type and name provided"
            created.fdn ==  "MeContext=my-mo-name"

        when: "retrieves the managed object directly from DPS"
            def databaseEntry = dps.liveBucket.findMoByFdn(created.fdn)

        then: "the managed object found in the database and the managed object returned should have the same fields"
            databaseEntry.fdn == created.fdn
            databaseEntry.poId == created.id as Long
            databaseEntry.name == created.name
            databaseEntry.version == created.version
            databaseEntry.type == created.type

        and: "the managed object found in the database and the managed object returned should have the same attributes"
            databaseEntry.allAttributes["my-attribute"] == created.attributes[0].value
    }

    def "create a child managed object"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create the root managed object"
            def rootResponse = dpsRest.createRootManagedObject(
                    new ManagedObjectDTO(name: "root-mo", type: "MeContext",
                    namespace: "OSS_TOP", version: "1.0.0"))

        then: "http status code should be 201"
            rootResponse.status == 201

        when: "extract the root response entity form the payload"
            def root = rootResponse.entity as ManagedObjectDTO

        and: "create a child managed object"
            def childResponse = dpsRest.createChildManagedObject(root.fdn,
                    new ManagedObjectDTO(name: "child-mo", type: "ManagedElement",
                    namespace: "OSS_TOP", version: "1.0.0"))

        then: "http status code should be 201"
            childResponse.status == 201

        when: "extract the child response entity form the payload"
            def child = childResponse.entity as ManagedObjectDTO

        then: "FDN should match the parent FDN, type and name provided"
            child.fdn == "MeContext=root-mo,ManagedElement=child-mo"

        when: "retrieves the managed object directly from DPS"
            def databaseEntry = dps.liveBucket.findMoByFdn(child.fdn)

        then: "the managed object found in the database and the managed object returned should have the same fields"
            databaseEntry.fdn == child.fdn
            databaseEntry.poId == child.id as Long
            databaseEntry.name == child.name
            databaseEntry.version == child.version
            databaseEntry.type == child.type

    }

    def "create a child managed object with a parent FDN that does not exist"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create a child managed object"
            dpsRest.createChildManagedObject("invalid-fdn",
                new ManagedObjectDTO(name: "child-mo", type: "ManagedElement",
                        namespace: "OSS_TOP", version: "1.0.0"))

        then: "an exception is expected"
            thrown(ManagedObjectNotFoundException)
    }

    def "create a child managed object without an authenticated user"() {

        when: "create a child managed object"
            dpsRest.createChildManagedObject("any-fdn",
                    new ManagedObjectDTO(name: "child-mo", type: "ManagedElement",
                            namespace: "OSS_TOP", version: "1.0.0"))

        then: "an exception is expected"
            thrown(UnauthenticatedRequestException)
    }

    def "create a root managed object without an authenticated user"() {

        when: "create a child managed object"
            dpsRest.createRootManagedObject(
                    new ManagedObjectDTO(name: "child-mo", type: "ManagedElement",
                            namespace: "OSS_TOP", version: "1.0.0"))

        then: "an exception is expected"
            thrown(UnauthenticatedRequestException)
    }

}
