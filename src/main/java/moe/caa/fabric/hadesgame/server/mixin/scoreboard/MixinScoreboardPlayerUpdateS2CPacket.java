package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardPlayerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScoreboardPlayerUpdateS2CPacket.class)
public abstract class MixinScoreboardPlayerUpdateS2CPacket implements IScoreboardPlayerUpdateS2CPacket {

    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }

    @Accessor
    protected abstract String getPlayerName();

    @Accessor
    protected abstract void setPlayerName(String playerName);

    @Override
    public String hg_getPlayerName() {
        return getPlayerName();
    }

    @Override
    public void hg_setPlayerName(String playerName) {
        setPlayerName(playerName);
    }

    @Accessor
    protected abstract String getObjectiveName();

    @Accessor
    protected abstract void setObjectiveName(String objectiveName);

    @Override
    public String hg_getObjectiveName() {
        return getObjectiveName();
    }

    @Override
    public void hg_setObjectiveName(String objectiveName) {
        setObjectiveName(objectiveName);
    }

    @Accessor
    protected abstract int getScore();

    @Accessor
    protected abstract void setScore(int score);

    @Override
    public int hg_getScore() {
        return getScore();
    }

    @Override
    public void hg_setScore(int score) {
        setScore(score);
    }

    @Accessor
    protected abstract ServerScoreboard.UpdateMode getMode();

    @Accessor
    protected abstract void setMode(ServerScoreboard.UpdateMode mode);

    @Override
    public ServerScoreboard.UpdateMode hg_getMode() {
        return getMode();
    }

    @Override
    public void hg_setMode(ServerScoreboard.UpdateMode mode) {
        setMode(mode);
    }
}
