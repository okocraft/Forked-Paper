package net.okocraft.paper;

import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public final class CustomWorldFile {

    @SuppressWarnings("resource")
    public static Stream<WorldCreator> read(Path filepath) throws IOException {
        return Files.lines(filepath, StandardCharsets.UTF_8)
                .flatMap(CustomWorldFile::readLine)
                .filter(Objects::nonNull);
    }

    private static Stream<WorldCreator> readLine(String originalLine) {
        var line = originalLine.trim().replace(" ", "");

        if (line.startsWith("#")) {
            return null;
        }

        String worldName;
        World.Environment environment;

        int i = line.indexOf(":");

        if (i == -1) {
            worldName = line;
            environment = World.Environment.NORMAL;
        } else if (i + 1 == line.length()) {
            worldName = line.substring(0, i);
            environment = World.Environment.NORMAL;
        } else {
            worldName = line.substring(0, i);
            environment = switch (line.substring(i + 1).toLowerCase(Locale.ENGLISH)) {
                case "nether" -> World.Environment.NETHER;
                case "end", "the_end" -> World.Environment.THE_END;
                default -> World.Environment.NORMAL;
            };
        }

        if (worldName.isEmpty()) {
            return null;
        }

        return Stream.of(WorldCreator.name(worldName).environment(environment));
    }

    private CustomWorldFile() {
        throw new UnsupportedOperationException();
    }
}
