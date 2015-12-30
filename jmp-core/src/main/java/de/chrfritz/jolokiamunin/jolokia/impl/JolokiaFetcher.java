// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: JolokiaFetcher
//              File: JolokiaFetcher.java
//        changed by: christian
//       change date: 23.02.13 20:11
//       description: The jolokia fetcher implementation
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.api.fetcher.Fetcher;
import de.chrfritz.jolokiamunin.api.fetcher.FetcherException;
import de.chrfritz.jolokiamunin.api.fetcher.Request;
import org.apache.commons.lang3.StringUtils;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MalformedObjectNameException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Implementation of the fechter interface to fetch values from a jolokia agent.
 */
public class JolokiaFetcher implements Fetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaFetcher.class);

    private J4pClient client;

    /**
     * Creates a new fetcher and connect to the given url.
     *
     * @param url The connection url
     */
    public JolokiaFetcher(URL url) {
        client = new J4pClient(url.toString());
    }

    /**
     * Create a new fetcher and use the given joloka client
     *
     * @param client The jolokia client to use.
     */
    public JolokiaFetcher(J4pClient client) {
        this.client = client;
    }

    /**
     * Fetch the values for the given requests. If a request is not specific all sub
     * attributes are be fetched and returned.
     * e.g. a request with only set mbean=java.lang:type=Memory will return 9 values
     *
     * @param requests The requested values.
     * @return A map that maps the specific request to a value.
     * @throws FetcherException In case of no values can be fetched
     */
    @Override
    public Map<Request, Number> fetchValues(List<Request> requests) throws FetcherException {

        List<J4pReadRequest> readRequests = createReadRequests(requests);

        try {
            List<J4pReadResponse> responses = client.execute(readRequests);

            return handleResponses(requests, responses);
        }
        catch (J4pException e) {
            LOGGER.error("Can not fetch values", e);
            throw new FetcherException(e);
        }
    }

    /**
     * Handle all responses.
     *
     * @param requests  The original requests to fetch
     * @param responses The returned responses
     * @return A map with the extracted values and corresponding requests as keys
     */
    private Map<Request, Number> handleResponses(List<Request> requests, List<J4pReadResponse> responses) {
        Map<Request, Number> results = new HashMap<>();
        int i = 0;
        LOGGER.info("Handle {} responses", responses.size());
        for (J4pReadResponse response : responses) {
            Object value = response.getValue();
            Request request = requests.get(i);

            boolean isValid = StringUtils.equals(request.getMbean(), response.getRequest().getObjectName().toString()) &&
                    StringUtils.equals(request.getAttribute(), response.getRequest().getAttribute()) &&
                    StringUtils.equals(request.getPath(), response.getRequest().getPath());

            // process only if request is equals to response
            if (!isValid) {
                continue;
            }
            handleResponse(results, value, request);
            i++;
        }
        LOGGER.info("Added {} values", responses.size());
        return results;
    }

    /**
     * Handle a response.
     *
     * @param results The results map to add the values
     * @param value   The json response value
     * @param request The original request
     */
    private void handleResponse(Map<Request, Number> results, Object value, Request request) {
        if (!isBlank(request.getAttribute()) && value instanceof Number) {
            LOGGER.debug("Adding {}:{}", request, value);
            results.put(request, (Number) value);
        }
        else if (!isBlank(request.getMbean()) && !isBlank(request.getAttribute())
                && isBlank(request.getPath())) {
            handleAttributeResponse(results, (Map) value, request);
        }
        else {
            handleMbeanResponse(results, (Map) value, request);
        }
    }

    /**
     * Handle a response thats request has only contained a mbean and no attribute and path.
     *
     * @param results The results map to add the values
     * @param value   The json response value
     * @param request The original request
     */
    private void handleMbeanResponse(Map<Request, Number> results, Map value, Request request) {
        for (Object entryObject : value.entrySet()) {
            if (!(entryObject instanceof Map.Entry)) {
                continue;
            }
            Map.Entry entry = (Map.Entry) entryObject;
            if (entry.getValue() instanceof Map) {
                handleAttributeResponse(results, (Map) entry.getValue(), new Request(request.getMbean(), (String) entry.getKey()));
            }
            else if (entry.getValue() instanceof Number) {
                Request key = new Request(request.getMbean(), (String) entry.getKey());
                LOGGER.debug("Adding {}:{}", key, entry.getValue());
                results.put(key, (Number) entry.getValue());
            }
        }
    }

    /**
     * Handle a response thats request contains a mbean and an attribute but no path.
     *
     * @param results The results map to add the values
     * @param values  The json response value
     * @param request The original request
     */
    private void handleAttributeResponse(Map<Request, Number> results, Map values, Request request) {
        for (Object entryObject : values.entrySet()) {
            if (!(entryObject instanceof Map.Entry)) {
                continue;
            }
            Map.Entry entry = (Map.Entry) entryObject;

            if (!(entry.getValue() instanceof Number)) {
                continue;
            }
            Request key = new Request(request.getMbean(), request.getAttribute(), (String) entry.getKey());
            LOGGER.debug("Adding {}:{}", key, entry.getValue());
            results.put(key, (Number) entry.getValue());
        }
    }

    /**
     * Create the jolokia read requests for given requests.
     *
     * @param requests The requests
     * @return A list with jolokia requests of the given requests.
     */
    private List<J4pReadRequest> createReadRequests(List<Request> requests) {
        List<J4pReadRequest> readRequests = new ArrayList<>();

        for (Request request : requests) {

            try {
                J4pReadRequest readRequest = new J4pReadRequest(request.getMbean(), request.getAttribute());
                if (!isBlank(request.getPath())) {
                    readRequest.setPath(request.getPath());
                }
                readRequests.add(readRequest);
            }
            catch (MalformedObjectNameException e) {
                LOGGER.error("Can not create read request due to malformed object name. Skipping", e);
            }
        }
        return readRequests;
    }
}
