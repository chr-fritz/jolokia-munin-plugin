package de.chrfritz.jolokiamunin.jolokia.impl;

import com.google.common.base.Strings;
import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;
import de.chrfritz.jolokiamunin.jolokia.Request;
import org.apache.commons.lang3.StringUtils;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MalformedObjectNameException;
import java.net.URL;
import java.util.*;

public class JolokiaFetcher implements Fetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaFetcher.class);

    private J4pClient client;


    public JolokiaFetcher(URL url) {
        client = new J4pClient(url.toString());
    }

    public JolokiaFetcher(J4pClient client) {
        this.client = client;
    }

    @Override
    public Map<Request, Number> fetchValues(List<Request> requests) throws FetcherException {

        List<J4pReadRequest> readRequests = createReadRequests(requests);

        try {
            List<J4pReadResponse> responses = client.execute(readRequests);

            return handleResponses(requests, responses);
        }
        catch (J4pException e) {
            LOGGER.error("", e);
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
        for (J4pReadResponse response : responses) {
            Object value = response.getValue();
            Request request = requests.get(i);

            boolean isValid = StringUtils.equals(request.getMbean(),
                                                 response.getRequest().getObjectName().toString()) &&
                    StringUtils.equals(request.getAttribute(), response.getRequest().getAttribute()) &&
                    StringUtils.equals(request.getPath(), response.getRequest().getPath());

            if (!isValid) {
                continue;
            }

            handleResponse(results, value, request);

            i++;
        }
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
        if (!Strings.isNullOrEmpty(request.getMbean()) && !Strings.isNullOrEmpty(
                request.getAttribute()) && !Strings.isNullOrEmpty(
                request.getPath()) && value instanceof Number) {
            LOGGER.debug("Adding {}:{}", request, value);
            results.put(request, (Number) value);
        } else if (!Strings.isNullOrEmpty(request.getMbean()) && !Strings.isNullOrEmpty(
                request.getAttribute()) && Strings.isNullOrEmpty(request.getPath())) {
            handleAttributeResponse(results, (JSONObject) value, request);
        } else {
            handleMbeanResponse(results, (JSONObject) value, request);
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
        for (Map.Entry entry : (Set<Map.Entry>) value.entrySet()) {
            if (entry.getValue() instanceof JSONObject) {
                handleAttributeResponse(results, (JSONObject) entry.getValue(),
                                        new Request(request.getMbean(), (String) entry.getKey()));
            } else if (entry.getValue() instanceof Number) {
                Request key = new Request(request.getMbean(), (String) entry.getKey());
                LOGGER.debug("Adding {}:{}", key, entry.getValue());
                results.put(key, (Number) entry.getValue());
            }
        }
    }

    /**
     * Hanlde a response thats request contains a mbean and an attribute but no path.
     *
     * @param results The results map to add the values
     * @param values  The json response value
     * @param request The original request
     */
    private void handleAttributeResponse(Map<Request, Number> results, Map values, Request request) {

        for (Map.Entry entry : (Set<Map.Entry>) values.entrySet()) {

            if (!(entry.getValue() instanceof Number)) {
                continue;
            }
            Request key = new Request(request.getMbean(), request.getAttribute(), (String) entry.getKey());
            LOGGER.debug("Adding {}:{}", key, entry.getValue());
            results.put(key, (Number) entry.getValue());
        }
    }

    private List<J4pReadRequest> createReadRequests(List<Request> requests) {
        List<J4pReadRequest> readRequests = new ArrayList<>();

        for (Request request : requests) {

            try {
                J4pReadRequest readRequest = new J4pReadRequest(request.getMbean(), request.getAttribute());
                if (!Strings.isNullOrEmpty(request.getPath())) {
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
