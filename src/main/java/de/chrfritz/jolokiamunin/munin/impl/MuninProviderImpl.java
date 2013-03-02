// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: MuninProviderImpl
//              File: MuninProviderImpl.java
//        changed by: christian
//       change date: 24.02.13 18:28
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.munin.impl;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import de.chrfritz.jolokiamunin.config.Category;
import de.chrfritz.jolokiamunin.config.Field;
import de.chrfritz.jolokiamunin.config.Graph;
import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;
import de.chrfritz.jolokiamunin.jolokia.FetcherFactory;
import de.chrfritz.jolokiamunin.jolokia.Request;
import de.chrfritz.jolokiamunin.munin.MuninProvider;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuninProviderImpl implements MuninProvider {

    private FetcherFactory fetcherFactory;

    public MuninProviderImpl(FetcherFactory fetcherFactory) {

        this.fetcherFactory = fetcherFactory;
    }

    @Override
    public String getConfig(List<Category> categories) {

        StringBuffer buffer = new StringBuffer();
        for (Category category : categories) {
            buffer.append(getConfig(category));
        }
        return buffer.toString();
    }

    @Override
    public String getConfig(Category category) {

        StringBuffer buffer = new StringBuffer();
        for (Graph graph : category.getGraphs()) {

            buffer.append("multigraph ").append(category.getName()).append(graph.getName()).append("\n");
            addAttribute(buffer, "graph_title", graph.getTitle());
            addAttribute(buffer, "graph_args", graph.getArgs());
            addAttribute(buffer, "graph_category", category.getName());
            addAttribute(buffer, "graph_info", graph.getInfo());
            addAttribute(buffer, "graph_vlabel", graph.getVlabel());

            StringBuffer fields = new StringBuffer();
            StringBuffer fieldOrder = new StringBuffer();

            getFieldDefinitions(graph, fields, fieldOrder);

            addAttribute(buffer, "graph_order", fieldOrder.toString());
            buffer.append(fields);
        }

        return buffer.toString();
    }

    private void getFieldDefinitions(Graph graph, StringBuffer fields, StringBuffer fieldOrder) {
        for (Field field : graph.getFields()) {
            String fieldName = graph.getName() + "_" + field.getName();
            fieldOrder.append(fieldName).append(" ");
            addFieldAttribute(fields, fieldName, "label", field.getLabel());
            addFieldAttribute(fields, fieldName, "draw", field.getDraw());
            addFieldAttribute(fields, fieldName, "info", field.getInfo());
            //addFieldAttribute(fields, fieldName, "max", field.getMax());
            //addFieldAttribute(fields, fieldName, "min", field.getMin());
            addFieldAttribute(fields, fieldName, "type", field.getType());
            addFieldAttribute(fields, fieldName, "warning", field.getWarning());
            addFieldAttribute(fields, fieldName, "critical", field.getCritical());
            addFieldAttribute(fields, fieldName, "colour", field.getColor());
        }
    }

    @Override
    public String getValues(List<Category> categories) throws FetcherException {

        StringBuffer buffer = new StringBuffer();
        for (Category category : categories) {
            buffer.append(getValues(category));
        }
        return buffer.toString();
    }

    @Override
    public String getValues(Category category) throws FetcherException {

        try {
            StringBuffer buffer = new StringBuffer();
            Map<String, Request> requests = buildRequests(category);

            Fetcher fetcher = fetcherFactory.getInstance(category.getSourceUrl());

            Map<Request, Number> values = fetcher.fetchValues(new ArrayList<>(requests.values()));
            for (Graph graph : category.getGraphs()) {
                buffer.append("multigraph ").append(category.getName()).append(graph.getName()).append("\n");
                for (Field field : graph.getFields()) {
                    String fieldName = graph.getName() + "_" + field.getName();

                    addFieldAttribute(buffer, fieldName, "value", values.get(requests.get(fieldName)));
                }
            }

            return buffer.toString();
        }
        catch (MalformedURLException e) {
            throw new FetcherException("Malformed source url for category.", e);
        }
    }

    private Map<String, Request> buildRequests(Category category) {
        Map<String, Request> requests = new HashMap<>();

        for (Graph graph : category.getGraphs()) {
            String graphMbean = graph.getMbean();
            String graphAttribute = graph.getAttribute();
            String graphName = graph.getName();

            for (Field field : graph.getFields()) {
                String mbean = Objects.firstNonNull(field.getMbean(), graphMbean);
                String attribute = Objects.firstNonNull(field.getAttribute(), graphAttribute);
                requests.put(graphName + "_" + field.getName(), new Request(mbean, attribute, field.getPath()));
            }
        }
        return requests;
    }

    private static void addAttribute(StringBuffer buffer, String name, String value) {
        if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(value)) {
            buffer.append(name).append(" ").append(value.trim()).append("\n");
        }
    }

    private static void addFieldAttribute(StringBuffer buffer, String fieldName, String attribute, String value) {
        addAttribute(buffer, fieldName + "." + attribute, value);
    }

    private static void addFieldAttribute(StringBuffer buffer, String fieldName, String attribute, Number number) {
        buffer.append(fieldName).append(".").append(attribute).append(" ").append(number).append("\n");
    }
}
