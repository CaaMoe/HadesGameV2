package moe.caa.fabric.hadesgame.server;

import moe.caa.fabric.hadesgame.server.gameevent.*;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import moe.caa.fabric.hadesgame.server.scoreboard.ScoreboardHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HadesGame implements ModInitializer {
    public static final WeightRandomArrayList<StatusEffect> STATUS_EFFECT_WEIGHT_RANDOM_ARRAY_LIST = new WeightRandomArrayList<>();
    public static final WeightRandomArrayList<Item> ITEM_WEIGHT_RANDOM_ARRAY_LIST = new WeightRandomArrayList<>();
    public static Optional<MinecraftServer> server = Optional.empty();
    public static List<String> scoreboardTemp = new ArrayList<>();

    // 大厅位置
    public static Location getLobbyLocation() {
        return new Location(new Vec3d(0, 202, 0), server.get().getOverworld(), 0, 0);
    }

    @Override
    public void onInitialize() {

        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> HadesGameCommand.register(dispatcher));

        // 赋值服务端实例
        ServerLifecycleEvents.SERVER_STARTING.register(server1 -> server = Optional.of(server1));

        // 初始化Schedule调度器
        HadesGameScheduleManager.INSTANCE.init();

        // 初始化计分板
        ScoreboardHandler.INSTANCE.init();

        // 初始化游戏核心
        GameCore.INSTANCE.init();

        // 设置计分板标题
        ScoreboardHandler.INSTANCE.currentTitle = "\u00a7e\u00a7l阴间游戏";

        // 设置动态模板
        scoreboardTemp.add("\u00a78{0}");
        scoreboardTemp.add("");
        scoreboardTemp.add("\u00a7f下一事件:");
        scoreboardTemp.add("\u00a7a{1}  \u00a77{2}");
        scoreboardTemp.add(" ");
        scoreboardTemp.add("\u00a7a存活： \u00a7c{3}");
        scoreboardTemp.add("  ");
        scoreboardTemp.add("\u00a7a边界： \u00a7c{4}");
        scoreboardTemp.add("   ");
        scoreboardTemp.add("\u00a7ewww.gbcraft.org");


        // 设置计分板内容
        ScoreboardHandler.INSTANCE.currentContent.clear();
        ScoreboardHandler.INSTANCE.currentContent.addAll(scoreboardTemp);

        // 注册事件

        // 交换位置
        GameCore.INSTANCE.eventList.add(new SwapLocationEvent(), 4);

        // 治疗
        GameCore.INSTANCE.eventList.add(new TreatmentEvent(), 2);

        // 交换背包
        GameCore.INSTANCE.eventList.add(new SwapInventoryEvent(), 3);

        // 交换生命
        GameCore.INSTANCE.eventList.add(new SwapHealthEvent(), 5);

        // 缩圈
        GameCore.INSTANCE.eventList.add(new NarrowRangeEvent(), 3);

        // 发光
        GameCore.INSTANCE.eventList.add(new LuminescenceEvent(), 2);

        // 雷暴
        GameCore.INSTANCE.eventList.add(new ThunderstormEvent(), 4);

        // 随机效果
        GameCore.INSTANCE.eventList.add(new RandomEffectEvent(), 10);

        // 铁砧雨
        GameCore.INSTANCE.eventList.add(new RainAnvilEvent(), 3);

        // 背包破损
        GameCore.INSTANCE.eventList.add(new RandomClearInventorySectionEvent(), 4);

        // 三倍掉落
        GameCore.INSTANCE.eventList.add(new TripleDropEvent(), 1);

        // 随机掉落
        GameCore.INSTANCE.eventList.add(new RandomDropEvent(), 1);

        // 昏乱
        GameCore.INSTANCE.eventList.add(new RandomMoveEvent(), 1);

        // 放置大厅方块
        HadesGameScheduleManager.runTask(()->{
            ServerWorld world = server.get().getOverworld();

            // 大厅地面
            for (int i = -10; i < 10; i++) {
                for (int j = -10; j < 10; j++) {
                    world.setBlockState(new BlockPos(i, 200, j), Blocks.BARRIER.getDefaultState());
                }
            }

            // 大厅墙壁
            for (int i = -10; i < 10; i++) {
                for (int j = 200; j < 208; j++) {
                    world.setBlockState(new BlockPos(10, j, i), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos(-10, j, i), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos(i, j, 10), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos(i, j, -10), Blocks.BARRIER.getDefaultState());
                }
            }
        });
    }
}
