package net.okocraft.paper;

import com.destroystokyo.paper.VersionHistoryManager;
import com.destroystokyo.paper.util.VersionFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

public class OkocraftVersionFetcher implements VersionFetcher {

    private static final String OKOCRAFT_PAPER_REPO = "https://github.com/okocraft/Forked-Paper";

    @Override
    public long getCacheTime() {
        return Long.MAX_VALUE; // prevent re-fetching
    }

    @Override
    public @NotNull Component getVersionMessage(final @NotNull String serverVersion) {
        TextComponent.Builder builder =
                text().append(text("This server software is maintained by OKOCRAFT."))
                        .append(newline())
                        .append(text("Repository: "))
                        .append(text(OKOCRAFT_PAPER_REPO)
                                .hoverEvent(text("Click to open", NamedTextColor.WHITE))
                                .clickEvent(ClickEvent.openUrl(OKOCRAFT_PAPER_REPO))
                        );


        final @Nullable Component history = this.getHistory();

        return history != null ? builder.append(newline()).append(history).build() : builder.build();
    }

    private @Nullable Component getHistory() {
        final VersionHistoryManager.VersionData data = VersionHistoryManager.INSTANCE.getVersionData();
        if (data == null) {
            return null;
        }

        final String oldVersion = data.getOldVersion();
        if (oldVersion == null) {
            return null;
        }

        return text("Previous version: " + oldVersion, NamedTextColor.GRAY, TextDecoration.ITALIC);
    }
}
