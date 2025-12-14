package it.fair.McCommands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import java.util.function.Supplier;

public record Command(String pkg, Class<?> clazz, List<Method> method, String alias, List<String> subPackages) {
    public static Map<String, Class<?>> getParameters(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(param -> !param.getType().equals(CommandContext.class) || param.isAnnotationPresent(Param.class))
                .collect(Collectors.toMap(p -> p.getAnnotation(Param.class).name(), Parameter::getType));
    }



    public static class ArgumentTypeResolver {

        private static final Map<Class<?>, ArgumentType<?>> TYPE_MAP = new HashMap<>();

        static {

            // Primitive
            TYPE_MAP.put(int.class, IntegerArgumentType.integer());
            TYPE_MAP.put(Integer.class, IntegerArgumentType.integer());

            TYPE_MAP.put(float.class, FloatArgumentType.floatArg());
            TYPE_MAP.put(Float.class, FloatArgumentType.floatArg());

            TYPE_MAP.put(double.class, DoubleArgumentType.doubleArg());
            TYPE_MAP.put(Double.class, DoubleArgumentType.doubleArg());
            TYPE_MAP.put(String.class, StringArgumentType.string());

            // Minecraft Specific
            TYPE_MAP.put(BlockPositionResolver.class, ArgumentTypes.blockPosition());
            TYPE_MAP.put(BlockState.class, ArgumentTypes.blockState());
            TYPE_MAP.put(Component.class, ArgumentTypes.component());
            TYPE_MAP.put(UUID.class, ArgumentTypes.uuid());
            TYPE_MAP.put(World.class, ArgumentTypes.world());
            TYPE_MAP.put(Player.class, ArgumentTypes.player());

        }

        public static void addCustomArgumentType(Class<?> clazz, ArgumentType<?> argumentType) {
            TYPE_MAP.put(clazz, argumentType);
        }

        public static ArgumentType<?> resolve(Class<?> type) {
            return TYPE_MAP.getOrDefault(type, StringArgumentType.string());
        }
    }
}
