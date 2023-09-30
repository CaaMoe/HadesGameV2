package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardPlayerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

@Mixin(ScoreboardPlayerUpdateS2CPacket.class)
public abstract class MixinScoreboardPlayerUpdateS2CPacket implements IScoreboardPlayerUpdateS2CPacket {

    @Mutable
    @Shadow
    @Final
    private String playerName;
    @Mutable
    @Shadow
    @Final
    @Nullable
    private String objectiveName;
    @Mutable
    @Shadow
    @Final
    private int score;
    @Mutable
    @Shadow
    @Final
    private ServerScoreboard.UpdateMode updateMode;
    @Unique
    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }

    @Override
    public String hg_getPlayerName() {
        return playerName;
    }

    @Override
    public void hg_setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String hg_getObjectiveName() {
        return objectiveName;
    }

    @Override
    public void hg_setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    @Override
    public int hg_getScore() {
        return score;
    }

    @Override
    public void hg_setScore(int score) {
        this.score = score;
    }

    @Override
    public ServerScoreboard.UpdateMode hg_getMode() {
        return updateMode;
    }

    @Override
    public void hg_setMode(ServerScoreboard.UpdateMode mode) {
        this.updateMode = mode;
    }
}
