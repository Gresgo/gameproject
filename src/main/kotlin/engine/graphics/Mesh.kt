package engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer

class Mesh(private val verticies: Array<Vertex>, val indices: IntArray) {

    var vao = 0
    var pbo = 0
    var ibo = 0
    var cbo = 0

    fun create() {
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        val positionBuffer = MemoryUtil.memAllocFloat(verticies.size * 3)
        val positionData = FloatArray(verticies.size * 3)

        for (i in verticies.indices) {
            positionData[i * 3] = verticies[i].position.x
            positionData[i * 3 + 1] = verticies[i].position.y
            positionData[i * 3 + 2] = verticies[i].position.z
        }
        positionBuffer.put(positionData).flip()

        pbo = storeData(positionBuffer, 0, 3)

        val colorBuffer = MemoryUtil.memAllocFloat(verticies.size * 3)
        val colorData = FloatArray(verticies.size * 3)

        for (i in verticies.indices) {
            colorData[i * 3] = verticies[i].color.x
            colorData[i * 3 + 1] = verticies[i].color.y
            colorData[i * 3 + 2] = verticies[i].color.z
        }
        colorBuffer.put(colorData).flip()

        cbo = storeData(colorBuffer, 1, 3)

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

        GL30.glDeleteVertexArrays(vao)
    }

}