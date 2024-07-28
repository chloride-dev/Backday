package cn.backday.component.impl.rotationcomponent;

public enum MovementFix {
    OFF("Off"),
    NORMAL("Rise"),
    TRADITIONAL("Traditional"),
    BACKWARDS_SPRINT("Backwards Sprint");

    final String name;

    MovementFix(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
