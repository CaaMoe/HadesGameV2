package moe.caa.fabric.hadesgame.server;

import moe.caa.fabric.hadesgame.server.gameevent.*;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import moe.caa.fabric.hadesgame.server.scoreboard.ScoreboardHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HadesGame implements ModInitializer {
    public static final WeightRandomArrayList<StatusEffect> STATUS_EFFECT_WEIGHT_RANDOM_ARRAY_LIST = new WeightRandomArrayList<>();
    public static final WeightRandomArrayList<Item> ITEM_WEIGHT_RANDOM_ARRAY_LIST = new WeightRandomArrayList<>();
    public static final WeightRandomArrayList<Block> BLOCK_WEIGHT_RANDOM_ARRAY_LIST = new WeightRandomArrayList<>();
    public static Optional<MinecraftServer> server = Optional.empty();
    public static List<String> scoreboardTemp = new ArrayList<>();
    private static Location loc;

    // 大厅位置
    public static Location getLobbyLocation() {
        return loc;
    }

    public static void randomLobbyLocation() {
        int x;
        int z;
        Block block;
        do {
            x = (int) (-20000000 + Math.random() * 40000000);
            z = (int) (-20000000 + Math.random() * 40000000);

            server.get().getOverworld().getChunk(x >> 4, z >> 4);
            int y = server.get().getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

            do {
                block = server.get().getOverworld().getBlockState(new BlockPos(x, y--, z)).getBlock();
            } while (block == Blocks.AIR && y > -64);

        } while (block instanceof FluidBlock || block == Blocks.AIR);

        System.out.println("下轮游戏: x = " + x + ", z = " + z);
        loc = new Location(new Vec3d(x, 302, z), server.get().getOverworld(), 0, 0);
        server.get().getOverworld().getWorldBorder().setCenter(x, z);
        server.get().getOverworld().getWorldBorder().setSize(200);

        placeLobbyBlock();
    }

    public static void deleteDir(File file) {
        //判断是否为文件夹
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    deleteDir(value);
                }
            }
        }
        file.delete();
    }

    private static void placeLobbyBlock() {
        // 放置大厅方块
        HadesGameScheduleManager.runTask(() -> {
            ServerWorld world = server.get().getOverworld();

            final Location lobbyLocation = getLobbyLocation();

            final double x = lobbyLocation.pos.x;
            final double z = lobbyLocation.pos.z;
            // 大厅地面

            for (int i = (int) (x - 10); i < (x + 10); i++) {

                for (int j = (int) (z - 10); j < (z + 10); j++) {
                    world.setBlockState(new BlockPos(i, 300, j), Blocks.BARRIER.getDefaultState());
                }
            }


            // 大厅墙壁
            for (int i = -10; i < 10; i++) {
                for (int y = 300; y < 308; y++) {
                    world.setBlockState(new BlockPos((int) (x + i), y, (int) (z + 10)), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos((int) (x + i), y, (int) (z - 10)), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos((int) (x + 10), y, (int) (z + i)), Blocks.BARRIER.getDefaultState());
                    world.setBlockState(new BlockPos((int) (x - 10), y, (int) (z + i)), Blocks.BARRIER.getDefaultState());
                }
            }
        });
    }

    public static void removeLobbyBlock() {
        ServerWorld world = server.get().getOverworld();

        final Location lobbyLocation = getLobbyLocation();

        // 大厅地面
        final double x = lobbyLocation.pos.x;
        final double z = lobbyLocation.pos.z;
        for (int i = (int) (x - 10); i < (x + 10); i++) {

            for (int j = (int) (z - 10); j < (z + 10); j++) {
                world.setBlockState(new BlockPos(i, 300, j), Blocks.AIR.getDefaultState());
            }
        }


        // 大厅墙壁
        for (int i = -10; i < 10; i++) {
            for (int y = 300; y < 308; y++) {
                world.setBlockState(new BlockPos((int) (x + i), y, (int) (z + 10)), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos((int) (x + i), y, (int) (z - 10)), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos((int) (x + 10), y, ((int) (z + i))), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos((int) (x - 10), y, ((int) (z + i))), Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public void onInitialize() {

        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> HadesGameCommand.register(dispatcher));

        // 赋值服务端实例
        ServerLifecycleEvents.SERVER_STARTING.register(server1 -> {
            server = Optional.of(server1);
            HadesGameScheduleManager.runTask(HadesGame::randomLobbyLocation);
        });

        // 初始化Schedule调度器
        HadesGameScheduleManager.INSTANCE.init();

        // 初始化计分板
        ScoreboardHandler.INSTANCE.init();

        // 初始化游戏核心
        GameCore.INSTANCE.init();

        // 设置计分板标题
        ScoreboardHandler.INSTANCE.currentTitle = "§e§l阴间游戏";

        // 设置动态模板
        scoreboardTemp.add("§7{0} ");
        scoreboardTemp.add("");
        scoreboardTemp.add("§f下一事件:");
        scoreboardTemp.add("§a{1}  §7{2}");
        scoreboardTemp.add(" ");
        scoreboardTemp.add("§a存活: §c{3}");
        scoreboardTemp.add("  ");
        scoreboardTemp.add("§a边界: §c{4}");
        scoreboardTemp.add("   ");
        scoreboardTemp.add("§7{5}");
        scoreboardTemp.add("§e(●'◡'●)");


        // 设置计分板内容
        ScoreboardHandler.INSTANCE.currentContent.clear();
        ScoreboardHandler.INSTANCE.currentContent.addAll(scoreboardTemp);

        // 注册事件

        // 交换位置
        GameCore.INSTANCE.eventList.add(new SwapLocationEvent(), 10);

        // 治疗
        GameCore.INSTANCE.eventList.add(new TreatmentEvent(), 2);

        // 交换背包
        GameCore.INSTANCE.eventList.add(new SwapInventoryEvent(), 10);

        // 交换生命
        GameCore.INSTANCE.eventList.add(new SwapHealthEvent(), 10);

        // 雷暴
        GameCore.INSTANCE.eventList.add(new ThunderstormEvent(), 10);

        // 随机效果
        GameCore.INSTANCE.eventList.add(new RandomEffectEvent(), 10);

        // 铁砧雨
        GameCore.INSTANCE.eventList.add(new RainAnvilEvent(), 10);

        // 背包破损
        GameCore.INSTANCE.eventList.add(new RandomClearInventorySectionEvent(), 10);

        // 三倍掉落
        GameCore.INSTANCE.eventList.add(new TripleDropEvent(), 5);

        // 随机掉落
        GameCore.INSTANCE.eventList.add(new RandomDropEvent(), 10);

        // 昏乱
        GameCore.INSTANCE.eventList.add(new RandomMoveEvent(), 10);

        // 方块雨
        GameCore.INSTANCE.eventList.add(new RainBlockEvent(), 5);

        // 沧海桑田
        // GameCore.INSTANCE.eventList.add(new TickSpeedUp(), 10);

        // 腐蚀
        GameCore.INSTANCE.eventList.add(new CorrosionEvent(), 10);

        // 超级矿工
        GameCore.INSTANCE.eventList.add(new SuperMinersEvent(), 10);

        // 爬
        GameCore.INSTANCE.eventList.add(new ClimbEvent(), 10);

        // 行为管制
        GameCore.INSTANCE.eventList.add(new CloseWindowEvent(), 10);

        // 发大财啦
        GameCore.INSTANCE.eventList.add(new GoldHereEvent(),10);

        // 木头人
        GameCore.INSTANCE.eventList.add(new WoodenManEvent(),10);

        // 梯恩梯润
        GameCore.INSTANCE.eventList.add(new TNTRunEvent(),10);

        // 凛冬将至
        GameCore.INSTANCE.eventList.add(new FreezeEffectEvent(),10);

        // 删除老的世界
        File file = new File("world");
        try {
            deleteDir(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
