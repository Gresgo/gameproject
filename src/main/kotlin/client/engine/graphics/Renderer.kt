package client.engine.graphics

import client.engine.math.Matrix4f
import client.engine.objects.GameObject
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import client.engine.io.Window
import client.engine.objects.camera.Camera

class Renderer(private val window: Window, private val shader: Shader) {

    fun renderGameObject(gameObject: GameObject, camera: Camera) {
        GL30.glBindVertexArray(gameObject.mesh.vao)
        GL30.glEnableVertexAttribArray(0)
        GL30.glEnableVertexAttribArray(1)
        GL30.glEnableVertexAttribArray(2)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, gameObject.mesh.ibo)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, gameObject.mesh.material.textureID)
        shader.bind()
        shader.setUniform("model", Matrix4f.transform(gameObject.position, gameObject.rotation, gameObject.scale))
        shader.setUniform("view", Matrix4f.view(camera.position, camera.rotation))
        shader.setUniform("projection", window.projectionMatrix)
        GL11.glDrawElements(GL11.GL_TRIANGLES, gameObject.mesh.indices.size, GL11.GL_UNSIGNED_INT, 0)
        shader.unbind()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
        GL30.glDisableVertexAttribArray(0)
        GL30.glDisableVertexAttribArray(1)
        GL30.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    fun renderMesh(mesh: Mesh) {
        GL30.glBindVertexArray(mesh.vao)
        GL30.glEnableVertexAttribArray(0)
        GL30.glEnableVertexAttribArray(1)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.ibo)
        shader.bind()
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.indices.size, GL11.GL_UNSIGNED_INT, 0)
        shader.unbind()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
        GL30.glDisableVertexAttribArray(0)
        GL30.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }

}