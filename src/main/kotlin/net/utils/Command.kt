package net.utils

object Command {
    const val LOGIN = 1           // normal packet session
    const val ASK_PACKET_NAME = 2 // send int, return string and close
    const val ASK_PACKET_ID = 3   // send string, return int and close
}