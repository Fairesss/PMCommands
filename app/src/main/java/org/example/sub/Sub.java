package org.example.sub;


import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;
import net.kyori.adventure.text.minimessage.MiniMessage;


@CommandClass(subCommandClass = {"org.example.sub.Double"})
public class Sub {
    @CommandCall
    public static void sub(CommandContext<CommandSourceStack> ctx){
        ctx.getSource().getExecutor().sendMessage(MiniMessage.miniMessage().deserialize("Calling from subCommand"));
    }
}
