package net.socket

import net.packet.PacketFactory
import net.packet.PacketHandler
import net.utils.AddressPort
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket

class DatagramListener(
    private val socket: DatagramSocket,
    private val packetFactory: PacketFactory,
    private val packetHandler: PacketHandler
): Runnable {

    companion object {
        const val BUFFER_SIZE = 32768//8192
    }
    private val buffer = ByteArray(BUFFER_SIZE)
    private val dgPacket = DatagramPacket(buffer, buffer.size)
    private var isRunning = true

    override fun run() {
        println("Datagram listener started in port: ${socket.localPort}")
        while (isRunning) {
            try {
                socket.soTimeout = 1000
                dgPacket.data = buffer
                dgPacket.length = buffer.size
                socket.receive(dgPacket)
                val bais = ByteArrayInputStream(dgPacket.data, 0, dgPacket.length)
                val inData = DataInputStream(bais)
                val packet = packetFactory.readPacket(inData)
                val source = AddressPort(dgPacket.address, dgPacket.port)
                packetHandler.handlePacket(packet, source)
                inData.close()
                bais.close()
            } catch (e: EOFException) {
                e.printStackTrace()
                isRunning = false
            } catch (e: InterruptedIOException) {
                //e.printStackTrace()
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
        println("Datagram listener ended: ${Thread.currentThread().name}")
    }

    fun stop() {
        isRunning = false
    }

}