package dev.turtin.turtinlib.client.modmenu;

import com.terraformersmc.modmenu.api.UpdateChannel;
import com.terraformersmc.modmenu.api.UpdateChecker;
import com.terraformersmc.modmenu.api.UpdateInfo;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class LibUpdateChecker implements UpdateChecker {
    @Override
    public @Nullable UpdateInfo checkForUpdates() {
        return new UpdateInfo() {
            @Override
            public boolean isUpdateAvailable() {
                return true;
            }

            @Override
            public @Nullable String getDownloadLink() {
                return "https://github.com/Turtin/TurtinLib/releases";
            }

            @Override
            public @NotNull UpdateChannel getUpdateChannel() {
                return UpdateChannel.RELEASE;
            }
        };
    }
}
