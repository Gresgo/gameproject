package server

import java.net.Socket

abstract class Client {
    abstract val id: Int
    abstract val socket: Socket
    @Volatile var isConnected = true

    abstract fun disconnect()
}