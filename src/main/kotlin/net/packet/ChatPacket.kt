package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream

class ChatPacket: Packet {
    var text = ""

    override fun read(inData: DataInputStream) {
        text = inData.readUTF()
    }

    override fun write(outData: DataOutputStream) {
        outData.writeUTF(text)
    }

}