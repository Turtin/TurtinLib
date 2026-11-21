package dev.turtin.turtinlib.client.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public interface CommandHandler {
    int execute(CommandContext<FabricClientCommandSource> context);
}
