package cn.backday.component;

import cn.backday.component.impl.FakeClientComponent;
import cn.backday.component.impl.KeybindComponent;
import com.darkmagician6.eventapi.EventManager;

import java.util.ArrayList;

public class ComponentManager extends ArrayList<Component> {
    public void init() {
        this.add(new KeybindComponent());
        this.add(new FakeClientComponent());
        this.registerToEventBus();
    }

    public void registerToEventBus() {
        for (Component component : this) {
            EventManager.register(component);
        }
    }
}
