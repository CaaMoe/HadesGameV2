package moe.caa.fabric.hadesgame.server.scoreboard;

import net.minecraft.scoreboard.ScoreboardDisplaySlot;

public interface IScoreboardDisplayS2CPacket extends IScoreboardS2CPacket {

    int hg_getSlot();

    void hg_setSlot(ScoreboardDisplaySlot slot);

    String hg_getName();

    void hg_setName(String name);
}
