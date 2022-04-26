package moe.caa.fabric.hadesgame.server;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Contains {

    // 不会随机掉落的物品列表
    public static final Set<UUID> doNntModifyDropType = ConcurrentHashMap.newKeySet();

    public static boolean tickSpeedUp = false;

    public static boolean superMiners = false;

}
