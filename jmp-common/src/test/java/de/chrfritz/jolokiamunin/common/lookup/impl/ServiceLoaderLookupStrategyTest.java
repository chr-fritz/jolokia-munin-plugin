package de.chrfritz.jolokiamunin.common.lookup.impl;

import de.chrfritz.jolokiamunin.common.lookup.TestService;
import de.chrfritz.jolokiamunin.common.lookup.impl.ServiceLoaderLookupStrategy.Producer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Unit test for the {@link ServiceLoaderLookupStrategy}.
 *
 * @author christian.fritz
 */
public class ServiceLoaderLookupStrategyTest {

    private ServiceLoaderLookupStrategy lookupStrategy;

    @Before
    public void setUp() throws Exception {
        lookupStrategy = new ServiceLoaderLookupStrategy();
    }

    @Test
    public void testLookup() throws Exception {
        TestService actual = lookupStrategy.lookup(TestService.class);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, instanceOf(TestServiceImpl.class));
    }

    @Test
    public void testLookupTwice() throws Exception {
        TestService actual1 = lookupStrategy.lookup(TestService.class);
        TestService actual2 = lookupStrategy.lookup(TestService.class);
        assertThat(actual1, is(notNullValue()));
        assertThat(actual2, is(notNullValue()));
        assertThat(actual1, instanceOf(TestServiceImpl.class));
        assertThat(actual2, instanceOf(TestServiceImpl.class));
        assertThat(actual1, is(actual2));
    }

    @Test
    public void testLookupInvalidService() throws Exception {
        ServiceLoaderLookupStrategyTest actual = lookupStrategy.lookup(ServiceLoaderLookupStrategyTest.class);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testLookupAll() throws Exception {
        List<TestService> actual = lookupStrategy.lookupAll(TestService.class);
        assertThat(actual, hasSize(2));
        assertThat(actual.get(0), instanceOf(TestServiceImpl.class));
        assertThat(actual.get(1), instanceOf(TestServiceImpl2.class));
    }

    @Test
    public void testLookupAllTwice() throws Exception {
        lookupStrategy.lookupAll(TestService.class);
        List<TestService> actual = lookupStrategy.lookupAll(TestService.class);
        assertThat(actual, hasSize(2));
        assertThat(actual.get(0), instanceOf(TestServiceImpl.class));
        assertThat(actual.get(1), instanceOf(TestServiceImpl2.class));
    }

    @Test
    public void testLookupProducer() throws Exception {
        lookupStrategy.init(TestService.class, (Producer<TestService>) TestServiceImpl::new);
        TestService actual = lookupStrategy.lookup(TestService.class);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, instanceOf(TestServiceImpl.class));
    }

    @Test
    public void testLookupProducerTwice() throws Exception {
        lookupStrategy.init(TestService.class, (Producer<TestService>) TestServiceImpl::new);

        TestService actual1 = lookupStrategy.lookup(TestService.class);
        TestService actual2 = lookupStrategy.lookup(TestService.class);
        assertThat(actual1, is(notNullValue()));
        assertThat(actual2, is(notNullValue()));
        assertThat(actual1, instanceOf(TestServiceImpl.class));
        assertThat(actual2, instanceOf(TestServiceImpl.class));
        assertThat(actual1, is(not(actual2)));
    }

    @Test
    public void testLookupAllProducer() throws Exception {
        lookupStrategy.lookup(TestService.class);
        lookupStrategy.init(TestService.class, (Producer<TestService>) TestServiceImpl::new);
        List<TestService> actual = lookupStrategy.lookupAll(TestService.class);
        assertThat(actual, hasSize(3));
        assertThat(actual.get(0), instanceOf(TestServiceImpl.class));
        assertThat(actual.get(1), instanceOf(TestServiceImpl2.class));
        assertThat(actual.get(2), instanceOf(TestServiceImpl.class));
    }

    @Test
    public void testLookupAllProducerTwice() throws Exception {
        lookupStrategy.lookup(TestService.class);
        lookupStrategy.init(TestService.class, (Producer<TestService>) TestServiceImpl::new);
        List<TestService> actual1 = lookupStrategy.lookupAll(TestService.class);
        List<TestService> actual2 = lookupStrategy.lookupAll(TestService.class);
        assertThat(actual1, hasSize(3));
        assertThat(actual2, hasSize(3));

        assertThat(actual1.get(0), instanceOf(TestServiceImpl.class));
        assertThat(actual1.get(1), instanceOf(TestServiceImpl2.class));
        assertThat(actual1.get(2), instanceOf(TestServiceImpl.class));

        assertThat(actual2.get(0), instanceOf(TestServiceImpl.class));
        assertThat(actual2.get(1), instanceOf(TestServiceImpl2.class));
        assertThat(actual2.get(2), instanceOf(TestServiceImpl.class));

        assertThat(actual1, hasItems(is(actual2.get(0)), is(not(actual2.get(2))), is(actual2.get(1))));
    }

    @Test
    public void testInit() throws Exception {
        lookupStrategy.init(TestService.class, new TestServiceImpl());
        lookupStrategy.init(TestService.class, new TestServiceImpl());
        assertThat(lookupStrategy.lookupAll(TestService.class), hasSize(2));
        lookupStrategy.init(TestService.class, new TestServiceImpl(), true);
        assertThat(lookupStrategy.lookupAll(TestService.class), hasSize(1));
    }

    @Test
    public void testInitProducer() throws Exception {
        lookupStrategy.init(TestService.class, new TestServiceImpl());
        lookupStrategy.init(TestService.class, new TestServiceImpl());
        assertThat(lookupStrategy.lookupAll(TestService.class), hasSize(2));
        lookupStrategy.init(TestService.class, (Producer<TestService>) TestServiceImpl::new, true);
        assertThat(lookupStrategy.lookupAll(TestService.class), hasSize(1));
    }

    @Test
    public void testRegisterBothInterfaceClass() throws Exception {
        TestService service1 = lookupStrategy.lookup(TestService.class);
        TestService service2 = lookupStrategy.lookup(TestServiceImpl.class);
        assertThat(service1, is(sameInstance(service2)));
    }

    @Test
    public void testInitArray() throws Exception {
        lookupStrategy.init(new Object[][]{
                {TestService.class, new TestServiceImpl()},
                {TestService.class, new TestServiceImpl2()},
        });
        assertThat(lookupStrategy.lookupAll(TestService.class), hasSize(2));
    }

    public static class TestServiceImpl implements TestService {
        @Override
        public String sayHello() {
            return "World";
        }
    }

    public static class TestServiceImpl2 implements TestService {
        @Override
        public String sayHello() {
            return "World2";
        }
    }
}
