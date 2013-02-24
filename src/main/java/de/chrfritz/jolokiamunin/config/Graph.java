// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Graph
//              File: Graph.java
//        changed by: christian
//       change date: 18.02.13 19:26
//       description: Graph DTO
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * A data transfer object for configuration graphs.
 */
public final class Graph {

    private List<Field> fields;

    private String title;
    private String vlabel;
    private String args;
    private String info;
    private boolean scale;

    private String attribute;
    private String mbean;

    public String getArgs() {
        return args;
    }

    public String getAttribute() {
        return attribute;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getInfo() {
        return info;
    }

    public String getMbean() {
        return mbean;
    }

    public boolean isScale() {
        return scale;
    }

    public String getTitle() {
        return title;
    }

    public String getVlabel() {
        return vlabel;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMbean(String mbean) {
        this.mbean = mbean;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVlabel(String vlabel) {
        this.vlabel = vlabel;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.fields)
                .append(this.title)
                .append(this.vlabel)
                .append(this.args)
                .append(this.info)
                .append(this.scale)
                .append(this.attribute)
                .append(this.mbean)
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
        final Graph other = (Graph) obj;
        return new EqualsBuilder().append(this.fields, other.fields)
                .append(this.title, other.title)
                .append(this.vlabel, other.vlabel)
                .append(this.args, other.args)
                .append(this.info, other.info)
                .append(this.scale, other.scale)
                .append(this.attribute, other.attribute)
                .append(this.mbean, other.mbean)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("args", args)
                .append("fields", fields)
                .append("title", title)
                .append("vlabel", vlabel)
                .append("info", info)
                .append("scale", scale)
                .append("attribute", attribute)
                .append("mbean", mbean)
                .toString();
    }
}
