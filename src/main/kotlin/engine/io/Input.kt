package engine.io

import org.lwjgl.glfw.*

class Input {

    companion object {
        private val keys = BooleanArray(GLFW.GLFW_KEY_LAST)
        private val mouseButtons = BooleanArray(GLFW.GLFW_MOUSE_BUTTON_LAST)
        var mouseX = 0.0
        var mouseY = 0.0
        var scrollX = 0.0
        var scrollY = 0.0

        fun isKeyDown(key: Int): Boolean = keys[key]

        fun isButtonDown(button: Int): Boolean = mouseButtons[button]

    }

    val keyboardCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            keys[key] = action != GLFW.GLFW_RELEASE
        }
    }

    val mousePositionCallback = object : GLFWCursorPosCallback() {
        override fun invoke(window: Long, xpos: Double, ypos: Double) {
            mouseX = xpos
            mouseY = ypos
        }
    }

    val mouseButtonCallback = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            mouseButtons[button] = action != GLFW.GLFW_RELEASE
        }
    }

    val mouseScrollCallback = object : GLFWScrollCallback() {
        override fun invoke(window: Long, xoffset: Double, yoffset: Double) {
            scrollX += xoffset
            scrollY += yoffset
        }
    }

    fun destroy() {
        keyboardCallback.free()
        mouseButtonCallback.free()
        mousePositionCallback.free()
        mouseScrollCallback.free()
    }

}