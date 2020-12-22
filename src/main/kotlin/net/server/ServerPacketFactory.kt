package net.server

import net.packet.BasePacketFactory
import net.packet.Packet

class ServerPacketFactory: BasePacketFactory() {
    private var nextPacketId = 1

    override fun getPacketId(packet: Packet): Int {
        val name = packet.javaClass.name
        var id = packetNameMapper.getId(name)
        if (id < 0) {
            synchronized(packetNameMapper) {
                id = packetNameMapper.getId(name)
                if (id < 0) {
                    id = nextPacketId++
                    packetNameMapper.addName(name, id)
                }
            }
        }
        return id
    }

    override fun getPacketName(id: Int): String? =
        packetNameMapper.getName(id)

}