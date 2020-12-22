package net.server

import net.packet.*
import net.socket.DatagramListener
import net.socket.DatagramSender
import net.socket.SocketListener
import net.utils.AddressPort
import net.utils.Command
import server.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.DatagramSocket
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class NetServerImpl(port: Int, private val serverHandler: IServerHandler): NetServer, Runnable,
    PacketHandler {

    private var nextClientId = 0
    private val addressToClient = Hashtable<AddressPort, ServerClient>()
    private val serverSocket: ServerSocket
    private val datagramSocket: DatagramSocket
    private val serverPacketFactory = ServerPacketFactory()
    private val datagramSender: DatagramSender
    @Volatile private var isRunning = false

    init {
        try {
            serverSocket = ServerSocket(port)
            datagramSocket = DatagramSocket(port)
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        nextClientId = 1
        datagramSender = DatagramSender(datagramSocket, serverPacketFactory)
    }

    override fun start() {
        val listener = DatagramListener(datagramSocket, serverPacketFactory, this)
        var t = Thread(listener, "client udp")
        t.isDaemon = true
        t.start()

        t = Thread(this, "listen")
        t.isDaemon = true
        isRunning = true
        t.start()
    }

    override fun run() {
        while (isRunning) {
            try {
                val socket = serverSocket.accept()
                val cmd = socket.getInputStream().read()
                when (cmd) {
                    Command.LOGIN -> login(socket)
                    Command.ASK_PACKET_NAME -> askPacketName(socket)
                    Command.ASK_PACKET_ID -> askPacketId(socket)
                    else -> println("Unknown server command: $cmd")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun login(socket: Socket) {
        val client = ServerClient(getNextClientId(), socket)
        val address = AddressPort(socket.inetAddress, socket.port)
        addressToClient[address] = client
        val listener = SocketListener(socket, serverPacketFactory, this)
        val t = Thread(listener, "client ${client.id}")
        println("new client connected: $client")
        serverHandler.clientConnect(client)
        t.start()
    }

    private fun askPacketName(socket: Socket) {
        val inData = DataInputStream(socket.getInputStream())
        val outData = DataOutputStream(socket.getOutputStream())
        val id = inData.readInt()
        val name = serverPacketFactory.packetNameMapper.getName(id)
        if (name != null) {
            outData.writeUTF(name)
        } else {
            println("Unknown packet: $name")
            outData.writeUTF("")
        }
        inData.close()
        outData.close()
        socket.close()
    }

    private fun askPacketId(socket: Socket) {
        val inData = DataInputStream(socket.getInputStream())
        val outData = DataOutputStream(socket.getOutputStream())
        val name = inData.readUTF()
        var id = serverPacketFactory.packetNameMapper.getId(name)
        if (id < 0) {
            try {
                val cl = Class.forName(name)
                val packet = cl.newInstance() as Packet
                id = serverPacketFactory.getPacketId(packet)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (id >= 0) {
            outData.writeInt(id)
        } else {
            println("Unknown packet name: $name")
            outData.writeInt(-1)
        }
        inData.close()
        outData.close()
        socket.close()
    }

    override fun handlePacket(packet: Packet, source: AddressPort) {
        val client = addressToClient[source]
        client ?: return println("Unknown packet address: $source")
        if (packet is ClosePacket) {
            client.disconnect()
            serverHandler.clientDisconnect(client)
        } else if (packet is MetaPacket) {
            packet.packets.forEach {
                serverHandler.handlePacket(packet, client)
            }
        } else serverHandler.handlePacket(packet, client)
    }

    private fun getNextClientId(): Int = nextClientId++

    override fun sendPacket(packet: Packet, client: Client, isReliable: Boolean) {
        val sc = client as ServerClient
        try {
            if (isReliable) {
                serverPacketFactory.writePacket(packet, sc.outData!!)
            } else {
                datagramSender.sendPacket(packet, sc.socket.inetAddress, sc.socket.port)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

}