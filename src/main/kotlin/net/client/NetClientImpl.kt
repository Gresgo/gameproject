package net.client

import net.packet.*
import net.socket.DatagramListener
import net.socket.DatagramSender
import net.socket.SocketListener
import net.utils.AddressPort
import net.utils.Command
import java.io.DataOutputStream
import java.io.IOException
import java.net.DatagramSocket
import java.net.Socket

class NetClientImpl(
    host: String, port: Int,
    private val clientHandler: IClientHandler
): NetClient, PacketHandler {
    private var isConnected = false
    private val socket: Socket
    private val datagramSocket: DatagramSocket
    private val out: DataOutputStream
    private val packetFactory: PacketFactory
    private val datagramSender: DatagramSender
    private lateinit var datagramListener: DatagramListener
    private lateinit var dglThread: Thread

    init {
        try {
            socket = Socket(host, port)
            datagramSocket = DatagramSocket(socket.localPort)
            out = DataOutputStream(socket.getOutputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        packetFactory =
            ClientPacketFactory(AddressPort(socket.inetAddress, socket.port))
        datagramSender = DatagramSender(datagramSocket, packetFactory)

        println("Client connected to server: ${socket.inetAddress}:${socket.port}")
    }

    override fun connect() {
        datagramListener = DatagramListener(datagramSocket, packetFactory, this)
        dglThread = Thread(datagramListener, "server udp")
        dglThread.isDaemon = true
        dglThread.start()

        val listener = SocketListener(socket, packetFactory, this)
        val t = Thread(listener, "server tcp")
        t.isDaemon = true
        t.start()
        try {
            out.writeByte(Command.LOGIN)
            out.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        isConnected = true
    }

    override fun handlePacket(packet: Packet, source: AddressPort) {
        if (packet is ClosePacket) {
            datagramListener.stop()
            datagramSocket.close()
            try {
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            isConnected = false
            clientHandler.serverDisconnect()
        } else if (packet is MetaPacket) {
            packet.packets.forEach {
                clientHandler.handlePacket(it)
            }
        } else clientHandler.handlePacket(packet)
    }

    override fun sendPacket(packet: Packet, isReliable: Boolean) {
        try {
            if (isReliable) {
                packetFactory.writePacket(packet, out)
            } else {
                datagramSender.sendPacket(packet, socket.inetAddress, socket.port)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

}