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
package com.ericsson.services.enm_sample_app.tests.unit

import com.ericsson.cds.cdi.support.configuration.InjectionProperties
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.itpf.datalayer.dps.stub.RuntimeConfigurableDps

/**
 * Base test specification class. All spock tests should inherit this class
 */
class BaseSpecification extends CdiSpecification {

    RuntimeConfigurableDps runtimeDps

    def setup() {
        runtimeDps = cdiInjectorRule.getService(RuntimeConfigurableDps.class)
        runtimeDps.withTransactionBoundaries()
    }

    /**
     * Customize the injection provider
     * @param injectionProperties
     */
    @Override
    Object addAdditionalInjectionProperties(InjectionProperties injectionProperties) {

        // Specifies which packages contains the implementations for your interfaces
        injectionProperties.autoLocateFrom('com.ericsson.services.enm_sample_app')

    }

}
