// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: VersionController
//              File: VersionController.java
//        changed by: christian.fritz
//       change date: 31.03.14 13:31
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.singletonList;

/**
 * Show the version information of the installed jolokia munin plugin.
 *
 * @author christian.fritz
 */
public class VersionController extends AbstractController {

    @Override
    protected String handle() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.properties"));

        return "Jolokia-Munin Plugin by Christian Fritz 2013 - 2014 \n" +
                "Version: " +
                props.getProperty("jmp.version") +
                "\nBuild from git commit " +
                props.getProperty("jmp.buildNumber") +
                " on git branch " +
                props.getProperty("jmp.buildBranch") +
                "\nBuild date: " +
                props.getProperty("jmp.buildDate") +
                "\n";
    }

    /**
     * Get a list with all command names that the controller is responsible for.
     *
     * @return A list with all handled commands.
     */
    @Override
    public List<String> getHandledCommands() {
        return singletonList("version");
    }

    @Override
    public String getHelpMessage() {
        return "Get the version information";
    }
}
