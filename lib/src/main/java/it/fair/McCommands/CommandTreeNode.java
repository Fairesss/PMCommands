package it.fair.McCommands;

import java.lang.reflect.Method;

import java.util.List;
import java.util.ArrayList;

import java.lang.Package;


public record CommandTreeNode(
			      Command command,
			      List<CommandTreeNode> children) {
    
}
