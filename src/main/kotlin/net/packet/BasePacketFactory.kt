package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.system.exitProcess

abstract class BasePacketFactory: PacketFactory {
    val packetNameMapper = PacketNameMapper()

    override fun readPacket(inData: DataInputStream): Packet {
        val id: Int
        val name: String?
        try {
            id = inData.readShort().toInt()
            name = getPacketName(id)
            name ?: throw IOException("unknown packet id: $id")
            val cl = Class.forName(name)
            val packet = cl.newInstance() as Packet
            if (packet is MetaPacket) {
                val n = inData.readInt()
                for (i in 0 until n) {
                    val p = readPacket(inData)
                    packet.packets.add(p)
                }
            } else {
                packet.read(inData)
            }
            return packet
        } catch (e: IOException) {
            //e.printStackTrace()
            //throw e
            //return ClosePacket()
//            exitProcess(10)
            return ChatPacket()
        }
    }

    override fun writePacket(packet: Packet, outData: DataOutputStream) {
        if (packet is MetaPacket) {
            outData.writeShort(getPacketId(packet))
            outData.writeInt(packet.packets.size)
            packet.packets.forEach {
                writePacket(it, outData)
            }
        } else {
            outData.writeShort(getPacketId(packet))
            packet.write(outData)
        }
    }

    abstract fun getPacketName(id: Int): String?

    abstract fun getPacketId(packet: Packet): Int

}