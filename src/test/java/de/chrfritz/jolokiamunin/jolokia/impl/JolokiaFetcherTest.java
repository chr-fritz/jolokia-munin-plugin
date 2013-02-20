package de.chrfritz.jolokiamunin.jolokia.impl;

import de.chrfritz.jolokiamunin.jolokia.Fetcher;
import de.chrfritz.jolokiamunin.jolokia.Request;
import org.jolokia.client.J4pClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class JolokiaFetcherTest {
    private Fetcher fetcher;
    @Mock
    private J4pClient client;

    @Before
    public void setUp() throws Exception {

        fetcher = new JolokiaFetcher(client);
    }

    @After
    public void tearDown() throws Exception {
        fetcher = null;
    }

    @Test
    public void testFetchValuesFull() throws Exception {

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
    public void testFetchValueWithoutAttribute() throws Exception {
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
}
