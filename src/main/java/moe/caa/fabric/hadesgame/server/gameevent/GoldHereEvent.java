package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GoldHereEvent extends ImplicitAbstractEvent{
    private final List<AbstractTick> eventTick = new ArrayList<>();
    private final Map<ServerPlayerEntity,BlockPos> lastPosMap = new HashMap<>();
    public GoldHereEvent() {
        super("goldHere", "发大财啦", true, 30, 100);
    }
    @Override
    public void callEvent() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        AbstractTick tick = new AbstractTick(10) {
            int count = 0;
            @Override
            protected void tick() {
                for (ServerPlayerEntity p : GameCore.INSTANCE.getSurvivalPlayers())
                {
                    if(count++ >= 60)this.cancel();
                    BlockPos location = p.getBlockPos().add(0,-1,0);
                    World world = p.getWorld();

                    Block block;
                    int randomInt = random.nextInt(100);
                    if(randomInt <= 15)block = Blocks.COAL_ORE;
                    else if(randomInt <= 30)block = Blocks.COPPER_ORE;
                    else if(randomInt <= 40)block = Blocks.IRON_ORE;
                    else if(randomInt <= 60)block = Blocks.GOLD_ORE;
                    else if(randomInt <= 70)block = Blocks.DIAMOND_ORE;
                    else if(randomInt <= 80)block = Blocks.EMERALD_ORE;
                    else if(randomInt <= 90)block = Blocks.REDSTONE_ORE;
                    else block = Blocks.LAPIS_ORE;

                    GameCore.INSTANCE.playSound(p, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.AMBIENT, 1, 1);
                    //防止玩家在同一个位置刷矿
                    if(location != lastPosMap.get(p))
                    {
                        lastPosMap.put(p,location);
                        world.setBlockState(location, block.getDefaultState());
                        world.updateNeighbors(location, block);
                        HadesGameScheduleManager.runTaskLater(()->{
                            world.setBlockState(location, Blocks.LAVA.getDefaultState());
                            world.updateNeighbors(location, Blocks.LAVA);
                        },20 * 4);
                    }}}};
        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }
    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
