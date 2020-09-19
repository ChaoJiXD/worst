/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.world;

import java.awt.Color;

import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[]{"eagle", "parkour"}, ModuleType.World);
        this.setColor(new Color(198, 253, 191).getRGB());
    }
}

