package moe.caa.fabric.hadesgame.server;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class Location {
    public final Vec3d pos;
    public final ServerWorld world;
    public final float yaw;
    public final float pitch;

    public Location(Vec3d pos, ServerWorld world, float yaw, float pitch) {
        this.pos = pos;
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static Location valueOf(LivingEntity entity) {
        return new Location(entity.getPos(), (ServerWorld) entity.world, entity.bodyYaw, entity.prevPitch);
    }

    @Override
    public Location clone() {
        return new Location(new Vec3d(pos.x, pos.y, pos.z), world, yaw, pitch);
    }
}














































