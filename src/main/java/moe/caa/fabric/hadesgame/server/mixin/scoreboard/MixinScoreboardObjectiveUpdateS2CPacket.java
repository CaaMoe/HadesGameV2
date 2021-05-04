package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScoreboardObjectiveUpdateS2CPacket.class)
public abstract class MixinScoreboardObjectiveUpdateS2CPacket implements IScoreboardObjectiveUpdateS2CPacket {
    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }


    @Accessor
    protected abstract String getName();

    @Accessor
    protected abstract void setName(String name);

    @Override
    public String hg_getName() {
        return getName();
    }

    @Override
    public void hg_setName(String name) {
        setName(name);
    }

    @Accessor
    protected abstract Text getDisplayName();

    @Accessor
    protected abstract void setDisplayName(Text text);

    @Override
    public Text hg_getDisplayName() {
        return getDisplayName();
    }

    @Override
    public void hg_setDisplayName(Text text) {
        setDisplayName(text);
    }

    @Accessor
    protected abstract ScoreboardCriterion.RenderType getType();

    @Accessor
    protected abstract void setType(ScoreboardCriterion.RenderType type);

    @Override
    public ScoreboardCriterion.RenderType hg_getType() {
        return getType();
    }

    @Override
    public void hg_setType(ScoreboardCriterion.RenderType type) {
        setType(type);
    }

    @Accessor
    protected abstract int getMode();

    @Accessor
    protected abstract void setMode(int mode);

    @Override
    public int hg_getMode() {
        return getMode();
    }

    @Override
    public void hg_setMode(int mode) {
        setMode(mode);
    }
}
