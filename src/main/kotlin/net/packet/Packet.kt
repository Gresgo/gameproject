package net.packet

import java.io.DataInputStream
import java.io.DataOutputStream

interface Packet {

    fun write(outData: DataOutputStream)

    fun read(inData: DataInputStream)
}