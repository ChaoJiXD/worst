/*
 * Decompiled with CFR 0.136.
 */
package xyz.WorstClient.module.modules.combat;

import java.awt.Color;

import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Hitbox
extends Module {
    public static Numbers<Double> size = new Numbers<Double>("size", "size", 0.5, 0.5, 1.0, 0.05);

    public Hitbox() {
        super("Hitbox", new String[]{"ka", "aura", "killa"}, ModuleType.Combat);
        this.setColor(new Color(226, 54, 30).getRGB());
        this.addValues(size);
    }

    private void onUpdate(EventPreUpdate event) {
        this.setSuffix(size.getValue());
    }
}

