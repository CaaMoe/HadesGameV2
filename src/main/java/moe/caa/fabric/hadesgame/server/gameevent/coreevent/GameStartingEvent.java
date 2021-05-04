package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.GameState;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;

import java.util.Random;

public class GameStartingEvent extends AbstractEvent {
    public GameStartingEvent() {
        super("starting", "开始游戏", true, 18, 20);
    }

    @Override
    public void tickCountdownSecond(int countdown) {
        switch (countdown) {
            case 15:
            case 10:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                GameCore.INSTANCE.sendAllMessage(new LiteralText("\u00a7e游戏将在 \u00a7c" + countdown + " \u00a7e秒后开始"));
                GameCore.INSTANCE.allPlayerHandler(playerEntity -> {
                            GameCore.INSTANCE.playSound(playerEntity, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.PLAYERS, 10, 1);
                            GameCore.INSTANCE.sendTitle(playerEntity, new LiteralText("\u00a7c" + countdown), new LiteralText("\u00a7e游戏即将开始"), 0, 10, 20);
                        }
                );
        }
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.currentState = GameState.GAMING;
        ServerWorld serverWorld = HadesGame.server.get().getOverworld();
        int[] a = getCenter(serverWorld);

        GameCore.INSTANCE.allPlayerHandler(playerEntity -> {

            playerEntity.teleport(serverWorld, a[0], a[1] + 1, a[2], 0, 0);
            serverWorld.getWorldBorder().setCenter(a[0], a[2]);
            serverWorld.getWorldBorder().interpolateSize(10, 500, 5000);
            HadesGameScheduleManager.INSTANCE.runTask.add(new AbstractTick() {
                @Override
                protected void tick() {
                    int y = serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, a[0], a[2]);
                    playerEntity.teleport(serverWorld, a[0], y, a[2], 0, 0);
                    GameCore.INSTANCE.clearState(playerEntity, GameMode.SURVIVAL);
                    playerEntity.setGameMode(GameMode.SURVIVAL);
                    GameCore.INSTANCE.playSound(playerEntity, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.PLAYERS, 10, 1);
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH ,20 * 60 * 2, 2));
                }
            });
        });
    }

    private int[] getCenter(ServerWorld world) {
        Random random = new Random();
        int x = -20000000 + random.nextInt(40000000);
        int z = -20000000 + random.nextInt(40000000);
        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
        return new int[]{x, y, z};
    }
}
