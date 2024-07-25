package cn.backday.api.event.impl.key;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class KeyEvent extends EventCancellable {
    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
