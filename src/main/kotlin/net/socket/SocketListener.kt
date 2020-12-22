package net.socket

import net.packet.ClosePacket
import net.packet.PacketFactory
import net.packet.PacketHandler
import net.utils.AddressPort
import java.io.DataInputStream
import java.io.EOFException
import java.io.IOException
import java.net.Socket

class SocketListener(
    private val socket: Socket,
    private val packetFactory: PacketFactory,
    private val packetHandler: PacketHandler
): Runnable {
    @Volatile private var isRunning = true

    override fun run() {
        var dis: DataInputStream? = null
        try {
            dis = DataInputStream(socket.getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            isRunning = false
        }
        val source = AddressPort(socket.inetAddress, socket.port)
        while (isRunning) {
            try {
                val packet = packetFactory.readPacket(dis!!)
                packetHandler.handlePacket(packet, source)
            } catch (e: EOFException) {
                e.printStackTrace()
                isRunning = false
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        try {
            dis?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        packetHandler.handlePacket(ClosePacket(), source)
    }

    fun stop() {
        isRunning = false
    }

}