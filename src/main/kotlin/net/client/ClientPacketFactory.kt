package net.client

import net.utils.AddressPort
import net.packet.BasePacketFactory
import net.utils.Command
import net.packet.Packet
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class ClientPacketFactory(private val server: AddressPort): BasePacketFactory() {

    override fun getPacketId(packet: Packet): Int {
        val name = packet.javaClass.name
        var id = packetNameMapper.getId(name)
        if (id < 0) {
            id = askPacketId(name)
            if (id >= 0)
                packetNameMapper.addName(name, id)
        }
        return id
    }

    override fun getPacketName(id: Int): String? {
        var name = packetNameMapper.getName(id)
        if (name == null) {
            name = askPacketName(id)
            name?.let { packetNameMapper.addName(it, id) }
        }
        return name
    }

    private fun askPacketName(id: Int): String? {
        try {
            val socket = Socket(server.address, server.port)
            val outData = DataOutputStream(socket.getOutputStream())
            val inData = DataInputStream(DataInputStream(socket.getInputStream()))
            outData.writeByte(Command.ASK_PACKET_NAME)
            outData.writeInt(id)
            outData.flush()
            val name = inData.readUTF()
            outData.close()
            inData.close()
            socket.close()
            return name
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun askPacketId(name: String): Int {
        try {
            val socket = Socket(server.address, server.port)
            val outData = DataOutputStream(socket.getOutputStream())
            val inData = DataInputStream(DataInputStream(socket.getInputStream()))
            outData.writeByte(Command.ASK_PACKET_ID)
            outData.writeUTF(name)
            outData.flush()
            val id = inData.readInt()
            outData.close()
            inData.close()
            socket.close()
            return id
        } catch (e: IOException) {
            return -1
        }
    }

}