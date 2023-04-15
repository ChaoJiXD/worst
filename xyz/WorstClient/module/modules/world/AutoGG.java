
package xyz.WorstClient.module.modules.world;
import net.minecraft.client.Minecraft;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventChat;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

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
    Minecraft.thePlayer.sendChatMessage("[Worst]GG 我叫AoneHax李铭舒正在给你妈洗B呢 B站关注AoneHax" );
    }
}
  }


