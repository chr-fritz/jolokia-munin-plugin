// ______________________________________________________________________________
//
//           Project: jolokia-munin-plugin
//            Module: jolokia-munin-plugin
//             Class: HelpController
//              File: HelpController.java
//        changed by: christian.fritz
//       change date: 31.03.14 12:46
// ______________________________________________________________________________
//
//         Copyright: (c) Christian Fritz, all rights reserved
// ______________________________________________________________________________

package de.chrfritz.jolokiamunin.controller;

import org.apache.commons.lang3.StringUtils;

/**
 * Build a short help message that contains the command name and a short description of all installed controllers.
 *
 * @author christian.fritz
 */
public class HelpController extends AbstractController {

    private static final int PREFIX_LENGHT = 15;

    @Override
    protected String handle() {

        StringBuilder builder = new StringBuilder();
        builder.append("Usage: jolokia [command]\n")
                .append("Available Commands:\n");
        for (Controller controller : getDispatcher().getControllers()) {

            String className = controller.getClass().getSimpleName();
            String command = className.substring(0, className.lastIndexOf("Controller")).toLowerCase();
            builder.append(StringUtils.leftPad(command, PREFIX_LENGHT))
                    .append(" : ")
                    .append(controller.getHelpMessage())
                    .append('\n');
        }
        builder.append("------------------------------------------------------------\n")
                .append(getDispatcher().handleRequest("version"))
                .append('\n');
        return builder.toString();
    }

    @Override
    public String getHelpMessage() {
        return "Print this help message";
    }
}
