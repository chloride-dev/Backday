package cn.backday.utils.packet

import cn.backday.utils.MinecraftInterface.mc
import net.minecraft.network.Packet

object PacketUtils {
    @JvmStatic
    fun sendPacket(packet: Packet<*>, triggerEvent: Boolean = true) {
        if (triggerEvent) {
            mc.netHandler?.addToSendQueue(packet)
            return
        }

        val netManager = mc.netHandler?.networkManager ?: return
        netManager.sendPacketNoEvent(packet)
    }

    @JvmStatic
    fun sendPackets(vararg packets: Packet<*>, triggerEvents: Boolean = true) =
        packets.forEach { sendPacket(it, triggerEvents) }
}