package org.example;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;
import net.kyori.adventure.text.minimessage.MiniMessage;


@CommandClass(subCommandClass = {})
public class FindClass {

    @CommandCall
    public static void addOne(CommandContext<CommandSourceStack> ctx) {
        var player = ctx.getSource().getExecutor();
        player.sendMessage(MiniMessage.miniMessage().deserialize("Calling:  " + player.getName()));
    }

}
