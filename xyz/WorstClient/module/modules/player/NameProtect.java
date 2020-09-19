
package xyz.WorstClient.module.modules.player;
import net.minecraft.client.Minecraft;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.fontRenderer.CFont.CFontRenderer;

public class NameProtect
extends Module {

    public NameProtect() {
        super("NameProtect", new String[]{"NameProtect"}, ModuleType.Player);
    }
}

