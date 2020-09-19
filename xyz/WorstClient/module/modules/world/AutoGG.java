
package xyz.WorstClient.module.modules.world;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventChat;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.Speed;

public class AutoGG
extends Module {
    public AutoGG() {
        super("AutoGG", new String[]{"AutoGG"}, ModuleType.World);
    }
@EventHandler
private void onChat(EventChat e)
{
  if (e.getMessage().contains("Winner"))
  {
    e.setCancelled(true);
    Minecraft.thePlayer.sendChatMessage("GG Buy Worst at chaoji.maikama.cn" );
    }
}
  }


