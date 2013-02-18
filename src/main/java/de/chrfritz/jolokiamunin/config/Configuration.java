package de.chrfritz.jolokiamunin.config;

import java.util.List;

public interface Configuration {

    void load();

    List<Category> getConfiguration();
}
