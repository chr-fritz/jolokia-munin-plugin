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
        Map<Request, Number> results = new HashMap<>();
        try {
            List<J4pReadResponse> responses = client.execute(readRequests);

            int i = 0;
            for (J4pReadResponse response : responses) {
                Object value = response.getValue();
                LOGGER.debug("Response value ({}): {}", value.getClass(), value);
                LOGGER.debug("Attributes {}", response.getAttributes());
                Request request = requests.get(i);

                boolean isValid = StringUtils.equals(request.getMbean(),
                                                     response.getRequest().getObjectName().toString()) &&
                        StringUtils.equals(request.getAttribute(), response.getRequest().getAttribute()) &&
                        StringUtils.equals(request.getPath(), response.getRequest().getPath());

                if (!isValid) {
                    continue;
                }

                if (!Strings.isNullOrEmpty(request.getMbean()) && !Strings.isNullOrEmpty(
                        request.getAttribute()) && !Strings.isNullOrEmpty(request.getPath())) {
                    results.put(request, (Number) value);
                } else if (!Strings.isNullOrEmpty(request.getMbean()) && !Strings.isNullOrEmpty(
                        request.getAttribute()) && Strings.isNullOrEmpty(request.getPath())) {
                    handleAttributeResponse(results, (JSONObject) value, request);
                } else if (!Strings.isNullOrEmpty(request.getMbean()) && Strings.isNullOrEmpty(
                        request.getAttribute())) {
                    for (Map.Entry entry : (Set<Map.Entry>) ((JSONObject) value).entrySet()) {
                        LOGGER.debug("{}", entry);
                        if (entry.getValue() instanceof JSONObject) {
                            handleAttributeResponse(results, (JSONObject) entry.getValue(),
                                                    new Request(request.getMbean(), (String) entry.getKey()));
                        } else if (entry.getValue() instanceof Number) {
                            results.put(new Request(request.getMbean(), (String) entry.getKey()),
                                        (Number) entry.getValue());
                        }
                    }
                }

                i++;
            }
        }
        catch (J4pException e) {
            LOGGER.error("", e);
            throw new FetcherException(e);
        }

        return results;
    }

    private void handleAttributeResponse(Map<Request, Number> results, JSONObject values, Request request) {

        for (Map.Entry entry : (Set<Map.Entry>) values.entrySet()) {

            if (!(entry.getValue() instanceof Number)) {
                continue;
            }
            LOGGER.debug("{}:{}", entry.getKey(), entry.getValue());
            results.put(new Request(request.getMbean(), request.getAttribute(), (String) entry.getKey()),
                        (Number) entry.getValue());
        }
    }

    private List<J4pReadRequest> createReadRequests(List<Request> requests) {
        List<J4pReadRequest> readRequests = new ArrayList<J4pReadRequest>();

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
