package org.example;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import it.fair.McCommands.CommandClass;
import it.fair.McCommands.CommandCall;
import it.fair.McCommands.Param;
import net.kyori.adventure.text.minimessage.MiniMessage;

@CommandClass(alias = "call", subCommandClass = {"org.example.sub"})
public class CallCommand {
    @CommandCall
    public static void call(CommandContext<CommandSourceStack> ctx, @Param(name = "a") int a, @Param(name = "b") float b, @Param(name = "range") IntegerRangeProvider rangeProvider) {
        var player = ctx.getSource().getExecutor();
        player.sendMessage(MiniMessage.miniMessage().deserialize("Calling:  " + player.getName()));
        player.sendMessage(MiniMessage.miniMessage().deserialize("int params: " + a));
        player.sendMessage(MiniMessage.miniMessage().deserialize("Float params: " + b));

        int rangeTypeTest = 10;
        if (rangeProvider.range().contains(rangeTypeTest)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("The number is inside the range"));

        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("The number is not inside the range"));
        }

    }
}
