// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Request
//              File: Request.java
//        changed by: christian
//       change date: 23.02.13 19:37
//       description: Request DTO
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.jolokia;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A data transfer object for requesting and responding jmx and jolokia.
 */
public final class Request {

    private final String mbean;
    private final String attribute;
    private final String path;

    /**
     * Create a new request that only contains the mbean name.
     *
     * @param mbean The name of the mbean.
     */
    public Request(String mbean) {
        this.mbean = mbean;
        this.attribute = null;
        this.path = null;
    }

    /**
     * Creates a new request with the mbean name and attribute name.
     *
     * @param mbean     The name of the mbean.
     * @param attribute The attribute name
     */
    public Request(String mbean, String attribute) {
        this.attribute = attribute;
        this.mbean = mbean;
        this.path = null;
    }

    /**
     * Creats a new request with the full address (mbean, attribute and path).
     *
     * @param mbean     The name of the mbean.
     * @param attribute The attribute name
     * @param path      The specific path
     */
    public Request(String mbean, String attribute, String path) {
        this.mbean = mbean;
        this.attribute = attribute;
        this.path = path;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMbean() {
        return mbean;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.mbean)
                .append(this.attribute)
                .append(this.path)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Request other = (Request) obj;
        return new EqualsBuilder().append(this.mbean, other.mbean)
                .append(this.attribute, other.attribute)
                .append(this.path, other.path)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attribute", attribute)
                .append("mbean", mbean)
                .append("path", path)
                .toString();
    }
}
