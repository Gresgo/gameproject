package client.engine.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.newdawn.slick.opengl.Texture
import org.newdawn.slick.opengl.TextureLoader
import java.io.IOException

class Material(private val path: String) {

    private lateinit var texture: Texture
    var width = 0f
    var height = 0f
    var textureID = 0

    fun create() {
        try {
            texture = TextureLoader.getTexture(
                path.split(".")[1],
                Material::class.java.getResourceAsStream(path),
                GL11.GL_NEAREST
            )
            width = texture.width
            height = texture.height
            textureID = texture.textureID
        } catch (e: IOException) {
            System.err.println("Can't find texture at $path")
        }
    }

    fun destroy() {
        GL13.glDeleteTextures(textureID)
    }

}