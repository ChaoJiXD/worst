package xyz.WorstClient.utils.Xray;

import net.minecraft.util.BlockPos;
import xyz.WorstClient.api.Event;

public class EventSetOpaqueCube extends Event {

    private final BlockPos pos;

    public EventSetOpaqueCube( BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}