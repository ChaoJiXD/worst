package xyz.WorstClient.api.events.misc;

import xyz.WorstClient.api.Event;
import net.minecraft.entity.Entity;

public class EventLivingUpdate
extends Event {
    private Entity entity;
    public EventLivingUpdate(Entity entity) {
        super();
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}

