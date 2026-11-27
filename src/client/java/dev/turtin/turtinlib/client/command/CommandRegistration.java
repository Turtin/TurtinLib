package dev.turtin.turtinlib.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.turtin.turtinlib.client.command.annotations.Command;
import dev.turtin.turtinlib.client.command.annotations.SubCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CommandRegistration {
    public static class SimpleRegistration {
        private static void registerCommand(String name, Function<CommandContext<FabricClientCommandSource>, Integer> executor) {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, env) -> dispatcher.register(literal(name)
                    .executes(executor::apply)
            ));
        }

        public static void register(Class<?> c) {
            for (Method method : c.getMethods()) {
                if (!method.isAnnotationPresent(Command.class)) {
                    continue;
                }

                if (method.getParameterCount() != 1 || !CommandContext.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("Command handler must take one CommandContext: " + method);
                }

                String commandName = method.getAnnotation(Command.class).name();

                registerCommand(commandName, context -> invokeMethod(method, context));

            }
        }
    }

    private static Method getExecutor(Class<?> c) {
        for (Method method : c.getMethods()) {
            if ((method.isAnnotationPresent(Command.class) || method.isAnnotationPresent(SubCommand.class)) && method.getName().equals("execute")) {
                return method;
            }
        }

        return null; // If no method matching the requirements is found
    }

    private static int invokeMethod(Method method, CommandContext<FabricClientCommandSource> context) {
        try {
            Class<?> c = method.getDeclaringClass();
            Class<?> outerClass = c.getEnclosingClass();
            Object instance;

            if (outerClass == null) {
                instance = c.getDeclaredConstructor().newInstance();
            } else {
                Object outerInstance = outerClass.getDeclaredConstructor().newInstance();
                instance = c.getDeclaredConstructor(outerClass).newInstance(outerInstance);
            }

            return (Integer) method.invoke(instance, context);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerCommandModules(Class<?> c, LiteralArgumentBuilder<FabricClientCommandSource> cmdBuilder) {
        for (Class<?> subClass : c.getClasses()) {
            if (!Arrays.asList(subClass.getInterfaces()).contains(CommandHandler.class)) {
                continue;
            }

            Method executor = getExecutor(subClass);
            SubCommand details = executor.getAnnotation(SubCommand.class);
            cmdBuilder.then(literal(details.name()).executes(context -> invokeMethod(executor, context)));
            registerCommandModules(subClass, cmdBuilder);
        }
    }

    public static void register(Class<?> c) {
        if (!Arrays.asList(c.getInterfaces()).contains(CommandHandler.class)) {
            throw new IllegalArgumentException("Given command class does not implement CommandHandler");
        }

        Method primaryExecutor = getExecutor(c);

        Command cmdDetails = primaryExecutor.getAnnotation(Command.class);

        LiteralArgumentBuilder<FabricClientCommandSource> cmdBuilder = literal(cmdDetails.name());
        cmdBuilder.executes(context -> invokeMethod(primaryExecutor, context));

        registerCommandModules(c, cmdBuilder);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, env) -> dispatcher.register(cmdBuilder));
    }

}
