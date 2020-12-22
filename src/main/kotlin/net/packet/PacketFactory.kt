package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream

interface PacketFactory {

    fun readPacket(inData: DataInputStream): Packet

    fun writePacket(packet: Packet, outData: DataOutputStream)

}