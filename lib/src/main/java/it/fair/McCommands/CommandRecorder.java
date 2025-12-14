package it.fair.McCommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class CommandRecorder {
    private final CommandTree commandTree;
    private final String prefix;
    private final JavaPlugin plugin;
    private final Logger logger = LoggerFactory.getLogger(CommandRecorder.class);

    public CommandRecorder(JavaPlugin plugins, CommandTree commandTree, String prefix) {
        this.commandTree = commandTree;
        this.prefix = prefix;
        this.plugin = plugins;
    }

    public void register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.prefix);

        //        builder.then(register(this.commandTree.getRoot()).build());
        this.commandTree.getRoot().children().forEach(c -> {
            builder.then(register(c).build());
        });
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> commands.registrar().register(builder.build()));
    }


    private LiteralArgumentBuilder<CommandSourceStack> register(CommandTreeNode root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(root.command().alias());
        this.addCommands(builder, root);
        root.children().forEach(c -> builder.then(register(c).build()));
        return builder;
    }

    private void addCommands(LiteralArgumentBuilder<CommandSourceStack> builder, CommandTreeNode node) {
        node.command().method().forEach(command -> {
            var c = Commands.literal(command.getName());
            addArguments(c, command);
            builder.then(c.build());
        });
    }

    private void addArguments(LiteralArgumentBuilder<CommandSourceStack> root, Method command) {
        var params = Command.getParameters(command).entrySet().iterator();

        if (!params.hasNext()) {
            root.executes(
                    ctx -> {
                        try {
                            command.invoke(null, ctx);
                            return 0;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } else {
            var param = params.next();
            logger.warn("Adding parameters {}", param.getKey());
            var arg = Commands.argument(param.getKey(), Command.ArgumentTypeResolver.resolve(param.getValue()));
            addArguments(params, arg, command);
            root.then(arg);
        }

//        Command.getParameters(command).forEach(
//                (name, type) ->
//                        c.then(Commands.argument(name, IntegerArgumentType.integer()).executes(ctx -> {
//            try {
//                Object args =  ctx.getArgument(name, type);
//                command.invoke(null, ctx, args);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//            return 1;
//        })));
    }

    private void putExecutes(RequiredArgumentBuilder<CommandSourceStack, ?> node, Method command) {
        node.executes(
                ctx -> {
                    try {

                        var param = new ArrayList<>();
                        param.add(ctx);
                        for (var val : Command.getParameters(command).entrySet()) {
                            logger.warn("name {} type {}", val.getKey(), val.getValue());
                            param.add(ctx.getArgument(val.getKey(), val.getValue()));
                        }

                        command.invoke(null, param.toArray());
                        return 0;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void addArguments(Iterator<Map.Entry<String, Class<?>>> params, RequiredArgumentBuilder<CommandSourceStack, ?> args, Method command) {
        if (!params.hasNext()) {
            logger.warn("Adding execute to {} ", command.getName());
            putExecutes(args, command);
        } else {
            var param = params.next();
            var arg = Commands.argument(param.getKey(), Command.ArgumentTypeResolver.resolve(param.getValue()));
            logger.warn("Added parameter {}", arg.getName());
            addArguments(params, arg, command);
            args.then(arg);
        }
    }
}
