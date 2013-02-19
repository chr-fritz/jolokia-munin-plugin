package de.chrfritz.jolokiamunin.jolokia;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class Request {

    private final String mbean;
    private final String attribute;
    private final String path;

    public Request(String mbean, String attribute) {
        this.attribute = attribute;
        this.mbean = mbean;
        this.path = null;
    }

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
