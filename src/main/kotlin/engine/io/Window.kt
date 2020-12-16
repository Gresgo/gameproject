package engine.io

import engine.graphics.Mesh
import engine.graphics.Renderer
import engine.graphics.Shader
import engine.graphics.Vertex
import engine.math.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.lang.IllegalStateException

class Window(private var width: Int = 1920,
             private var height: Int = 1080,
             private var title: String = "Title") {

    companion object {
        private var window: Window? = null

        fun get(): Window {
            if (window == null) {
                window = Window()
            }

            return window!!
        }
    }

    private var window: Long = NULL
    private lateinit var input: Input
    private lateinit var renderer: Renderer
    private lateinit var shader: Shader

    private var background = Vector3f(0F, 0F, 0F)
    private var backgroundA = 1F
    private var isResized = false
    private var isFullscreen = false
    private var windowPosX = IntArray(1)
    private var windowPosY = IntArray(1)

    fun run() {
        init()
        loop()
    }

    private fun init() {
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE)

        window = glfwCreateWindow(width, height, title, NULL, NULL)
        if (window == NULL) {
            throw IllegalStateException("Unable to create GLFW")
        }

        glfwMakeContextCurrent(window)
        GL.createCapabilities()

        glEnable(GL_DEPTH_TEST)

        setCallbacks()

        glfwSwapInterval(1)
        glfwShowWindow(window)
    }

    val mesh = Mesh(
        verticies = arrayOf(
            Vertex(Vector3f(-0.5F, 0.5F, 0F), Vector3f(0.0F, 0.0F, 1.0F)),
            Vertex(Vector3f(0.5F, 0.5F, 0F), Vector3f(0.0F, 0.0F, 1.0F)),
            Vertex(Vector3f(0.5F, -0.5F, 0F), Vector3f(0.0F, 0.0F, 1.0F)),
            Vertex(Vector3f(-0.5F, -0.5F, 0F), Vector3f(0.0F, 0.0F, 1.0F))
        ),
        indices = intArrayOf(
            0, 1, 2,
            0, 3, 2
        )
    )

    private fun loop() {
        var frames = 0
        var time = System.currentTimeMillis()
        shader = Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl")
        renderer = Renderer(shader)
        mesh.create()
        shader.create()
        while (!glfwWindowShouldClose(window) && !Input.isKeyDown(GLFW_KEY_ESCAPE)) {
            if (isResized) {
                glViewport(0, 0, width, height)
                isResized = false
            }

            glClearColor(background.x, background.y, background.z, backgroundA)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glfwPollEvents()

            frames++
            if (System.currentTimeMillis() > time + 1000) {
                glfwSetWindowTitle(window, "$title | FPS: $frames")
                time = System.currentTimeMillis()
                frames = 0
            }

            if (Input.isKeyDown(GLFW_KEY_F11)) setFullscreen(!isFullscreen)
            renderer.renderMesh(mesh)
            glfwSwapBuffers(window)
        }
        close()
    }

    private val sizeCallback = object : GLFWWindowSizeCallback() {
        override fun invoke(window: Long, width: Int, height: Int) {
            this@Window.width = width
            this@Window.height = height
            isResized = true
        }
    }

    private fun setCallbacks() {
        input = Input()

        glfwSetKeyCallback(window, input.keyboardCallback)
        glfwSetCursorPosCallback(window, input.mousePositionCallback)
        glfwSetMouseButtonCallback(window, input.mouseButtonCallback)
        glfwSetScrollCallback(window, input.mouseScrollCallback)
        glfwSetWindowSizeCallback(window, sizeCallback)
    }

    fun setFullscreen(fullscreen: Boolean) {
        isFullscreen = fullscreen
        isResized = true
        if (isFullscreen) {
            glfwGetWindowPos(window, windowPosX, windowPosY)
            glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, width, height, 0)
        } else {
            glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0)
        }
    }

    fun setBackgroundColor(r: Float, g: Float, b: Float, a: Float = 1F) {
        background.set(r, g, b)
        backgroundA = a
    }

    private fun close() {
        destroy()
        mesh.destroy()
        shader.destroy()
    }

    fun destroy() {
        input.destroy()
    }
    
}