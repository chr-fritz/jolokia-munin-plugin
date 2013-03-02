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
            buffer.append(getConfig(category)).append("\n");
        }
        return buffer.toString();
    }

    @Override
    public String getConfig(Category category) {
        return null;
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
                    buffer.append(fieldName).append(".value ").append(values.get(requests.get(fieldName))).append("\n");
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
}
