/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.MoveUtils;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.render.ColorUtils;

public class EnchantEffect

extends Module {
    public static Numbers<Double> r = new Numbers<Double>("Red", "Red", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> g = new Numbers<Double>("Green", "Green", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> b = new Numbers<Double>("Blue", "Blue", 255.0, 0.0, 255.0, 1.0);
    public EnchantEffect() {
        super("EnchantEffect", new String[]{"EnchantEffect"}, ModuleType.Render);

        this.addValues(r,g,b);
    }
    @EventHandler
    private void onUpdate(EventPreUpdate e)  {
    	r.setValue((double) ColorUtils.getRainbow().getRed());
    	g.setValue((double) ColorUtils.getRainbow().getGreen());
    	b.setValue((double) ColorUtils.getRainbow().getBlue());
        //this.setEnabled(false);
	}
}

