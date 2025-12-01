package org.example.sub;


import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;


@CommandClass(subCommandClass = {"org.example.sub.Double"})
public class Sub {
    @CommandCall
    public void sub(){}
}
