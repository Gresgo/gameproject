package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream

class LoginSuccessPacket: Packet {
    var id = -1

    override fun read(inData: DataInputStream) {
        id = inData.readShort().toInt()
    }

    override fun write(outData: DataOutputStream) {
        outData.writeShort(id)
    }

}