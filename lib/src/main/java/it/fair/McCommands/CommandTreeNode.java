package it.fair.McCommands;



import java.util.List;


public record CommandTreeNode(
			      Command command,
			      List<CommandTreeNode> children) {
    
}
