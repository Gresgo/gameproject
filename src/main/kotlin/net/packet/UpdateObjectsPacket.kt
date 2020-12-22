package net.packet

import client.engine.math.Vector3f
import server.ServerObject
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class UpdateObjectsPacket: Packet {
    var serverObjects = Vector<ServerObject>()

    override fun read(inData: DataInputStream) {
        val n = inData.readInt()
        serverObjects = Vector(n)
        repeat(n) {
            serverObjects.add(ServerObject().apply {
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
            })
        }
    }

    override fun write(outData: DataOutputStream) {
        outData.writeInt(serverObjects.size)
        serverObjects.forEach {
            outData.writeInt(it.id)
            outData.writeShort(it.position.x.toInt())
            outData.writeShort(it.position.y.toInt())
            outData.writeShort(it.position.z.toInt())
            outData.writeShort(it.rotation.x.toInt())
            outData.writeShort(it.rotation.y.toInt())
            outData.writeShort(it.rotation.z.toInt())
        }
        //println(outData.size())
    }

}