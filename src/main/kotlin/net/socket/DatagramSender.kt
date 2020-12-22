package net.socket

import net.packet.Packet
import net.packet.PacketFactory
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class DatagramSender(private val socket: DatagramSocket,
                     private val packetFactory: PacketFactory
): ByteArrayOutputStream(BUFFER_SIZE) {

    companion object {
        const val BUFFER_SIZE = 32768
    }

    private val dgPacket = DatagramPacket(buf, buf.size)
    private val out = DataOutputStream(this)

    fun sendPacket(packet: Packet, address: InetAddress, port: Int) {
        out.flush()
        reset()
        packetFactory.writePacket(packet, out)
        dgPacket.apply {
            this.data = buf
            this.length = size()
            this.address = address
            this.port = port
        }
        socket.send(dgPacket)
    }
}