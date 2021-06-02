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
package com.ericsson.services.enm_sample_app.api.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a managed object
 */
public class ManagedObjectDTO implements Serializable {

    /**
     * Unique id
     */
    private String id;

    /**
     * managed object name
     */
    private String name;

    /**
     * managed object type
     */
    private String type;

    /**
     * managed object namespace
     */
    private String namespace;

    /**
     * managed object version
     */
    private String version;

    /**
     * managed object FDN
     */
    private String fdn;

    /**
     * time in POSIX format when this managed object was created
     */
    private Long timeCreated;

    /**
     * time in POSIX format when this managed object was updated the last time
     */
    private Long timeLastUpdated;

    /**
     * the managed object attributes
     */
    private Set<AttributeDTO> attributes = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getFdn() {
        return fdn;
    }

    public void setFdn(final String fdn) {
        this.fdn = fdn;
    }

    public Set<AttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Set<AttributeDTO> attributes) {
        this.attributes = attributes;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(final Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Long getTimeLastUpdated() {
        return timeLastUpdated;
    }

    public void setTimeLastUpdated(final Long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ManagedObjectDTO that = (ManagedObjectDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ManagedObjectDTO{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", namespace='").append(namespace).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", fdn='").append(fdn).append('\'');
        sb.append(", timeCreated='").append(timeCreated).append('\'');
        sb.append(", timeLastUpdated='").append(timeLastUpdated).append('\'');
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}
