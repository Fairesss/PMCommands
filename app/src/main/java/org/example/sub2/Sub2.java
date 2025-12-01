package org.example.sub2;

import it.fair.McCommands.CommandCall;
import it.fair.McCommands.CommandClass;

@CommandClass
public class Sub2 {
    @CommandCall
    public int add2(int x) {
        return x + 2;
    }
}
