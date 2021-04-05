package server

import client.engine.math.Vector3f
import java.util.*
import kotlin.collections.HashSet

class World {
    val players = HashSet<Player>()
    private val playerSpawn = Vector3f(0F, 5F, 0F)
    val serverObjects = Vector<ServerObject>()

    init {
        serverObjects.clear()
        for (x in -8..8)
//            for (y in 0..4)
                for (z in -1..8)
                    serverObjects.add(ServerObject().apply {
                        id = 1
                        val y = 0
                        position = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
                        rotation = Vector3f(0F, 0F, 0F)
                    })
    }

    fun update() {

    }

    fun addObject(gameObject: ServerObject) {

    }

    fun removeObject(gameObject: ServerObject?) {

    }

}