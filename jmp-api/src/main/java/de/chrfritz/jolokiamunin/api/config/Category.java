// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jmp-api
//             Class: Category
//              File: Category.java
//        changed by: christian.fritz
//       change date: 29.12.15 14:43
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.api.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A data transfer object for configuration categories.
 */
public final class Category {

    private String name;

    private String sourceUrl;

    private List<Graph> graphs = new ArrayList<>();

    public List<Graph> getGraphs() {
        return graphs;
    }

    public String getName() {
        return name;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.sourceUrl)
                .append(this.graphs)
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
        final Category other = (Category) obj;
        return new EqualsBuilder()
                .append(this.name, other.name)
                .append(this.sourceUrl, other.sourceUrl)
                .append(this.graphs, other.graphs)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("graphs", graphs)
                .append("name", name)
                .append("sourceUrl", sourceUrl)
                .toString();
    }
}
