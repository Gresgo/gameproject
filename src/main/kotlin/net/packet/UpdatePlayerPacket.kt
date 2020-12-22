package net.packet

import client.engine.math.Vector3f
import server.Player
import java.io.DataInputStream
import java.io.DataOutputStream

class UpdatePlayerPacket : Packet {
    var player = Player()

    override fun read(inData: DataInputStream) {
        player = Player().apply {
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
        }
    }

    override fun write(outData: DataOutputStream) {
        outData.writeInt(player.id)
        outData.writeFloat(player.position.x)
        outData.writeFloat(player.position.y)
        outData.writeFloat(player.position.z)
        outData.writeFloat(player.rotation.x)
        outData.writeFloat(player.rotation.y)
        outData.writeFloat(player.rotation.z)
    }

}