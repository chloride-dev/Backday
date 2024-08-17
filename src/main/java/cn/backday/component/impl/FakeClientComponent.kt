package cn.backday.component.impl

import cn.backday.Client
import cn.backday.component.Component
import cn.backday.event.impl.network.PacketEvent
import com.darkmagician6.eventapi.EventTarget
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.C17PacketCustomPayload

class FakeClientComponent : Component() {
    @EventTarget
    fun onPacket(packet: PacketEvent) {
        if (packet.packet is C17PacketCustomPayload) {
            val c17 = packet.packet as C17PacketCustomPayload

            if (c17.channelName == "MC|Brand") {
                c17.data = PacketBuffer(Unpooled.buffer()).writeString(
                    "lunarclient:${Client.clientCommit}"
                )
            }
        }
    }
}