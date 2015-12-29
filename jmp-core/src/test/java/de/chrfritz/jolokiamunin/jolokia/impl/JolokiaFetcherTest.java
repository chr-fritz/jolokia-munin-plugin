// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: JolokiaFetcherTest
//              File: JolokiaFetcherTest.java
//        changed by: christian
//       change date: 23.02.13 19:37
//       description: The jolokia fetcher test
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.api.fetcher.Fetcher;
import de.chrfritz.jolokiamunin.api.fetcher.Request;
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

/**
 * Test for the jolokia fetcher.
 */
@RunWith(MockitoJUnitRunner.class)
public class JolokiaFetcherTest {
    public static final int HTTP_STATUS_OK = 200;
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

        InputStream stream = getClass().getResourceAsStream(responseFile);

        HttpResponse response = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), expectedResponseStatus, ""));
        response.setStatusCode(expectedResponseStatus);
        response.setEntity(new InputStreamEntity(stream, Long.MAX_VALUE));

        when(client.execute((HttpUriRequest) any())).thenReturn(response);
    }

    @Test
    public void testFetchValuesFull() throws Exception {

        injectResponse(HTTP_STATUS_OK, "/de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesFull.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 139647584L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 279379968L);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesWithoutPath() throws Exception {
        injectResponse(HTTP_STATUS_OK, "/de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesWithoutPath.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 67108864L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 90741928L);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "HeapMemoryUsage"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesWithoutAttribute() throws Exception {
        injectResponse(HTTP_STATUS_OK, "/de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesWithoutAttirbute.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 67108864L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 114814760L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "max"), 587202560L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 279379968L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "init"), 270991360L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 139644112L);
        expected.put(new Request("java.lang:type=Memory", "ObjectPendingFinalizationCount"), 0L);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchValuesMixed() throws Exception {
        injectResponse(HTTP_STATUS_OK, "/de/chrfritz/jolokiamunin/jolokia/impl/fetchValuesMixed.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "max"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "committed"), 129761280L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "init"), 67108864L);
        expected.put(new Request("java.lang:type=Memory", "HeapMemoryUsage", "used"), 120071160L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"), 279379968L);
        expected.put(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"), 139712936L);

        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Memory", "HeapMemoryUsage"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "used"));
        requestList.add(new Request("java.lang:type=Memory", "NonHeapMemoryUsage", "committed"));
        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }

    @Test
    public void testFetchAttributeValue() throws Exception {
        injectResponse(HTTP_STATUS_OK, "/de/chrfritz/jolokiamunin/jolokia/impl/fetchAttributeValue.json");
        Map<Request, Number> expected = new HashMap<>();
        expected.put(new Request("java.lang:type=Threading", "ThreadCount"), 55L);
        List<Request> requestList = new ArrayList<>();
        requestList.add(new Request("java.lang:type=Threading", "ThreadCount"));

        Map<Request, Number> responses = fetcher.fetchValues(requestList);

        assertEquals(expected, responses);
    }
}
