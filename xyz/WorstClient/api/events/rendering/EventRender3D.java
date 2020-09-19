/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.api.events.rendering;

import shadersmod.client.Shaders;
import xyz.WorstClient.api.Event;

public class EventRender3D
extends Event {
    public static float ticks;
    private boolean isUsingShaders;

    public EventRender3D() {
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }

    public EventRender3D(float ticks) {
        this.ticks = ticks;
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}

