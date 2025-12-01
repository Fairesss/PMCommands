package org.example.sub.Double;

import it.fair.McCommands.CommandCall;
import it.fair.McCommands.CommandClass;

@CommandClass
public class Double {
    @CommandCall
    public int crazy(int a) {
        return a * 2;
    }
}
