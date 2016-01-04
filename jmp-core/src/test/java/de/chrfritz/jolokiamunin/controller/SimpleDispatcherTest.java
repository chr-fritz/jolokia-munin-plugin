package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.api.Controller;
import de.chrfritz.jolokiamunin.common.lookup.Lookup;
import de.chrfritz.jolokiamunin.common.lookup.LookupStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

/**
 * Dispatcher test.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleDispatcherTest {

    @Mock
    private LookupStrategy strategy;

    private de.chrfritz.jolokiamunin.api.Dispatcher dispatcher;

    @Before
    public void setUp() throws Exception {
        Lookup.init(strategy);

        dispatcher = new SimpleDispatcher();
        SimpleDispatcher.HelpController helpController = new SimpleDispatcher.HelpController();
        helpController.setDispatcher(dispatcher);
        VersionController versionController = new VersionController();
        when(strategy.lookupAll(Controller.class)).thenReturn(Arrays.asList(helpController, versionController));
        when(strategy.lookup(SimpleDispatcher.HelpController.class)).thenReturn(helpController);
        when(strategy.lookup(VersionController.class)).thenReturn(versionController);
        dispatcher.init(Collections.singletonList(Controller.class));
    }

    @Test
    public void testHandleRequest() throws Exception {
        String actual = dispatcher.handleRequest("version");
        assertThat(actual, containsString("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    public void testHandleRequestHelp() throws Exception {
        String actual = dispatcher.handleRequest("help");
        assertThat(actual, containsString("Usage: jolokia [command]\n" +
                "Available Commands:\n" +
                "help, h : Print this help message\n" +
                "version : Get the version information\n" +
                "------------------------------------------------------------\n" +
                "Jolokia-Munin Plugin by Christian Fritz"));
    }
}
