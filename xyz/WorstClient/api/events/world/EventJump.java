package xyz.WorstClient.api.events.world;

import xyz.WorstClient.api.Event;

public class EventJump extends Event {

    private float motion;

    public EventJump(float motion) {

        this.motion=motion;

    }

    public float getMotion() {
        return motion;
    }

}
