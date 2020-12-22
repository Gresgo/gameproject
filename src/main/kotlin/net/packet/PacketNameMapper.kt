package net.packet

import java.util.*

class PacketNameMapper {

    private val nameToId = Hashtable<String, Int>()
    private val idToName = Hashtable<Int, String>()

    fun getId(name: String): Int = nameToId[name] ?: -1

    fun getName(id: Int): String? = idToName[id]

    fun addName(name: String, id: Int) {
        nameToId[name] = id
        idToName[id] = name
        println("packet name added: $name $id")
    }

}