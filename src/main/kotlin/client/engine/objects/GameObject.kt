package client.engine.objects

import client.engine.graphics.Mesh
import client.engine.math.Vector3f

class GameObject(var position: Vector3f,
                 var rotation: Vector3f = Vector3f(0F, 0F, 0F),
                 var scale: Vector3f = Vector3f(1F, 1F, 1F),
                 var mesh: Mesh) {
    var id = -1

    fun update() {
        //position.z = position.z - 0.01F
    }

}