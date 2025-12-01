package org.example;

import it.fair.McCommands.CommandRegister;

import org.example.sub.Sub;


public class Example {

    public static void main(String[] args) {
        CommandRegister cr = new CommandRegister("org.example");

    Sub a = new Sub();
	var clazzes = cr.getClazzes();
	var methods = cr.getMethods();
	clazzes.forEach((key, value) -> System.out.println(key + " = " +  value));
	methods.forEach((key, value) -> System.out.println(key + " = " +  value));

	System.out.println("-----------------------");
	var commandTree = cr.getCommandTree();

	System.out.println(commandTree);
    }
}
