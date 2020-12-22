package client.engine.math

import kotlin.math.sqrt

data class Vector2f(val x: Float, val y: Float) {

    companion object {
        fun add(vector1: Vector2f, vector2: Vector2f): Vector2f {
            return Vector2f(vector1.x + vector2.x, vector1.y + vector2.y)
        }

        fun substract(vector1: Vector2f, vector2: Vector2f): Vector2f {
            return Vector2f(vector1.x - vector2.x, vector1.y - vector2.y)
        }

        fun multiply(vector1: Vector2f, vector2: Vector2f): Vector2f {
            return Vector2f(vector1.x * vector2.x, vector1.y * vector2.y)
        }

        fun divide(vector1: Vector2f, vector2: Vector2f): Vector2f {
            return Vector2f(vector1.x / vector2.x, vector1.y / vector2.y)
        }

        fun length(vector: Vector2f): Float {
            return sqrt(vector.x * vector.x + vector.y * vector.y)
        }

        fun normalize(vector: Vector2f): Vector2f {
            val length = length(vector)
            return divide(vector, Vector2f(length, length))
        }

        fun dot(vector1: Vector2f, vector2: Vector2f): Float {
            return vector1.x * vector2.x + vector1.y * vector2.y
        }
    }

}