package dev.turtin.turtinlib.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.gui.widget.ModMenuButtonWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ConfigScreen extends Screen{
    private final Screen parent;

    protected ConfigScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    String name = "Test Button ";

    @Override
    protected void init() {
        this.addRenderableWidget(ModMenuButtonWidget.builder(Component.literal(name), (button) -> {
            name += "|";
            rebuildWidgets();
        }).build());
    }
}
