package moe.caa.fabric.hadesgame.server;

import moe.caa.fabric.hadesgame.server.fabric.customevent.LivingEntityChangeHealthEvent;
import moe.caa.fabric.hadesgame.server.fabric.customevent.PlayerJoinEvent;
import moe.caa.fabric.hadesgame.server.fabric.customevent.PlayerQuitEvent;
import moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent;
import moe.caa.fabric.hadesgame.server.gameevent.coreevent.GameEndingEvent;
import moe.caa.fabric.hadesgame.server.gameevent.coreevent.GameStartingEvent;
import moe.caa.fabric.hadesgame.server.gameevent.coreevent.GameWaitingEvent;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import moe.caa.fabric.hadesgame.server.scoreboard.ScoreboardHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.border.WorldBorder;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameCore {
    public static final GameCore INSTANCE = new GameCore();
    public final WeightRandomArrayList<AbstractEvent> eventList = new WeightRandomArrayList<>();
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
    public GameState currentState = GameState.WAITING;
    public AbstractEvent nextEvent = new GameWaitingEvent();
    public int currentCountdown = 0;
    private GameState lastState = null;
    private int currentSurvivalPlayerNumber = 0;
    private AbstractTick tick = null;

    private GameCore() {

    }

    protected void tick() {

        // 是否交换
        boolean isSwapState = currentState != lastState;
        lastState = currentState;

        if (currentState == GameState.WAITING) {
            if (isSwapState) {
                clearAllPlayerState(GameMode.ADVENTURE);
                allPlayerHandler(playerEntity -> teleport(playerEntity, HadesGame.getLobbyLocation()));
                clearWorld();
            }
        } else if (currentState == GameState.STARTING) {
            if (isSwapState) {
                setEvent(new GameStartingEvent());
            }
        } else if (currentState == GameState.GAMING) {
            if (currentSurvivalPlayerNumber <= 1) {
                endGame();
            }
        }

        if (currentState != GameState.GAMING) {
            if (0.5 > Math.random()) {
                clearWorld();
            }
        }

        testCallEvent();
        updateScoreboardContent();
    }

    private void clearWorld() {
        // 设置天气
        HadesGame.server.get().getOverworld().setWeather(6000, 0, false, false);

        // 设置时间
        HadesGame.server.get().getOverworld().setTimeOfDay(1000);

        // 清理实体
        try {
            (HadesGame.server.get().getOverworld()).iterateEntities().forEach(entity -> {
                if (!(entity instanceof ServerPlayerEntity)) {
                    if (entity != null)
                        entity.kill();
                }
            });
        } catch (Exception e) {
        }

        HadesGame.server.get().getOverworld().getWorldBorder().setCenter(HadesGame.getLobbyLocation().pos.x, HadesGame.getLobbyLocation().pos.z);
        HadesGame.server.get().getOverworld().getWorldBorder().setSize(100);
    }

    // 设置事件
    private void setEvent(AbstractEvent event) {
        nextEvent = event;
        if (nextEvent.SHOULD_COUNTDOWN) {
            currentCountdown = nextEvent.COUNTDOWN_RANDOM_MIN + new Random().nextInt(nextEvent.COUNTDOWN_RANDOM_MAX - nextEvent.COUNTDOWN_RANDOM_MIN);
        }

        WorldBorder worldBorder = HadesGame.server.get().getOverworld().getWorldBorder();
        double size = worldBorder.getSize();
        if (size > 50) {
            size = Math.max(size - 40 - Math.random() * 10, 50);
        } else {
            size = Math.max(size - 6 - Math.random() * 4, 1);
        }
        double size0 = worldBorder.getSize();
        if (size != size0) {
            worldBorder.interpolateSize(size0, size, 10000);
        }
    }

    // 事件tick
    private void testCallEvent() {
        if (tick.getTickNumber() == 0 || tick.getTickNumber() % 20 == 0) {
            currentCountdown--;
            if (nextEvent.SHOULD_COUNTDOWN)
                nextEvent.tickCountdownSecond(currentCountdown);
            if (currentCountdown == 0) {
                if (nextEvent.getFakeEventProb() < Math.random()) {
                    runPrintException(nextEvent, AbstractEvent::callEvent);
                } else {
                    allPlayerHandler(p -> {
                        p.sendMessage(Text.literal("\u00a7c\u00a7lFAKE EVENT"), true);
                    });
                }
                if (nextEvent.SHOULD_COUNTDOWN)
                    nextEvent();
            }
        }
    }

    // 更新计分板
    private void updateScoreboardContent() {
        List<String> list = HadesGame.scoreboardTemp;
        for (int i = 0; i < list.size(); i++) {
            ScoreboardHandler.INSTANCE.currentContent.set(i, formatMessage(list.get(i)));
        }
    }

    // 计分板占位符
    private String formatMessage(String s) {
        return MessageFormat.format(s, DATE_FORMAT.format(new Date()),
                nextEvent.getFormatEventName(currentCountdown),
                nextEvent.getFormatCountDown(currentCountdown),
                currentSurvivalPlayerNumber,
                (int) HadesGame.server.get().getOverworld().getWorldBorder().getSize(),
                ""
        );
    }

    // 清理游戏状态
    public void clearState(ServerPlayerEntity entity, GameMode mode) {
        entity.changeGameMode(mode);
        entity.getInventory().clear();
        entity.getInventory().selectedSlot = 0;
        entity.setExperienceLevel(0);
        entity.setExperiencePoints(0);
        entity.getHungerManager().setFoodLevel(20);
        entity.getEnderChestInventory().clear();
        entity.setHealth(20);
        entity.updateLastActionTime();
        ((IServerPlayer) entity).hg_setLivingFlag(4, false);
        ((IServerPlayer) entity).hg_setFlag(7, false);
        List<StatusEffectInstance> effects = new ArrayList<>(entity.getStatusEffects());
        for (StatusEffectInstance effect : effects) {
            entity.removeStatusEffect(effect.getEffectType());
        }

        entity.sendAbilitiesUpdate();
    }

    // 清理所有游戏状态
    protected void clearAllPlayerState(GameMode mode) {
        allPlayerHandler(p -> clearState(p, mode));
    }

    // 发送消息
    public void sendAllMessage(Text text) {
        allPlayerHandler(playerEntity -> playerEntity.sendMessage(text, false));
    }

    // 播放声音
    public void playSound(ServerPlayerEntity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        entity.networkHandler.sendPacket(new PlaySoundIdS2CPacket(sound.getId(), category, entity.getPos(), volume, pitch, entity.getWorld().getRandom().nextLong()));
    }

    // 传送
    public void teleport(ServerPlayerEntity playerEntity, Location location) {
        playerEntity.teleport(location.world,
                location.pos.x, location.pos.y, location.pos.z, location.yaw, location.pitch);
    }

    // 发送标题
    public void sendTitle(ServerPlayerEntity playerEntity, Text title, Text subtitle, int fadeIn, int fadeOut, int delay) {

        TitleFadeS2CPacket time = new TitleFadeS2CPacket(fadeIn, delay, fadeOut);

        TitleS2CPacket titlePacket = new TitleS2CPacket(title);
        SubtitleS2CPacket subtitlePacket = new SubtitleS2CPacket(subtitle);

        playerEntity.networkHandler.sendPacket(time);
        playerEntity.networkHandler.sendPacket(titlePacket);
        playerEntity.networkHandler.sendPacket(subtitlePacket);

    }

    // 操作所有玩家
    public final void allPlayerHandler(Consumer<ServerPlayerEntity> consumer) {
        List<ServerPlayerEntity> list = getAllPlayer();
        for (ServerPlayerEntity serverPlayerEntity : list) {
            runPrintException(serverPlayerEntity, consumer);
        }
    }

    // 操作生存玩家
    public final void survivalPlayerHandler(Consumer<ServerPlayerEntity> consumer) {
        List<ServerPlayerEntity> list = getSurvivalPlayers();
        for (ServerPlayerEntity serverPlayerEntity : list) {
            runPrintException(serverPlayerEntity, consumer);
        }
    }

    // 强制终止游戏
    public void forceEndGame() {
        GameCore.INSTANCE.currentState = GameState.WAITING;
        setEvent(new GameWaitingEvent());
        allPlayerHandler(playerEntity -> sendTitle(playerEntity, Text.literal("\u00a7cFBI WARNING"), Text.literal("\u00a7e游戏已被强制终止"), 0, 20, 20));

        for (WeightRandomArrayList.Entry<AbstractEvent> entry : eventList.resource) {
            entry.value.gameEnd();
        }
    }

    // 结束游戏
    public void endGame() {
        setEvent(new GameEndingEvent());
        currentState = GameState.ENDING;
        ServerPlayerEntity target = getSurvivalPlayers().stream().findFirst().orElse(null);

        allPlayerHandler(p -> {
            p.sendMessage(Text.literal("\u00a7c\u00a7l游戏结束"), false);
            p.sendMessage(Text.literal("\u00a7a赢家： \u00a7c" + (target == null ? "无" : target.getName().getString())), false);
            sendTitle(p, Text.literal("\u00a7c游戏结束"), Text.literal("\u00a7a赢家： \u00a7c" + (target == null ? "无" : target.getName().getString())), 0, 20, 20);
            GameCore.INSTANCE.playSound(p, SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.PLAYERS, 10, 1);
            clearState(p, GameMode.SPECTATOR);
        });
        clearWorld();

        for (WeightRandomArrayList.Entry<AbstractEvent> entry : eventList.resource) {
            entry.value.gameEnd();
        }

    }

    // 下一事件
    public void nextEvent() {
        if ((nextEvent = nextEvent.getNextEvent()) == null)
            setEvent(eventList.randomGet());
        else setEvent(nextEvent);
    }

    // 更改下一事件
    public boolean forceChangeEvent(String eventId) {
        AbstractEvent event = eventList.stream().filter(e -> e.value.EVENT_ID.equals(eventId)).limit(1).map(e -> e.value).findFirst().orElse(null);
        if (event == null)
            return false;
        setEvent(event);
        return true;
    }

    // 获得所有生存玩家
    public final List<ServerPlayerEntity> getSurvivalPlayers() {
        return HadesGame.server.get().getPlayerManager().getPlayerList().stream()
                .filter(serverPlayerEntity -> serverPlayerEntity.interactionManager.getGameMode() != GameMode.SPECTATOR)
                .collect(Collectors.toList());
    }

    // 获得所有玩家
    public final List<ServerPlayerEntity> getAllPlayer() {
        return HadesGame.server.get().getPlayerManager().getPlayerList();
    }

    // 获得事件IDs
    protected List<String> getEventIds() {
        return eventList.stream().map(e -> e.value).map(e -> e.EVENT_ID).collect(Collectors.toList());
    }

    public <T> void runPrintException(T obj, Consumer<T> consumer) {
        try {
            consumer.accept(obj);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void reBuildSurvivalNumber() {
        currentSurvivalPlayerNumber = getSurvivalPlayers().size();
    }


    protected void init() {
        tick = HadesGameScheduleManager.runTaskTimer(GameCore.INSTANCE::tick, 1);

        // 游戏加入和退出
        PlayerJoinEvent.INSTANCE.register(player -> HadesGameScheduleManager.runTask(() -> {
            ServerWorld world = HadesGame.server.get().getOverworld();
            if (currentState == GameState.WAITING || currentState == GameState.STARTING) {
                clearState(player, GameMode.ADVENTURE);
                teleport(player, HadesGame.getLobbyLocation());
            } else {
                clearState(player, GameMode.SPECTATOR);
                teleport(player, new Location(new Vec3d(world.getWorldBorder().getCenterX(), 200, world.getWorldBorder().getCenterZ()), world, 0, 0));
            }
        }));

        LivingEntityChangeHealthEvent.INSTANCE.register((livingEntity, newHealth) -> {
            if (livingEntity instanceof ServerPlayerEntity) {
                ServerWorld world = HadesGame.server.get().getOverworld();
                if (currentState == GameState.GAMING) {
                    if (livingEntity.isDead()) {
                        ((ServerPlayerEntity) livingEntity).getInventory().dropAll();
                        clearState((ServerPlayerEntity) livingEntity, GameMode.SPECTATOR);
                        sendAllMessage(Text.literal("\u00a7e" + livingEntity.getDisplayName().getString() + " \u00a7c死了"));

                        if (livingEntity.getY() < -64) {
                            teleport((ServerPlayerEntity) livingEntity, new Location(new Vec3d(livingEntity.getX(), 200, livingEntity.getZ()), world, 0, 0));
                        }
                    }
                } else {
                    if (livingEntity.getHealth() != 20f) {
                        livingEntity.setHealth(20f);
                    }
                }
            }
        });

        // 这堆事件只处理计算生存人数
        PlayerJoinEvent.INSTANCE.register(playerEntity -> HadesGameScheduleManager.runTask(this::reBuildSurvivalNumber));
        PlayerQuitEvent.INSTANCE.register(playerEntity -> HadesGameScheduleManager.runTask(this::reBuildSurvivalNumber));
        LivingEntityChangeHealthEvent.INSTANCE.register((livingEntity, newHealth) -> HadesGameScheduleManager.runTask(this::reBuildSurvivalNumber));
    }
}
