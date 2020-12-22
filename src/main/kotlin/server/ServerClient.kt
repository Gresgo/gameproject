package server

import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class ServerClient(
    override val id: Int,
    override val socket: Socket
): Client() {
    var outData: DataOutputStream? = null
        get() {
            if (field == null) {
                synchronized(socket) {
                    if (field == null)
                        outData = DataOutputStream(socket.getOutputStream())
                }
            }
            return field
        }

    override fun equals(other: Any?): Boolean =
        if (other is ServerClient) other.id == id else false

    override fun hashCode(): Int = id

    override fun disconnect() {
        try {
            outData?.close()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        isConnected = false
    }

}