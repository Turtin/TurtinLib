package dev.turtin.turtinlib.client;

import dev.turtin.turtinlib.client.command.CommandRegistration;
import net.fabricmc.api.ClientModInitializer;

public class TurtinlibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommandRegistration.SimpleRegistration.register(this.getClass());
        CommandRegistration.register(TestCommand.class);
    }
}
