package net.okocraft.paper;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import org.bukkit.World;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public record CustomWorld(
        String name,
        World.Environment environment,
        ResourceKey<LevelStem> stemKey,
        ResourceKey<Level> dimensionKey
) {

    public static List<CustomWorld> readCustomWorldFile(Path filepath) throws IOException {
        try (Stream<String> lines = Files.lines(filepath, StandardCharsets.UTF_8)) {
            return lines.map(CustomWorld::readCustomWorldFileLine)
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private static CustomWorld readCustomWorldFileLine(String originalLine) {
        String line = originalLine.trim().replace(" ", "");

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

        return createWorldInfo(worldName, environment);
    }

    public static List<CustomWorld> readResourceNumberFile(Path filepath) throws IOException {
        if (Files.isRegularFile(filepath) && Files.isReadable(filepath)) {
            List<String> lines = Files.readAllLines(filepath, StandardCharsets.UTF_8);

            if (!lines.isEmpty() && !lines.getFirst().isEmpty()) {
                return resourceWorlds(lines.getFirst());
            }
        }

        return List.of();
    }

    private static List<CustomWorld> resourceWorlds(String suffix) {
        String worldName = "resource_" + suffix;
        return List.of(
                createWorldInfo(worldName, World.Environment.NORMAL),
                createWorldInfo(worldName + "_nether", World.Environment.NETHER),
                createWorldInfo(worldName + "_the_end", World.Environment.THE_END)
        );
    }

    private static CustomWorld createWorldInfo(String worldName, World.Environment environment) {
        return new CustomWorld(worldName, environment, toLevelStemKey(environment), ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(Identifier.DEFAULT_NAMESPACE, worldName)));
    }

    private static ResourceKey<LevelStem> toLevelStemKey(World.Environment environment) {
        return switch (environment) {
            case NETHER -> LevelStem.NETHER;
            case THE_END -> LevelStem.END;
            default -> LevelStem.OVERWORLD;
        };
    }
}
