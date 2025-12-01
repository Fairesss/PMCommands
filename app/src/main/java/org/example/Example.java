package org.example;

import it.fair.McCommands.CommandRegister;



public class Example {

    public static void main(String[] args) {
        CommandRegister cr = new CommandRegister("org.example");

        var commandTree = cr.getCommandTree();

        System.out.println(commandTree);
    }
}
