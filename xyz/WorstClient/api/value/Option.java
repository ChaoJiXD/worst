
package xyz.WorstClient.api.value;

import xyz.WorstClient.api.value.Value;

public class Option<V>
extends Value<V> {
    public Option(String displayName, String name, V enabled) {
        super(displayName, name);
        this.setValue(enabled);
    }
}

