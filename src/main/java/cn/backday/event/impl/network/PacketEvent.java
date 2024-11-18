package cn.backday.event.impl.network;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class PacketEvent extends EventCancellable {
    private Packet<?> packet;
    private final PacketType type;

    public enum PacketType {
        SEND, RECEIVE
    }

    public PacketEvent(Packet<?> packet, PacketType type) {
        this.packet = packet;
        this.type = type;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public PacketType getType() {
        return type;
    }
}
