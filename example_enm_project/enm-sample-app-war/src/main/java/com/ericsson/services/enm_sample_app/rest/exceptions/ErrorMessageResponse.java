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
package com.ericsson.services.enm_sample_app.rest.exceptions;

import com.ericsson.services.enm_sample_app.exceptions.MyApplicationException;

/**
 * This class defines the payload for error responses
 */
public class ErrorMessageResponse {

    /**
     * The numeric error code for the exception
     */
    private Integer errorCode;

    /**
     * The message object
     */
    private Message message;

    public ErrorMessageResponse(final String title, final String message, final Integer errorCode) {
        this.errorCode = errorCode;
        this.message = new Message(title, message);
    }

    public ErrorMessageResponse(final MyApplicationException exception) {
        this.errorCode = exception.getErrorCode();
        this.message = new Message(exception.getTitle(), exception.getMessage());
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public Message getMessage() {
        return message;
    }

    /**
     * This object groups the message title and body
     */
    public class Message {

        /**
         * Message brief title
         */
        private String title;

        /**
         * Message body (detailed error message)
         */
        private String body;

        public Message(final String title, final String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

    }

}
