package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WoodenManEvent extends ImplicitAbstractEvent{

    private static final List<AbstractTick> eventTick = new ArrayList<>();
    public WoodenManEvent() {
        super("woodenMan", "木头人", true, 30, 100);
    }

    @Override
    public void callEvent() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        AbstractTick tick = new AbstractTick(20 * 3) {
            int count = 0;
            @Override
            protected void tick() {
                if(count++ >= 15) this.cancel();
                int randomInt = random.nextInt(100);
                if(randomInt < 70) // 可以动
                {
                    for (ServerPlayerEntity p : GameCore.INSTANCE.getSurvivalPlayers())
                    {
                        GameCore.INSTANCE.playSound(p, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), SoundCategory.AMBIENT, 1, 1);
                    }
                }else{
                    for (ServerPlayerEntity p : GameCore.INSTANCE.getSurvivalPlayers())
                    {
                        GameCore.INSTANCE.playSound(p, SoundEvents.ENTITY_WITHER_HURT, SoundCategory.AMBIENT, 1, 1);
                    }
                    moveCheck();
                }
            }
        };

        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }
    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
    void moveCheck()
    {
        Map<ServerPlayerEntity, Vec3d> playerLocMap = new HashMap<>();
        AbstractTick tick = new AbstractTick(5) {
            int count = 0;
            @Override
            protected void tick() {
                if(count++ >= 12)this.cancel();

                for(ServerPlayerEntity p : GameCore.INSTANCE.getSurvivalPlayers())
                {
                    if(!playerLocMap.containsKey(p))playerLocMap.put(p,p.getPos());
                    if(!playerLocMap.get(p).equals(p.getPos()))
                    {
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60,1));
                    }
                    else
                    {
                        p.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 25,1));
                    }
                }
            }
        };
        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }
}
