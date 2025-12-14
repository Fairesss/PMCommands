package org.example;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import it.fair.McCommands.Command;
import it.fair.McCommands.CommandParser;
import it.fair.McCommands.CommandRecorder;
import org.bukkit.plugin.java.JavaPlugin;

public class Example extends JavaPlugin {

    public static void main(String[] args) {
        Example example = new Example();
        CommandParser cr = new CommandParser("org.example");
        CommandRecorder  r = new CommandRecorder(example, cr.getCommandTree(), "fair");
    }
    @Override
    public void onEnable() {
        CommandParser cr = new CommandParser("org.example");
        CommandRecorder  r = new CommandRecorder(this, cr.getCommandTree(), "fair");
        Command.ArgumentTypeResolver.addCustomArgumentType(IntegerRangeProvider.class, ArgumentTypes.integerRange());
        r.register();
    }

}
