package moe.caa.fabric.hadesgame.server.schedule;

public abstract class AbstractTick {
    protected final int PERIOD;
    private int tickNumber = 0;
    private boolean cancel = false;

    public AbstractTick(int period) {
        PERIOD = period;
    }

    public AbstractTick() {
        PERIOD = 0;
    }

    protected final void tick0() {
        if (tickNumber == 0 || tickNumber % PERIOD == 0) {
            tick();
        }
        tickNumber++;
    }

    public int getTickNumber() {
        return tickNumber;
    }

    protected abstract void tick();

    public void cancel() {
        this.cancel = true;
    }

    public boolean isCancel() {
        return cancel;
    }
}
