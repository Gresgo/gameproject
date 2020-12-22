package net.client

import net.packet.Packet

interface IClientHandler {

    fun handlePacket(packet: Packet)

    fun serverDisconnect()

}