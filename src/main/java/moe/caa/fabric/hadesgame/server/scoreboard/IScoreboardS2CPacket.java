package moe.caa.fabric.hadesgame.server.scoreboard;

public interface IScoreboardS2CPacket {

    default boolean hg_isHgGamePacket() {
        return false;
    }

    void hg_setHgGamePacket(boolean value);
}
