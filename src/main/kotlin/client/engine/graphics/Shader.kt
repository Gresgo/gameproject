package client.engine.graphics

import client.engine.math.Matrix4f
import client.engine.math.Vector2f
import client.engine.math.Vector3f
import client.engine.utils.FileUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryUtil

class Shader(vertexPath: String, fragmentPath: String) {

    private val vertexFile = FileUtils.loadAsString(vertexPath)
    private val fragmentFile = FileUtils.loadAsString(fragmentPath)

    private var vertexId = 0
    private var fragmentId = 0
    private var programId = 0

    fun create() {
        programId = GL20.glCreateProgram()
        vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)

        GL20.glShaderSource(vertexId, vertexFile)
        GL20.glCompileShader(vertexId)

        if (GL20.glGetShaderi(vertexId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexId))
            return
        }

        fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)

        GL20.glShaderSource(fragmentId, fragmentFile)
        GL20.glCompileShader(fragmentId)

        if (GL20.glGetShaderi(fragmentId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentId))
            return
        }

        GL20.glAttachShader(programId, vertexId)
        GL20.glAttachShader(programId, fragmentId)

        GL20.glLinkProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programId))
            return
        }

        GL20.glValidateProgram(programId)
        if (GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programId))
            return
        }
    }

    fun getUniformLocation(name: String): Int {
        return GL20.glGetUniformLocation(programId, name)
    }

    fun setUniform(name: String, value: Float) {
        GL20.glUniform1f(getUniformLocation(name), value)
    }

    fun setUniform(name: String, value: Int) {
        GL20.glUniform1i(getUniformLocation(name), value)
    }

    fun setUniform(name: String, value: Boolean) {
        GL20.glUniform1i(getUniformLocation(name), if (value) 1 else 0)
    }

    fun setUniform(name: String, value: Vector2f) {
        GL20.glUniform2f(getUniformLocation(name), value.x, value.y)
    }

    fun setUniform(name: String, value: Vector3f) {
        GL20.glUniform3f(getUniformLocation(name), value.x, value.y, value.z)
    }

    fun setUniform(name: String, value: Matrix4f) {
        val matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE)
        matrix.put(value.elements).flip()
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix)
    }

    fun bind() {
        GL20.glUseProgram(programId)
    }

    fun unbind() {
        GL20.glUseProgram(0)
    }

    fun destroy() {
        GL20.glDetachShader(programId, vertexId)
        GL20.glDetachShader(programId, fragmentId)
        GL20.glDeleteShader(vertexId)
        GL20.glDeleteShader(fragmentId)
        GL20.glDeleteProgram(programId)
    }
}