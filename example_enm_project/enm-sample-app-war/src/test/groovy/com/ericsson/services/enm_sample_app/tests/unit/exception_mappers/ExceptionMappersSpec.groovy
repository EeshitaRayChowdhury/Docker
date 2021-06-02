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
package com.ericsson.services.enm_sample_app.tests.unit.exception_mappers

import com.ericsson.oss.itpf.sdk.security.accesscontrol.SecurityViolationException
import com.ericsson.services.enm_sample_app.exceptions.ErrorCode
import com.ericsson.services.enm_sample_app.exceptions.ManagedObjectNotFoundException
import com.ericsson.services.enm_sample_app.exceptions.MyApplicationException
import com.ericsson.services.enm_sample_app.exceptions.UnauthenticatedRequestException
import com.ericsson.services.enm_sample_app.rest.exceptions.mappers.GenericApplicationExceptionMapper
import com.ericsson.services.enm_sample_app.rest.exceptions.mappers.GenericExceptionMapper
import com.ericsson.services.enm_sample_app.rest.exceptions.mappers.ManagedObjectNotFoundExceptionMapper
import com.ericsson.services.enm_sample_app.rest.exceptions.mappers.SecurityViolationExceptionMapper
import com.ericsson.services.enm_sample_app.rest.exceptions.mappers.UnauthenticatedRequestExceptionMapper
import com.ericsson.services.enm_sample_app.tests.unit.BaseSpecification

class ExceptionMappersSpec extends BaseSpecification {

    def "GenericApplicationExceptionMapper should manage application exceptions"() {

        given: "a sample exception"
            def myAppException = new MyApplicationException("my title", "my message", ErrorCode.UNEXPECTED_EXCEPTION)

        when: "the exception mapper is triggered"
            def response = new GenericApplicationExceptionMapper().toResponse(myAppException)

        then: "http status code should be 500"
            response.status == 500

        and: "title, message and error code should match the ones in the exception"
            response.entity.errorCode == -1
            response.entity.message.title == "my title"
            response.entity.message.body == "my message"
    }

    def "GenericExceptionMapper should manage any unhandled exception"() {

        given: "a sample exception"
            def myException = new IllegalArgumentException("my message")

        when: "the exception mapper is triggered"
            def response = new GenericExceptionMapper().toResponse(myException)

        then: "http status code should be 500"
            response.status == 500

        and: "title, message and error code should match the ones in the exception"
            response.entity.errorCode == -1
            response.entity.message.title == "Unknown Exception"
            response.entity.message.body == "my message"
    }

    def "ManagedObjectNotFoundExceptionMapper should manage the corresponding exception"() {

        given: "a sample exception"
            def exception = new ManagedObjectNotFoundException("my message")

        when: "the exception mapper is triggered"
            def response = new ManagedObjectNotFoundExceptionMapper().toResponse(exception)

        then: "http status code should be 404"
            response.status == 404

        and: "title, message and error code should match the ones in the exception"
            response.entity.errorCode == ErrorCode.MANAGED_OBJECT_NOT_FOUND.code
            response.entity.message.title == "Managed object not found"
            response.entity.message.body == "my message"
    }

    def "UnauthenticatedRequestExceptionMapper should manage the corresponding exception"() {

        given: "a sample exception"
            def exception = new UnauthenticatedRequestException()

        when: "the exception mapper is triggered"
            def response = new UnauthenticatedRequestExceptionMapper().toResponse(exception)

        then: "http status code should be 401"
            response.status == 401

        and: "title, message and error code should match the ones in the exception"
            response.entity.errorCode == ErrorCode.UNAUTHENTICATED_REQUEST.code
            response.entity.message.title == "Not authenticated"
            response.entity.message.body == "You need to be authenticated to use this service!"
    }

    def "SecurityViolationExceptionMapper should manage the corresponding exception"() {

        given: "a sample exception"
            def exception = new SecurityViolationException("my message")

        when: "the exception mapper is triggered"
            def response = new SecurityViolationExceptionMapper().toResponse(exception)

        then: "http status code should be 403"
            response.status == 403

        and: "title, message and error code should match the ones in the exception"
            response.entity.errorCode == ErrorCode.ACCESS_DENIED.code
            response.entity.message.title == "Access Denied"
            response.entity.message.body == "my message"
    }

}
