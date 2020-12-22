package net.server

import server.Client
import net.packet.Packet

interface NetServer {

    fun start()

    fun sendPacket(packet: Packet, client: Client, isReliable: Boolean = false)

}