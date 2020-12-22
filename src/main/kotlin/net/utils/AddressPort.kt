package net.utils

import java.net.InetAddress

class AddressPort(val address: InetAddress, val port: Int) {

    override fun equals(other: Any?): Boolean =
        if (other is AddressPort)
            address == other.address && port == other.port
        else false

    override fun hashCode(): Int = address.hashCode() xor port
}