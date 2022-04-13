package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScoreboardDisplayS2CPacket.class)
public abstract class MixinScoreboardDisplayS2CPacket implements IScoreboardDisplayS2CPacket {
    @Mutable
    @Shadow
    @Final
    private int slot;
    @Mutable
    @Shadow
    @Final
    private String name;
    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }

    @Override
    public int hg_getSlot() {
        return slot;
    }

    @Override
    public void hg_setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public String hg_getName() {
        return name;
    }

    @Override
    public void hg_setName(String name) {
        this.name = name;
    }
}
