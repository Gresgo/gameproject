package client.engine.graphics

import client.engine.math.Vector2f
import client.engine.math.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class Mesh(private val vertices: Array<Vertex>, val indices: IntArray, val material: Material) {

    var vao = 0
    var pbo = 0
    var ibo = 0
    var cbo = 0
    var tbo = 0

    fun create() {
        material.create()

        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        // positon
        val positionBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val positionData = FloatArray(vertices.size * 3)

        for (i in vertices.indices) {
            positionData[i * 3] = vertices[i].position.x
            positionData[i * 3 + 1] = vertices[i].position.y
            positionData[i * 3 + 2] = vertices[i].position.z
        }
        positionBuffer.put(positionData).flip()

        pbo = storeData(positionBuffer, 0, 3)

        // color
        val colorBuffer = MemoryUtil.memAllocFloat(vertices.size * 3)
        val colorData = FloatArray(vertices.size * 3)

        for (i in vertices.indices) {
            colorData[i * 3] = vertices[i].color.x
            colorData[i * 3 + 1] = vertices[i].color.y
            colorData[i * 3 + 2] = vertices[i].color.z
        }
        colorBuffer.put(colorData).flip()

        cbo = storeData(colorBuffer, 1, 3)

        // texture
        val textureBuffer = MemoryUtil.memAllocFloat(vertices.size * 2)
        val textureData = FloatArray(vertices.size * 2)

        for (i in vertices.indices) {
            textureData[i * 2] = vertices[i].textureCord.x
            textureData[i * 2 + 1] = vertices[i].textureCord.y
        }
        textureBuffer.put(textureData).flip()

        tbo = storeData(textureBuffer, 2, 2)

        // add
        val indicesBuffer = MemoryUtil.memAllocInt(indices.size)
        indicesBuffer.put(indices).flip()

        ibo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    private fun storeData(buffer: FloatBuffer, index: Int, size: Int): Int {
        val bufferId = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferId)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        return bufferId
    }

    fun destroy() {
        GL15.glDeleteBuffers(pbo)
        GL15.glDeleteBuffers(cbo)
        GL15.glDeleteBuffers(ibo)
        GL15.glDeleteBuffers(tbo)

        GL30.glDeleteVertexArrays(vao)

        material.destroy()
    }

    companion object {
        fun createCube(texture: String = "beautiful.jpg"): Mesh {
            return Mesh(arrayOf(
                //Back face
                Vertex(Vector3f(-0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f(-0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f( 0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f( 0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f)),

                //Front face
                Vertex(Vector3f(-0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f(-0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f( 0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f( 0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f)),

                //Right face
                Vertex(Vector3f( 0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f( 0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f( 0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f( 0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f)),

                //Left face
                Vertex(Vector3f(-0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f(-0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f(-0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f(-0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f)),

                //Top face
                Vertex(Vector3f(-0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f(-0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f( 0.5f,  0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f( 0.5f,  0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f)),

                //Bottom face
                Vertex(Vector3f(-0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 0.0f)),
                Vertex(Vector3f(-0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(0.0f, 1.0f)),
                Vertex(Vector3f( 0.5f, -0.5f, -0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 1.0f)),
                Vertex(Vector3f( 0.5f, -0.5f,  0.5f), Vector3f(0f,  0f, 0f), Vector2f(1.0f, 0.0f))
                ),
                intArrayOf(
                    //Back face
                    0, 1, 3,
                    3, 1, 2,

                    //Front face
                    4, 5, 7,
                    7, 5, 6,

                    //Right face
                    8, 9, 11,
                    11, 9, 10,

                    //Left face
                    12, 13, 15,
                    15, 13, 14,

                    //Top face
                    16, 17, 19,
                    19, 17, 18,

                    //Bottom face
                    20, 21, 23,
                    23, 21, 22
                ),
                Material("/textures/$texture"))
        }
    }

}