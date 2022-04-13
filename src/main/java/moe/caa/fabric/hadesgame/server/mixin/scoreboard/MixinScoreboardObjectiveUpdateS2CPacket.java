package moe.caa.fabric.hadesgame.server.mixin.scoreboard;

import moe.caa.fabric.hadesgame.server.scoreboard.IScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScoreboardObjectiveUpdateS2CPacket.class)
public abstract class MixinScoreboardObjectiveUpdateS2CPacket implements IScoreboardObjectiveUpdateS2CPacket {
    @Mutable
    @Shadow
    @Final
    private String name;
    @Mutable
    @Shadow
    @Final
    private Text displayName;
    @Mutable
    @Shadow
    @Final
    private ScoreboardCriterion.RenderType type;
    @Mutable
    @Shadow
    @Final
    private int mode;
    private boolean hg_gamePacket = false;

    @Override
    public boolean hg_isHgGamePacket() {
        return hg_gamePacket;
    }

    public void hg_setHgGamePacket(boolean hg_gamePacket) {
        this.hg_gamePacket = hg_gamePacket;
    }

    @Override
    public String hg_getName() {
        return name;
    }

    @Override
    public void hg_setName(String name) {
        this.name = name;
    }

    @Override
    public Text hg_getDisplayName() {
        return displayName;
    }

    @Override
    public void hg_setDisplayName(Text text) {
        this.displayName = text;
    }


    @Override
    public ScoreboardCriterion.RenderType hg_getType() {
        return type;
    }

    @Override
    public void hg_setType(ScoreboardCriterion.RenderType type) {
        this.type = type;
    }

    @Override
    public int hg_getMode() {
        return mode;
    }

    @Override
    public void hg_setMode(int mode) {
        this.mode = mode;
    }
}
