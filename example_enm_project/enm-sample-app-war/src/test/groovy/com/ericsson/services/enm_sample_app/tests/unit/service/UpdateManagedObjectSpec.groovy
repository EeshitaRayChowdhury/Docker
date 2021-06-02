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
 * Test specification for the UPDATE flow of Managed Objects REST endpoint
 */
class UpdateManagedObjectSpec extends BaseSpecification {

    @ObjectUnderTest
    ManagedObjectRestResource dpsRest

    @Inject
    DataPersistenceService dps

    def "update a managed object"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        and: "a sample managed object"
            def managedObject = new ManagedObjectDTO(name: "root-mo", type: "MeContext",
                    namespace: "OSS_TOP", version: "1.0.0",
                    attributes: [new AttributeDTO(name: "my-attr", value: "123")] as Set)

        when: "create the managed object"
            def root = dpsRest.createRootManagedObject(managedObject)
                    .entity as ManagedObjectDTO

        and: "retrieves the managed object directly from DPS"
            def databaseEntry = dps.liveBucket.findMoByFdn(root.fdn)

        then: "the managed object should be found and have the attribute my-attr with value 123"
            databaseEntry.allAttributes["my-attr"] == "123"

        when: "update the managed object"
            managedObject.attributes[0].value = "321"
            def response = dpsRest.updateManagedObject("MeContext=root-mo", managedObject)

        then: "respose code should be 200"
            response.status == 200

        when: "retrieves the managed object directly from DPS after the update"
            databaseEntry = dps.liveBucket.findMoByFdn(root.fdn)

        then: "the managed object should be found and have the attribute my-attr with value 321"
            databaseEntry.allAttributes["my-attr"] == "321"
    }

    def "update a managed object using an FDN that does not exist"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "try to update an invalid FDN"
            dpsRest.updateManagedObject("invalid-fdn", new ManagedObjectDTO())

        then: "an exception is expected"
            thrown(ManagedObjectNotFoundException)
    }

    def "update a managed object without an authenticated user"() {

        when: "try to update"
            dpsRest.updateManagedObject("any-fdn", new ManagedObjectDTO())

        then: "an exception is expected"
            thrown(UnauthenticatedRequestException)
    }

}
