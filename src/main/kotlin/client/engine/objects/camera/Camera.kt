package client.engine.objects.camera

import client.engine.io.Input
import client.engine.math.Vector3f
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

open class Camera(var position: Vector3f, var rotation: Vector3f) {

    protected var oldMouseX = 0.0
    protected var oldMouseY = 0.0
    protected var newMouseX = 0.0
    protected var newMouseY = 0.0

    protected var moveSpeed = 0.05F
    protected var mouseSensitivity = 0.2F

    open fun update() {
        newMouseX = Input.mouseX
        newMouseY = Input.mouseY

        val x = sin(Math.toRadians(rotation.y.toDouble())).toFloat() * moveSpeed
        val z = cos(Math.toRadians(rotation.y.toDouble())).toFloat() * moveSpeed

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, Vector3f(-z, 0F, x))
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, Vector3f(z, 0F, -x))
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, Vector3f(-x, 0F, -z))
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, Vector3f(x, 0F, z))
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = Vector3f.add(position, Vector3f(0F, moveSpeed, 0F))
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position = Vector3f.add(position, Vector3f(0F, -moveSpeed, 0F))

        val dx = (newMouseX - oldMouseX).toFloat()
        val dy = (newMouseY - oldMouseY).toFloat()

        rotation = Vector3f.add(rotation, Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0F))

        oldMouseX = newMouseX
        oldMouseY = newMouseY
    }

}