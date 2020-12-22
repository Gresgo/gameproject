package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream

class LoginPacket: Packet {
    var name = ""

    override fun read(inData: DataInputStream) {
        name = inData.readUTF()
    }

    override fun write(outData: DataOutputStream) {
        outData.writeUTF(name)
    }

}