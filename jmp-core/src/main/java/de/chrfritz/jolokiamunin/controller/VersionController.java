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
import java.util.Properties;

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

        StringBuilder buffer = new StringBuilder();
        buffer.append("Jolokia-Munin Plugin by Christian Fritz 2013 - 2014 \n")
                .append("Version: ")
                .append(props.getProperty("jmp.version"))
                .append("\nBuild from git commit ")
                .append(props.getProperty("jmp.buildNumber"))
                .append(" on git branch ")
                .append(props.getProperty("jmp.buildBranch"))
                .append("\nBuild date: ")
                .append(props.getProperty("jmp.buildDate"))
                .append("\n");

        return buffer.toString();
    }

    @Override
    public String getHelpMessage() {
        return "Get the version information";
    }
}
