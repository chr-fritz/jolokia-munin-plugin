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
    private String name;
    private String title;
    private String vlabel;
    private String args;
    private String info;
    private boolean scale;
    private String attribute;
    private String mbean;
    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMbean() {
        return mbean;
    }

    public void setMbean(String mbean) {
        this.mbean = mbean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isScale() {
        return scale;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVlabel() {
        return vlabel;
    }

    public void setVlabel(String vlabel) {
        this.vlabel = vlabel;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.fields)
                .append(this.name)
                .append(this.title)
                .append(this.vlabel)
                .append(this.args)
                .append(this.info)
                .append(this.scale)
                .append(this.attribute)
                .append(this.mbean)
                .append(this.hostname)
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
                .append(this.name, other.name)
                .append(this.title, other.title)
                .append(this.vlabel, other.vlabel)
                .append(this.args, other.args)
                .append(this.info, other.info)
                .append(this.scale, other.scale)
                .append(this.attribute, other.attribute)
                .append(this.mbean, other.mbean)
                .append(this.hostname, other.hostname)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("args", args)
                .append("attribute", attribute)
                .append("fields", fields)
                .append("hostname", hostname)
                .append("info", info)
                .append("mbean", mbean)
                .append("name", name)
                .append("scale", scale)
                .append("title", title)
                .append("vlabel", vlabel)
                .toString();
    }
}
