package gg.airplane.structs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public final class OptimizeSuffocation {

    public static boolean shouldCheckForSuffocation(LivingEntity entity, ServerLevel world) {
        if (entity instanceof WitherBoss) {
            return true;
        }
        return entity.tickCount % 10 == 0 && !entity.isInvulnerableTo(world, entity.damageSources().inWall());
    }

    private OptimizeSuffocation() {
        throw new UnsupportedOperationException();
    }
}
