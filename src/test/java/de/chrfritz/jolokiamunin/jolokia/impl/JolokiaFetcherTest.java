package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JolokiaFetcherTest {
    private Fetcher fetcher;

    @Before
    public void setUp() throws Exception {
        fetcher = new JolokiaFetcher();
        fetcher.setUrl(new URL("http://localhost:8080/jolokia/"));
    }

    @After
    public void tearDown() throws Exception {
        fetcher = null;
    }

    @Test
    public void testFetchValues() throws Exception {

        List<Request> requestList = new ArrayList<Request>();
        requestList.add(new Request("java.lang:type=Memory", "HeapMemoryUsage"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(6, responses.size());
    }
}
