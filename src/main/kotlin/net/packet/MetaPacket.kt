package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class MetaPacket: Packet {
    val packets = Vector<Packet>()

    override fun read(inData: DataInputStream) {
        throw IOException("Do not call read on MetaPacket this")
    }

    override fun write(outData: DataOutputStream) {
        throw IOException("Do not call write on MetaPacket this")
    }

}