package moe.caa.fabric.hadesgame.server.scoreboard;

public interface IScoreboardDisplayS2CPacket extends IScoreboardS2CPacket {

    int hg_getSlot();

    void hg_setSlot(int slot);

    String hg_getName();

    void hg_setName(String name);
}
