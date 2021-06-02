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
import com.ericsson.services.enm_sample_app.api.dto.ManagedObjectDTO
import com.ericsson.services.enm_sample_app.exceptions.ManagedObjectNotFoundException
import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException
import com.ericsson.services.enm_sample_app.rest.resource.ManagedObjectRestResource
import com.ericsson.services.enm_sample_app.tests.unit.BaseSpecification

import javax.inject.Inject

/**
 * Test specification for the DELETE flow of Managed Objects REST endpoint
 */
class DeleteManagedObjectSpec extends BaseSpecification {

    @ObjectUnderTest
    ManagedObjectRestResource dpsRest

    @Inject
    DataPersistenceService dps

    def "delete a managed object"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create a managed object"
            def root = dpsRest.createRootManagedObject(
                new ManagedObjectDTO(name: "root-mo", type: "MeContext",
                        namespace: "OSS_TOP", version: "1.0.0")).entity as ManagedObjectDTO

        and: "retrieves the managed object directly from DPS"
            def databaseEntry = dps.liveBucket.findMoByFdn(root.fdn)

        then: "the managed object should be found"
            databaseEntry != null

        when: "delete the managed object"
            def response = dpsRest.deleteManagedObject(root.fdn)

        then: "respose code should be 204"
            response.status == 204

        when: "retrieves the managed object directly from DPS after the deletion"
            databaseEntry = dps.liveBucket.findMoByFdn(root.fdn)

        then: "the managed object should be not be found"
            !databaseEntry
    }

    def "delete a managed object using an FDN that does not exist"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "try to delete an invalid FDN"
            dpsRest.deleteManagedObject("invalid-fdn")

        then: "an exception is expected"
            thrown(ManagedObjectNotFoundException)
    }

    def "delete a managed object without an authenticated user"() {

        when: "try to delete"
            dpsRest.deleteManagedObject("any-fdn")

        then: "an exception is expected"
            thrown(UnauthenticatedRequestException)
    }

}
