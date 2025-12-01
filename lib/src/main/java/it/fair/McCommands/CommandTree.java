package it.fair.McCommands;

import it.fair.McCommands.CommandTreeNode;

import java.lang.reflect.Method;

import java.util.List;
import java.util.ArrayList;


import java.lang.StringBuilder;


public class CommandTree {
    CommandTreeNode root;

    public CommandTree(String baseClass) {
        this.root = new CommandTreeNode(
                new Command(baseClass, null, null, null),
                new ArrayList<>());
    }

    public void addCommand(Command command) {
        this.addCommand(this.root, command);
    }

    public void addSubCommand(Command parent, Command command) {
        this.root.children().forEach(child -> {
            if (child.command().equals(parent)) {
                child.children().add(new CommandTreeNode(command, new ArrayList<>()));
            } else {
                addSubCommand(child, parent, command);
            }
        });
    }

    public void addSubCommand(CommandTreeNode root, Command parent, Command command) {
        root.children().forEach(child -> {
            if (child.equals(parent)) {
                child.children().add(new CommandTreeNode(command, new ArrayList<>()));
            } else {
                this.addSubCommand(child, parent, command);
            }
        });
    }

    private void addCommand(CommandTreeNode node, Command command) {
        if (node.command().pkg().equals(command.pkg())) {
            node.children().add(new CommandTreeNode(
                    command,
                    new ArrayList<>()));
        } else {
            node.children()
                    .forEach(child -> this.addCommand(child, command));
        }
    }


    private void printInfo(CommandTreeNode node, StringBuilder sb) {
        sb.append(node.toString()).append("\n").append("\t");
        node.children().forEach(c -> this.printInfo(c, sb));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        printInfo(this.root, sb);
        return sb.toString();
    }
}
