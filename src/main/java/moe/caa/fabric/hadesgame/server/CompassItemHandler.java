package moe.caa.fabric.hadesgame.server;

import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CompassItemHandler {
    public static final CompassItemHandler INSTANCE = new CompassItemHandler();
    private final Map<World, List<ServerPlayerEntity>> players = new Hashtable<>();
    private final Map<UUID, Integer> index = new Hashtable<>();
    private final Map<UUID, ServerPlayerEntity> compass = new Hashtable<>();

    private CompassItemHandler() {
        HadesGameScheduleManager.runTaskTimer(this::tick, 10);
        HadesGameScheduleManager.runTaskTimer(this::genPlayerList, 40);
    }

    public void put(World world, ServerPlayerEntity user, ItemStack itemStack) {
        int index = this.index.getOrDefault(user.getUuid(), 0);
        List<ServerPlayerEntity> entities = players.get(user.getWorld());
        if (entities == null) return;
        if (entities.size() <= index) {
            index = 0;
        }
        ServerPlayerEntity target = entities.get(index++);
        compass.put(user.getUuid(), target);
        this.index.put(user.getUuid(), index);
    }

    public void genPlayerList() {
        synchronized (players) {
            players.clear();
            for (ServerPlayerEntity player : GameCore.INSTANCE.getSurvivalPlayers()) {
                if (!players.containsKey(player.getWorld())) {
                    players.put(player.getWorld(), Collections.synchronizedList(new ArrayList<>()));
                }
                players.get(player.getWorld()).add(player);
            }
        }
    }

    private void tick() {
        MinecraftServer server = HadesGame.server.get();
        for (Map.Entry<UUID, ServerPlayerEntity> entry : compass.entrySet()) {
            ServerPlayerEntity owner = server.getPlayerManager().getPlayer(entry.getKey());
            if (owner == null) continue;
            ServerPlayerEntity target = entry.getValue();
            if (!target.isAlive() || target.getWorld() != owner.getWorld()) {
                if (owner.getMainHandStack().getItem() instanceof CompassItem || owner.getOffHandStack().getItem() instanceof CompassItem)
                    owner.sendMessage(Text.literal("§c目标已丢失，请重新指定目标"), true);

                continue;
            }

            owner.networkHandler.sendPacket(
                    new PlayerSpawnPositionS2CPacket(
                            new BlockPos(
                                    target.getBlockPos().getX(),
                                    target.getBlockPos().getY(),
                                    target.getBlockPos().getZ()),
                            target.headYaw));
            if (owner.getMainHandStack().getItem() instanceof CompassItem || owner.getOffHandStack().getItem() instanceof CompassItem)
                owner.sendMessage(Text.literal("§a目标 \u00a77: " + target.getGameProfile().getName()
                        + "    " + "\u00a7a距离 \u00a77: " + passDouble(target.getBlockPos(), owner.getBlockPos())), true);
        }
    }

    private String passDouble(BlockPos pos1, BlockPos pos2) {
        return String.format("%.1f", Math.abs(Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2) + Math.pow(pos1.getZ() - pos2.getZ(), 2))));
    }
}
