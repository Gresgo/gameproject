package net.packet

import client.engine.math.Vector3f
import server.Player
import java.io.DataInputStream
import java.io.DataOutputStream
import java.util.*

class UpdatePlayersPacket: Packet {
    var players = Vector<Player>()

    override fun read(inData: DataInputStream) {
        val n = inData.readInt()
        players = Vector(n)
        repeat(n) {
            players.add(Player().apply {
                id = inData.readInt()
                position = Vector3f(
                    inData.readFloat(),
                    inData.readFloat(),
                    inData.readFloat()
                )
                rotation = Vector3f(
                    inData.readFloat(),
                    inData.readFloat(),
                    inData.readFloat()
                )
            })
        }
    }

    override fun write(outData: DataOutputStream) {
        outData.writeInt(players.size)
        players.forEach {
            outData.writeInt(it.id)
            outData.writeFloat(it.position.x)
            outData.writeFloat(it.position.y)
            outData.writeFloat(it.position.z)
            outData.writeFloat(it.rotation.x)
            outData.writeFloat(it.rotation.y)
            outData.writeFloat(it.rotation.z)
        }

    }

}