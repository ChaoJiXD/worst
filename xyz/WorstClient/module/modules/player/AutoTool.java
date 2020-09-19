package xyz.WorstClient.module.modules.player;
import net.minecraft.util.BlockPos;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.BlockUtils;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", new String[] {"autoyool"},ModuleType.Player);
    }
	public Class type() {
        return EventPacketSend.class;
    }

	@EventHandler
	    public void onEvent(EventPreUpdate event) {
	        if (!mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
	            return;
	        }
	        if (mc.objectMouseOver == null) {
	            return;
	        }
	        BlockPos pos = mc.objectMouseOver.getBlockPos();
	        if (pos == null) {
	            return;
	        }
	        BlockUtils.updateTool(pos);
	    }
	}