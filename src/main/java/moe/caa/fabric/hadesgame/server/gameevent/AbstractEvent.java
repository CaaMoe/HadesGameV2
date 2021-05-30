package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public abstract class AbstractEvent {
    public final String EVENT_NAME;

    // 事件英文ID
    public final String EVENT_ID;

    // 事件是否需要倒计时
    public final boolean SHOULD_COUNTDOWN;

    // 事件随机倒计时的最小值
    public final int COUNTDOWN_RANDOM_MIN;

    // 事件随机倒计时的最大值
    public final int COUNTDOWN_RANDOM_MAX;

    public AbstractEvent(String id, String event_name, boolean should_countdown, int countdown_random_min, int countdown_random_max) {
        EVENT_ID = id;
        EVENT_NAME = event_name;
        SHOULD_COUNTDOWN = should_countdown;
        COUNTDOWN_RANDOM_MIN = countdown_random_min;
        COUNTDOWN_RANDOM_MAX = countdown_random_max;
    }

    // 每秒倒计时触发
    public void tickCountdownSecond(int countdown) {
        switch (countdown) {
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                GameCore.INSTANCE.allPlayerHandler(playerEntity -> {
                    GameCore.INSTANCE.playSound(playerEntity, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundCategory.PLAYERS, 10, 1);
                });

        }
    }

    // 获取格式化的倒计时
    public String getFormatCountDown(int countdown) {
        return String.format("%02d:%02d", countdown / 60, countdown % 60);
    }

    public AbstractEvent getNextEvent() {
        return null;
    }

    /**
     * 根据事件发生倒计时获取格式化的事件名称
     * 对于CoreEvent始终是事件名称本身
     * 对于ImplicitEvent会根据距离事件发生的时间改变
     *
     * @param countdown 距离事件发生的时间, 单位秒
     * @return 事件名称
     * @see moe.caa.fabric.hadesgame.server.gameevent.coreevent
     * @see ImplicitAbstractEvent
     */
    public abstract String getFormatEventName(int countdown);

    /**
     * 获取假事件概率，方法返回值应在区间[0,1]内，值越大，假事件几率越大
     *
     * @return 假事件几率
     * @see moe.caa.fabric.hadesgame.server.gameevent.coreevent
     */
    public abstract double getFakeEventProb();

    public abstract void callEvent();

    /**
     * 当游戏被终止时，GameCore会尝试遍历EventList字段的所有事件并调用这个方法，以供事件注销自己的所有待执行任务
     * 仅注册事件有效，核心事件无效
     *
     * @see moe.caa.fabric.hadesgame.server.HadesGame
     */
    public void gameEnd() {
    }
}
