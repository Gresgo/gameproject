package server

import net.Net
import net.packet.*
import net.server.IServerHandler
import net.server.NetServer
import java.lang.Exception
import java.util.*

class Server(port: Int): IServerHandler, Runnable {
    private val server: NetServer = Net.createServer(port, this)
    @Volatile private var isRunning = true
    private val players = Hashtable<Client, Player>()
    private val world = World()
    private var frames = 0L
    private var frameCount = 0

    init {
        val t = Thread(this, "main")
        t.start()
        server.start()
    }

    override fun run() {
        val delay = 1000L / 60
        while (isRunning) {
            try {
                Thread.sleep(delay)
                update()
                frames++
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        frameCount++
        updatePlayers()
        world.update()
        sendPackets()
    }

    private fun updatePlayers() {
        // TODO: update players coordinates
    }

    private fun sendPackets() {
        val updatePlayersPacket = UpdatePlayersPacket().apply {
            players = world.players.toCollection(Vector())
        }
        val updateObjectsPacket = UpdateObjectsPacket().apply {
            serverObjects = world.serverObjects
        }
        val metaPacket = MetaPacket()
        metaPacket.packets.add(updatePlayersPacket)
        metaPacket.packets.add(updateObjectsPacket)

        players.forEach {
            val client = it.key
            try {
                if (client.isConnected) server.sendPacket(metaPacket, client)
            } catch (e: Exception) {
                e.printStackTrace()
                client.disconnect()
            }
        }
    }

    override fun clientConnect(client: Client) {
        println("Client ${client.id} connected")
    }

    override fun clientDisconnect(client: Client) {
        val player = players[client]
        players.remove(client)
        world.removeObject(player)
        println("Client ${client.id} disconnected")
    }

    override fun handlePacket(packet: Packet, source: Client) {
        try {
            when (packet) {
                is LoginPacket -> login(packet, source)
                is UpdateObjectPacket -> updateObject(packet, source)
                is ChatPacket -> chat(packet, source)
                is UpdatePlayerPacket -> updatePlayer(packet, source)
                else -> println("Unknown packet: $packet")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun login(packet: LoginPacket, client: Client) {
        val player = Player().apply {
            id = client.id
            name = packet.name
        }
        world.players.add(player)
        players[client] = player
        try {
            server.sendPacket(LoginSuccessPacket().apply { id = client.id }, client, true)
            server.sendPacket(UpdateObjectsPacket().apply {
                this.serverObjects = world.serverObjects
            }, client)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("Client ${client.id} login")
    }

    private fun updateObject(packet: UpdateObjectPacket, client: Client) {
        // TODO: update object
    }

    private fun chat(packet: ChatPacket, client: Client) {
        // TODO: chat
    }

    private fun updatePlayer(packet: UpdatePlayerPacket, client: Client) {
        world.players.find { it.id == packet.player.id }?.apply {
            position = packet.player.position
            rotation = packet.player.rotation
        }
    }
}