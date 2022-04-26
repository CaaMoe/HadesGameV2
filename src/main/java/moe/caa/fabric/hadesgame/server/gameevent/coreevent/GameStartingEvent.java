package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.GameState;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;

public class GameStartingEvent extends CoreAbstractEvent {
    public GameStartingEvent() {
        super("starting", "开始游戏", true, 18, 20);
    }

    @Override
    public void tickCountdownSecond(int countdown) {
        switch (countdown) {
            case 15, 10, 5, 4, 3, 2, 1 -> {
                GameCore.INSTANCE.sendAllMessage(Text.literal("\u00a7e游戏将在 \u00a7c" + countdown + " \u00a7e秒后开始"));
                GameCore.INSTANCE.allPlayerHandler(playerEntity -> {
                            GameCore.INSTANCE.playSound(playerEntity, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.PLAYERS, 10, 1);
                            GameCore.INSTANCE.sendTitle(playerEntity, Text.literal("\u00a7c" + countdown), Text.literal("\u00a7e游戏即将开始"), 0, 10, 20);
                        }
                );
            }
        }
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.currentState = GameState.GAMING;
        ServerWorld serverWorld = HadesGame.server.get().getOverworld();
        int[] a = getCenter(serverWorld);

        serverWorld.getWorldBorder().setCenter(a[0], a[1]);
        serverWorld.getWorldBorder().setSize(985);
        HadesGame.removeLobbyBlock();

        GameCore.INSTANCE.allPlayerHandler(playerEntity -> {
            final int x = (int) playerEntity.getPos().getX();
            final int z = (int) playerEntity.getPos().getZ();
            playerEntity.teleport(serverWorld, x, serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z), z, playerEntity.getYaw(), playerEntity.getPitch());
            HadesGameScheduleManager.INSTANCE.runTask.add(new AbstractTick() {
                @Override
                protected void tick() {
                    GameCore.INSTANCE.clearState(playerEntity, GameMode.SURVIVAL);
                    playerEntity.changeGameMode(GameMode.SURVIVAL);
                    GameCore.INSTANCE.playSound(playerEntity, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.PLAYERS, 10, 1);
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20 * 60 * 2, 2));
                }
            });
        });
    }

    private int[] getCenter(ServerWorld world) {
        int x = ((int) HadesGame.getLobbyLocation().pos.x);
        int z = ((int) HadesGame.getLobbyLocation().pos.z);
        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
        return new int[]{x, z};
    }
}
