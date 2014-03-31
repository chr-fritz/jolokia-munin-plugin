package de.chrfritz.jolokiamunin.controller;


import de.chrfritz.jolokiamunin.config.Configuration;
import de.chrfritz.jolokiamunin.munin.MuninProvider;

public interface Controller {

    String execute(String arguments);

    String getHelpMessage();

    MuninProvider getMuninProvider();

    void setMuninProvider(MuninProvider provider);

    Configuration getConfiguration();

    void setConfiguration(Configuration configuration);

    void setDispatcher(Dispatcher dispatcher);
}
