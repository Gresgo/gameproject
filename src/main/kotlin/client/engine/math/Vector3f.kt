package client.engine.math

import kotlin.math.sqrt

data class Vector3f(
    var x: Float,
    var y: Float,
    var z: Float
){

    companion object {
        fun add(vector1: Vector3f, vector2: Vector3f): Vector3f {
            return Vector3f(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z)
        }

        fun substract(vector1: Vector3f, vector2: Vector3f): Vector3f {
            return Vector3f(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z)
        }

        fun multiply(vector1: Vector3f, vector2: Vector3f): Vector3f {
            return Vector3f(vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z)
        }

        fun divide(vector1: Vector3f, vector2: Vector3f): Vector3f {
            return Vector3f(vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z)
        }

        fun length(vector: Vector3f): Float {
            return sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z)
        }

        fun normalize(vector: Vector3f): Vector3f {
            val length = length(vector)
            return divide(vector, Vector3f(length, length, length))
        }

        fun dot(vector1: Vector3f, vector2: Vector3f): Float {
            return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z
        }
    }

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

}