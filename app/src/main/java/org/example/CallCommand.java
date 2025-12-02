package org.example;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;
import net.kyori.adventure.text.minimessage.MiniMessage;


@CommandClass(alias = "call", subCommandClass = {"org.example.sub"})
public class CallCommand {

    @CommandCall
    public static void call(CommandContext<CommandSourceStack> ctx) {
        var player = ctx.getSource().getExecutor();
        player.sendMessage(MiniMessage.miniMessage().deserialize("Calling:  " + player.getName()));
    }
}
