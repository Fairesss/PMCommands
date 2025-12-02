package it.fair.McCommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class CommandRecorder {
    private final CommandTree commandTree;
    private final String prefix;
    private final JavaPlugin plugin;

    public CommandRecorder(JavaPlugin plugins, CommandTree commandTree, String prefix) {
        this.commandTree = commandTree;
        this.prefix = prefix;
        this.plugin = plugins;
    }

    public LiteralCommandNode<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.prefix);

//        builder.then(register(this.commandTree.getRoot()).build());
        this.commandTree.getRoot().children().forEach(c -> {
            builder.then(register(c).build());
        });
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(builder.build()));
        return null;
    }


    private LiteralArgumentBuilder<CommandSourceStack> register(CommandTreeNode root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(root.command().alias());
        this.addCommands(builder, root);
        root.children().forEach(c -> builder.then(register(c).build()));
        return builder;
    }

    private void addCommands(LiteralArgumentBuilder<CommandSourceStack> builder, CommandTreeNode node) {
        node.command().method().forEach(command -> builder.then(Commands.literal(command.getName()).executes(ctx -> {
            try {
                command.invoke(null, ctx);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return 1;
        })));
    }
}
