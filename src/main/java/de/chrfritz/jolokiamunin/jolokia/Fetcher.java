package de.chrfritz.jolokiamunin.jolokia;

import java.util.List;
import java.util.Map;

public interface Fetcher {

    Map<Request, Number> fetchValues(List<Request> requests) throws FetcherException;
}
