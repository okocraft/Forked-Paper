package net.okocraft.paper;

import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class ResourceWorldNumberFile {

    public static Stream<WorldCreator> read(Path filepath) throws IOException {
        if (Files.isRegularFile(filepath) && Files.isReadable(filepath)) {
            var lines = Files.readAllLines(filepath, StandardCharsets.UTF_8);
            if (lines.isEmpty() || lines.getFirst().isEmpty()) {
                return Stream.empty();
            } else {
                return resourceWorlds(lines.getFirst());
            }
        }

        return Stream.empty();
    }

    private static Stream<WorldCreator> resourceWorlds(String suffix) {
        var worldName = "resource_" + suffix;
        return Stream.of(
                WorldCreator.name(worldName).environment(World.Environment.NORMAL),
                WorldCreator.name(worldName + "_nether").environment(World.Environment.NETHER),
                WorldCreator.name(worldName + "_the_end").environment(World.Environment.THE_END)
        );
    }
}
