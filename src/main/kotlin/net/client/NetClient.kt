package net.client

import net.packet.Packet

interface NetClient {

    fun connect()

    fun sendPacket(packet: Packet, isReliable: Boolean = false)

}