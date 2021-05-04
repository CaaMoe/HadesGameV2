package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScoreboardDisplayS2CPacket.class)
public abstract class MixinScoreboardDisplayS2CPacket implements IScoreboardDisplayS2CPacket {
    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }

    @Accessor
    protected abstract int getSlot();

    @Accessor
    protected abstract void setSlot(int slot);

    @Override
    public int hg_getSlot() {
        return getSlot();
    }

    @Override
    public void hg_setSlot(int slot) {
        setSlot(slot);
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
}
