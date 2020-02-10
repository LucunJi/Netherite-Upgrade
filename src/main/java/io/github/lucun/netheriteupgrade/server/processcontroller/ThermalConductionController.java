package io.github.lucun.netheriteupgrade.server.processcontroller;


public class ThermalConductionController {
    public static final ThermalConductionController INSTANCE = new ThermalConductionController();

    private float damage = 0;
    private boolean alive = false;

    public void begin() {
        this.alive = true;
        this.damage = 0;
    }

    public void end() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
