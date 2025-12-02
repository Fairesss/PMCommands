package org.example;

import it.fair.McCommands.CommandParser;
import it.fair.McCommands.CommandRecorder;
import org.bukkit.plugin.java.JavaPlugin;


public class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandParser cr = new CommandParser("org.example");
        CommandRecorder  r = new CommandRecorder(this, cr.getCommandTree(), "fair");
        r.register();
    }
}
