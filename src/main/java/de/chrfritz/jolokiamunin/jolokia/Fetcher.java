package de.chrfritz.jolokiamunin.jolokia;

import java.net.URL;
import java.util.List;
import java.util.Map;

public interface Fetcher {

    void setUrl(URL url);

    Map<Request, Number> fetchValues(List<Request> requests) throws FetcherException;
}
