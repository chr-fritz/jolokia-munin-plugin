// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: Field
//              File: Field.java
//        changed by: christian
//       change date: 18.02.13 19:26
//       description: Field DTO
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * A data transfer object for configuration fields.
 */
public final class Field {

    private String info;
    private String mbean;
    private String attribute;
    private String path;
    private String name;
    private String type;
    private String label;
    private BigDecimal max;
    private BigDecimal min;
    private String critical;
    private String warning;
    private String draw;
    private String color;

    public String getAttribute() {
        return attribute;
    }

    public String getColor() {
        return color;
    }

    public String getCritical() {
        return critical;
    }

    public String getDraw() {
        return draw;
    }

    public String getInfo() {
        return info;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getMax() {
        return max;
    }

    public String getMbean() {
        return mbean;
    }

    public BigDecimal getMin() {
        return min;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getWarning() {
        return warning;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public void setMbean(String mbean) {
        this.mbean = mbean;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.info)
                .append(this.mbean)
                .append(this.attribute)
                .append(this.path)
                .append(this.name)
                .append(this.type)
                .append(this.label)
                .append(this.max)
                .append(this.min)
                .append(this.critical)
                .append(this.warning)
                .append(this.draw)
                .append(this.color)
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
        final Field other = (Field) obj;
        return new EqualsBuilder().append(this.info, other.info)
                .append(this.mbean, other.mbean)
                .append(this.attribute, other.attribute)
                .append(this.path, other.path)
                .append(this.name, other.name)
                .append(this.type, other.type)
                .append(this.label, other.label)
                .append(this.max, other.max)
                .append(this.min, other.min)
                .append(this.critical, other.critical)
                .append(this.warning, other.warning)
                .append(this.draw, other.draw)
                .append(this.color, other.color)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("attribute", attribute).
                append("info", info).
                append("mbean", mbean).
                append("path", path).
                append("name", name).
                append("type", type).
                append("label", label).
                append("max", max).
                append("min", min).
                append("critical", critical).
                append("warning", warning).
                append("draw", draw).
                append("color", color).
                toString();
    }
}
