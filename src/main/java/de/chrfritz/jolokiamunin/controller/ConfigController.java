package de.chrfritz.jolokiamunin.controller;

import de.chrfritz.jolokiamunin.config.Configuration;

public class ConfigController extends AbstractController {
    @Override
    protected String handle() {
        Configuration config = getConfiguration();
        return getMuninProvider().getConfig(config.getConfiguration());
    }

    @Override
    public String getHelpMessage() {
        return "Get the configuration for munin";
    }
}
