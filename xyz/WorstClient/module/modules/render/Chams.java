/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package xyz.WorstClient.module.modules.render;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventPostRenderPlayer;
import xyz.WorstClient.api.events.rendering.EventPreRenderPlayer;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Chams
extends Module {
    public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])ChamsMode.values(), (Enum)ChamsMode.Textured);

    public Chams() {
        super("Chams", new String[]{"seethru", "cham"}, ModuleType.Render);
        this.addValues(this.mode);
        this.setColor(new Color(159, 190, 192).getRGB());
    }

    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e) {
        GL11.glEnable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
    }

    @EventHandler
    private void postRenderPlayer(EventPostRenderPlayer e) {
        GL11.glDisable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);
    }

    public static enum ChamsMode {
        Textured,
        Normal;
    }

}

