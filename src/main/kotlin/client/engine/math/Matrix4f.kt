package client.engine.math

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix4f {

    companion object {
        const val SIZE = 4

        fun identity(): Matrix4f {
            val result = Matrix4f()
            for (i in 0 until SIZE) {
                for (j in 0 until SIZE) {
                    result.set(i, j, 0F)
                }
            }
            result.set(0, 0, 1F)
            result.set(1, 1, 1F)
            result.set(2, 2, 1F)
            result.set(3, 3, 1F)
            return result
        }

        fun translate(translate: Vector3f): Matrix4f {
            val result = identity()
            result.set(3, 0, translate.x)
            result.set(3, 1, translate.y)
            result.set(3, 2, translate.z)
            return result
        }

        fun rotate(angle: Float, axis: Vector3f): Matrix4f {
            val result = identity()

            val cos = cos(Math.toRadians(angle.toDouble())).toFloat()
            val sin = sin(Math.toRadians(angle.toDouble())).toFloat()
            val c = 1 - cos

            result.set(0, 0, cos + axis.x * axis.x * c)
            result.set(0, 1, axis.x * axis.y * c - axis.z * sin)
            result.set(0, 2, axis.x * axis.z * c + axis.y * sin)
            result.set(1, 0, axis.y * axis.x * c + axis.z * sin)
            result.set(1, 1, cos + axis.y * axis.y * c)
            result.set(1, 2, axis.y * axis.z * c - axis.x * sin)
            result.set(2, 0, axis.z * axis.x * c - axis.y * sin)
            result.set(2, 1, axis.z * axis.y * c + axis.x * sin)
            result.set(2, 2, cos + axis.z * axis.z * c)

            return result
        }

        fun scale(translate: Vector3f): Matrix4f {
            val result = identity()
            result.set(0, 0, translate.x)
            result.set(1, 1, translate.y)
            result.set(2, 2, translate.z)
            return result
        }

        fun transform(position: Vector3f, rotation: Vector3f, scale: Vector3f): Matrix4f {
            val translationMatrix = translate(position)
            val rotXMatrix = rotate(rotation.x, Vector3f(1F, 0F, 0F))
            val rotYMatrix = rotate(rotation.y, Vector3f(0F, 1F, 0F))
            val rotZMatrix = rotate(rotation.z, Vector3f(0F, 0F, 1F))
            val scaleMatrix = scale(scale)
            val rotationMatrix = multiply(rotXMatrix, multiply(rotYMatrix, rotZMatrix))
            return multiply(translationMatrix, multiply(rotationMatrix, scaleMatrix))
        }

        fun projection(fov: Float, aspect: Float, near: Float, far: Float): Matrix4f {
            val result = identity()
            val tanFOV = tan(Math.toRadians(fov.toDouble() / 2)).toFloat()
            val range = far - near
            result.set(0, 0, 1F / (aspect * tanFOV))
            result.set(1, 1, 1F / tanFOV)
            result.set(2, 2, - ((far + near) / range))
            result.set(2, 3, -1F)
            result.set(3, 2, - ((2 * far * near) / range))
            result.set(3, 3, 0F)
            return result
        }

        fun view(position: Vector3f, rotation: Vector3f): Matrix4f {
            val negative = Vector3f(-position.x, -position.y, -position.z)
            val translationMatrix = translate(negative)
            val rotXMatrix = rotate(rotation.x, Vector3f(1F, 0F, 0F))
            val rotYMatrix = rotate(rotation.y, Vector3f(0F, 1F, 0F))
            val rotZMatrix = rotate(rotation.z, Vector3f(0F, 0F, 1F))
            val rotationMatrix = multiply(rotZMatrix, multiply(rotYMatrix, rotXMatrix))
            return multiply(translationMatrix, rotationMatrix)
        }

        fun multiply(matrix: Matrix4f, other: Matrix4f): Matrix4f {
            val result = identity()
            for (i in 0 until SIZE) {
                for (j in 0 until SIZE) {
                    result.set(i, j,
                        matrix.get(i, 0) * other.get(0, j) +
                        matrix.get(i, 1) * other.get(1, j) +
                        matrix.get(i, 2) * other.get(2, j) +
                        matrix.get(i, 3) * other.get(3, j)
                    )
                }
            }
            return result
        }
    }

    val elements = FloatArray(16)

    fun get(x: Int, y: Int): Float {
        return elements[y * SIZE + x]
    }

    fun set(x: Int, y: Int, value: Float) {
        elements[y * SIZE + x] = value
    }
}