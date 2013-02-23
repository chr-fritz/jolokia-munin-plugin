package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.Request;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.jolokia.client.J4pClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JolokiaFetcherTest {
    private Fetcher fetcher;
    @Mock
    private HttpClient client;

    @Before
    public void setUp() throws Exception {

        fetcher = new JolokiaFetcher(new J4pClient("", client));
    }

    @After
    public void tearDown() throws Exception {
        fetcher = null;
    }

    private void injectResponse(int expectedResponseStatus, String responseFile) throws
            IOException {

        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(responseFile);

        HttpResponse response = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), expectedResponseStatus, ""));
        response.setStatusCode(expectedResponseStatus);
        response.setEntity(new InputStreamEntity(stream, Long.MAX_VALUE));

        when(client.execute((HttpUriRequest) any())).thenReturn(response);
    }

    @Test
    public void testFetchValuesFull() throws Exception {

        injectResponse(200, "de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesFull.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 1000);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesWithoutPath() throws Exception {
        injectResponse(200, "de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesWithoutPath.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 1000);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "HeapMemoryUsage"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesWithoutAttribute() throws Exception {
        injectResponse(200, "de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesWithoutAttirbute.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "max"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "init"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 1000);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesMixed() throws Exception {
        injectResponse(200, "de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesMixed.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 1000);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 1000);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 1000);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "HeapMemoryUsage"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }
}
