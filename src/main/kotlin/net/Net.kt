package net

import net.client.IClientHandler
import net.client.NetClient
import net.client.NetClientImpl
import net.server.IServerHandler
import net.server.NetServer
import net.server.NetServerImpl

object Net {

    fun createServer(port: Int, handler: IServerHandler): NetServer =
        NetServerImpl(port, handler)

    fun createClient(host: String, port: Int, handler: IClientHandler): NetClient =
        NetClientImpl(host, port, handler)

}