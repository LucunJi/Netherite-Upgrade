package io.github.lucun.netheriteupgrade.api;

/**
 * One feature can be separated into many parts in different Mixins.
 * This Controller is used to ensure the flow goes well.
 */
public abstract class FlowControllerAbstract<T> {
    protected boolean alive = false;

    public void begin() {
            this.alive = true;
    }

    public void end() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void feed(T item) {
        if (!this.alive) {
            throw new RuntimeException("FlowController is fed when it is not alive!");
        } else {
            this.onFeed(item);
        }
    }

    protected abstract void onFeed(T item);
}
