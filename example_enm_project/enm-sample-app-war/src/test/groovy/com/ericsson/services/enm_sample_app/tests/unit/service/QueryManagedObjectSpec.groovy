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
import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException
import com.ericsson.services.enm_sample_app.rest.resource.ManagedObjectRestResource
import com.ericsson.services.enm_sample_app.tests.unit.BaseSpecification

import javax.inject.Inject

/**
 * Test specification for the Managed Objects queries REST endpoint
 */
class QueryManagedObjectSpec extends BaseSpecification {

    @ObjectUnderTest
    ManagedObjectRestResource dpsRest

    @Inject
    DataPersistenceService dps

    def "query a managed object by namespace and type"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create a managed object as MeContext in namespace OSS_TOP"
            dpsRest.createRootManagedObject(
                    new ManagedObjectDTO(name: "mecontext-01", type: "MeContext",
                            namespace: "OSS_TOP", version: "1.0.0"))

        and: "create a managed object as MeContext in namespace OTHER"
            dpsRest.createRootManagedObject(
                    new ManagedObjectDTO(name: "mecontext-02", type: "MeContext",
                            namespace: "OTHER", version: "1.0.0"))

        and: "create a managed object as NetworkElement in namespace OSS_TOP"
            dpsRest.createRootManagedObject(
                    new ManagedObjectDTO(name: "network-element-01", type: "NetworkElement",
                            namespace: "OSS_TOP", version: "1.0.0"))

        and: "use the query endpoint passing namespace OSS_TOP and type MeContext"
            def response = dpsRest.queryManagedObjects("OSS_TOP", "MeContext", null)

        then: "the status code should be 200"
            response.status == 200

        when: "extract the results from the payload"
            def objects = response.entity as List<ManagedObjectDTO>

        then: "only the managed object matching the namespace and type provided should be found"
            objects.size() == 1
            objects[0].name == "mecontext-01"

    }

    def "query a managed object by namespace and type limiting the response size"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create 5 managed objects as MeContext in namespace OSS_TOP"
            (1..5).each {
                dpsRest.createRootManagedObject(
                        new ManagedObjectDTO(name: "mecontext-0${it}", type: "MeContext",
                                namespace: "OSS_TOP", version: "1.0.0"))
            }

        and: "use the query endpoint without a limit"
            def response = dpsRest.queryManagedObjects("OSS_TOP", "MeContext", null)

        then: "the status code should be 200"
            response.status == 200

        when: "extract the results from the payload"
            def objects = response.entity as List<ManagedObjectDTO>

        then: "5 managed objects should be returned"
            objects.size() == 5

        when: "use the query endpoint with a limit of 3 objects"
            response = dpsRest.queryManagedObjects("OSS_TOP", "MeContext", 3)

        then: "the status code should be 200"
            response.status == 200

        when: "extract the results from the payload"
            objects = response.entity as List<ManagedObjectDTO>

        then: "only 3 managed objects should be returned"
            objects.size() == 3

    }

    def "query a managed object by namespace, type and attribute"() {

        given: "an authenticated user"
            dpsRest.userId = "any-user"

        when: "create 5 managed objects as MeContext in namespace OSS_TOP"
            (1..5).each {
                dpsRest.createRootManagedObject(
                        new ManagedObjectDTO(name: "mecontext-0${it}", type: "MeContext",
                                namespace: "OSS_TOP", version: "1.0.0",
                                attributes: [new AttributeDTO(name: "my-attr", value: "$it")]))
            }

        and: "use the query endpoint without a limit"
            def response = dpsRest.queryManagedObjects("OSS_TOP", "MeContext", null,
                    new AttributeDTO(name: "my-attr", value: "1"))

        then: "the status code should be 200"
            response.status == 200

        when: "extract the results from the payload"
            def objects = response.entity as List<ManagedObjectDTO>

        then: "1 managed object should be returned"
            objects.size() == 1
            objects[0].name == "mecontext-01"

    }

    def "query a managed object without an authenticated user"() {

        when: "use the query endpoint"
            def response = dpsRest.queryManagedObjects("OSS_TOP", "MeContext", null)

        then: "an exception is expected"
            thrown(UnauthenticatedRequestException)

    }
}
