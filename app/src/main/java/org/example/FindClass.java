package org.example;

import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;



@CommandClass(subCommandClass = {"org.example.sub", "org.example.sub2"})
public class FindClass {

    @CommandCall
    public int addOne(int x) {
	return x + 1;
    }

    @CommandCall
    public void nothing() {}
}
