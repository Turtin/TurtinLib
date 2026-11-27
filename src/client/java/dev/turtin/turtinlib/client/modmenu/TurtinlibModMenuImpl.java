package dev.turtin.turtinlib.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.UpdateChecker;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class TurtinlibModMenuImpl implements ModMenuApi {
    @Override
    public @Nullable UpdateChecker getUpdateChecker() {
        return new LibUpdateChecker();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(Component.literal("Test"), parent);
    }
}
