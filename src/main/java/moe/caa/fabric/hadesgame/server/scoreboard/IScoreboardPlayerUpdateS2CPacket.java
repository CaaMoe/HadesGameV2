package moe.caa.fabric.hadesgame.server.scoreboard;

import net.minecraft.scoreboard.ServerScoreboard;

public interface IScoreboardPlayerUpdateS2CPacket extends IScoreboardS2CPacket {
    String hg_getPlayerName();

    void hg_setPlayerName(String playerName);

    String hg_getObjectiveName();

    void hg_setObjectiveName(String objectiveName);

    int hg_getScore();

    void hg_setScore(int score);

    ServerScoreboard.UpdateMode hg_getMode();

    void hg_setMode(ServerScoreboard.UpdateMode mode);

}
