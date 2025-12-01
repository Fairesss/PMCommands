package org.example.sub.Double;

import it.fair.McCommands.CommandCall;
import it.fair.McCommands.CommandClass;

@CommandClass(subCommandClass = {"org.example.sub.Double.triple"})
public class Double {
    @CommandCall
    public int crazy(int a) {
        return a * 2;
    }
}
