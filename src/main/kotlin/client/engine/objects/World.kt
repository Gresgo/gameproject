package client.engine.objects

import client.engine.graphics.Mesh
import client.engine.math.Vector3f

class World {

    companion object {
        fun create(): ArrayList<GameObject> {
            val mesh = Mesh.createCube()
            val result = arrayListOf<GameObject>()

            for (i in 0..20)
                for (j in 0..20)
                    result.add(GameObject(Vector3f(i.toFloat(), j.toFloat(), 0F), mesh = mesh))

            return result
        }
    }

}