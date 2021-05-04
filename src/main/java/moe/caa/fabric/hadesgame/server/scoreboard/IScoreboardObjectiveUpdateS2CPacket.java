package moe.caa.fabric.hadesgame.server.scoreboard;

import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.text.Text;

public interface IScoreboardObjectiveUpdateS2CPacket extends IScoreboardS2CPacket {

    String hg_getName();

    void hg_setName(String name);

    Text hg_getDisplayName();

    void hg_setDisplayName(Text text);

    ScoreboardCriterion.RenderType hg_getType();

    void hg_setType(ScoreboardCriterion.RenderType type);

    int hg_getMode();

    void hg_setMode(int mode);
}
