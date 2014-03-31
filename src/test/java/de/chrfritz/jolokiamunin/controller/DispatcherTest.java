package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Dispatcher test.
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherTest {

    @Mock
    private MuninProvider provider;

    @Mock
    private Configuration configuration;

    private Dispatcher dispatcher;

    @Before
    public void setUp() throws Exception {
        dispatcher = new Dispatcher(configuration, provider);
        dispatcher.resolveControllers();
    }

    @Test
    public void testHandleRequest() throws Exception {
        String actual = dispatcher.handleRequest("version");
        assertTrue(actual.contains("Jolokia-Munin Plugin by Christian Fritz"));
    }

    @Test
    public void testGetControllerNotExists() throws Exception {
        Controller controller = dispatcher.getControllerForCommand("ControllerNotExists");
        assertNull(controller);
    }

    @Test
    public void testGetController() throws Exception {
        Controller controller = dispatcher.getControllerForCommand("version");
        assertTrue(controller instanceof VersionController);
    }
}
