package moe.caa.fabric.hadesgame.server;

public interface IItemEntityHandler {

    boolean hg_isModSpawn();

    void hg_setModSpawn(boolean b);

    int hg_getPickupDelay();
}
