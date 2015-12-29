package de.chrfritz.jolokiamunin.common.lookup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link Lookup} class.
 *
 * @author christian.fritz
 */
@RunWith(MockitoJUnitRunner.class)
public class LookupTest {
    @Mock
    private LookupStrategy strategy;
    @Mock
    private TestService service;

    @Before
    public void setUp() throws Exception {
        Lookup.init(strategy);
        when(strategy.lookup(TestService.class)).thenReturn(service);
        when(strategy.lookupAll(TestService.class)).thenReturn(Arrays.asList(service));
    }

    @Test
    public void testLookup() throws Exception {
        TestService actual = Lookup.lookup(TestService.class);
        assertThat(actual, is(service));
        verify(strategy).lookup(TestService.class);
    }

    @Test
    public void testLookupAll() throws Exception {
        List<TestService> actual = Lookup.lookupAll(TestService.class);
        assertThat(actual, hasItem(service));
        verify(strategy).lookupAll(TestService.class);
    }

    @Test
    public void testGetLookupStrategy() throws Exception {
        Lookup.init(null);
        LookupStrategy actual = Lookup.getLookupStrategy();
        assertThat(actual, is(nullValue()));
    }
}
