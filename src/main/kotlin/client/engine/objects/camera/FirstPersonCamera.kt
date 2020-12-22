package client.engine.objects.camera

import client.engine.io.Input
import client.engine.math.Vector3f
import client.engine.objects.GameObject
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

class FirstPersonCamera(private val gameObject: GameObject): Camera(gameObject.position, gameObject.rotation) {

    override fun update() {
        newMouseX = Input.mouseX
        newMouseY = Input.mouseY

        val x = sin(Math.toRadians(rotation.y.toDouble())).toFloat() * moveSpeed
        val z = cos(Math.toRadians(rotation.y.toDouble())).toFloat() * moveSpeed

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(-z, 0F, x))
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(z, 0F, -x))
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(-x, 0F, -z))
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(x, 0F, z))
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(0F, moveSpeed, 0F))
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) gameObject.position = Vector3f.add(gameObject.position, Vector3f(0F, -moveSpeed, 0F))

        val dx = (newMouseX - oldMouseX).toFloat()
        val dy = (newMouseY - oldMouseY).toFloat()

        //gameObject.rotation = Vector3f.add(gameObject.rotation, Vector3f(-dy * mouseSensitivity, 0F, 0F))

        //position = Vector3f.add(gameObject.position, Vector3f(0F, 0F, -0.6F))
        position = gameObject.position
        //rotation = gameObject.rotation
        rotation = Vector3f.add(rotation, Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0F))

        oldMouseX = newMouseX
        oldMouseY = newMouseY
    }

}