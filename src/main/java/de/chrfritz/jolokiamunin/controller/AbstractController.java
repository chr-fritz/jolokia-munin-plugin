package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractController implements Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
    private List<String> arguments;
    private Configuration configuration;
    private MuninProvider provider;
    private Dispatcher dispatcher;

    @Override
    public String execute(String arguments) {
        try {
            this.arguments = Arrays.asList(arguments.split("\\s"));
            return handle();
        }
        catch (Exception e) {
            LOGGER.error("Can not handle request.", e);
            return "ERROR: " + e.getMessage();
        }
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public void setMuninProvider(MuninProvider provider) {
        this.provider = provider;
    }

    @Override
    public MuninProvider getMuninProvider() {
        return provider;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    protected final Dispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public final void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    protected abstract String handle() throws Exception;
}
