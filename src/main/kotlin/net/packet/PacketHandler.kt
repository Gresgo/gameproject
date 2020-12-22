package net.packet

import net.utils.AddressPort

interface PacketHandler {

    fun handlePacket(packet: Packet, source: AddressPort)

}