package client.engine.objects.camera

import client.engine.graphics.Renderer
import client.engine.io.Input
import client.engine.math.Vector3f
import client.engine.objects.GameObject
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

class ThirdPersonCamera(private val gameObject: GameObject, private val renderer: Renderer):
    Camera(Vector3f(0F, 0F, 0F), Vector3f(0F, 0F, 0F)) {

    private var distance = 2.0F
    private var horizontalAngle = 0F
    private var verticalAngle = 0F

    override fun update() {
        newMouseX = Input.mouseX
        newMouseY = Input.mouseY

        val dx = (newMouseX - oldMouseX).toFloat()
        val dy = (newMouseY - oldMouseY).toFloat()

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            verticalAngle -= dy * mouseSensitivity
            horizontalAngle += dx * mouseSensitivity
        }
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            if (distance > 0) {
                distance += dy * mouseSensitivity
            } else {
                distance = 0.1F
            }
        }

        val horizontalDistance = (distance * cos(Math.toRadians(verticalAngle.toDouble()))).toFloat()
        val verticalDistance = (distance * sin(Math.toRadians(verticalAngle.toDouble()))).toFloat()

        val xOffset = horizontalDistance * sin(Math.toRadians(-horizontalAngle.toDouble())).toFloat()
        val zOffset = horizontalDistance * cos(Math.toRadians(-horizontalAngle.toDouble())).toFloat()

        position.set(gameObject.position.x + xOffset, gameObject.position.y - verticalDistance, gameObject.position.z + zOffset)
        rotation.set(verticalAngle, -horizontalAngle, 0F)

        oldMouseX = newMouseX
        oldMouseY = newMouseY

        renderer.renderGameObject(gameObject, this)
    }

}