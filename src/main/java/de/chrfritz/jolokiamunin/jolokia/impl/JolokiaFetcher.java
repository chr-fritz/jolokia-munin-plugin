// ______________________________________________________________________________
//
//           Project:
//              File: $Id$
//      last changed: $Rev$
// ______________________________________________________________________________
//
//        created by: ${USER}
//     creation date: ${DATE}
//        changed by: $Author$
//       change date: $LastChangedDate$
//       description:
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________
package de.chrfritz.jolokiamunin.jolokia.impl;

import com.google.common.base.Strings;
import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.FetcherException;
import de.chrfritz.jolokiamunin.jolokia.Request;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MalformedObjectNameException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JolokiaFetcher implements Fetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaFetcher.class);

    private J4pClient client;


    public JolokiaFetcher(URL url){
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

            responses.size();
        }
        catch (J4pException e) {
            LOGGER.error("", e);
            throw new FetcherException(e);
        }

        return null;
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
