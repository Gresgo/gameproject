package net.server

import server.Client
import net.packet.Packet

interface IServerHandler {

    fun handlePacket(packet: Packet, source: Client)

    fun clientConnect(client: Client)

    fun clientDisconnect(client: Client)

}