package net.packet

import client.engine.math.Vector3f
import java.io.DataInputStream
import java.io.DataOutputStream

class UpdateObjectPacket: Packet {
    var id = 0
    var position = Vector3f(0F, 0F, 0F)
    var rotation = Vector3f(0F, 0F, 0F)
    var exists = false

    override fun read(inData: DataInputStream) {
        id = inData.readInt()
        position = Vector3f(
            inData.readShort().toFloat(),
            inData.readShort().toFloat(),
            inData.readShort().toFloat()
        )
        rotation = Vector3f(
            inData.readShort().toFloat(),
            inData.readShort().toFloat(),
            inData.readShort().toFloat()
        )
        exists = inData.readBoolean()
    }

    override fun write(outData: DataOutputStream) {
        outData.writeInt(id)
        outData.writeShort(position.x.toInt())
        outData.writeShort(position.y.toInt())
        outData.writeShort(position.z.toInt())
        outData.writeShort(rotation.x.toInt())
        outData.writeShort(rotation.y.toInt())
        outData.writeShort(rotation.z.toInt())
        outData.writeBoolean(exists)
    }

}