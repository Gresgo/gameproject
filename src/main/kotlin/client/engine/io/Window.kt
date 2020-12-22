package client.engine.io

import client.engine.graphics.*
import client.engine.math.Matrix4f
import client.engine.math.Vector3f
import client.engine.objects.camera.Camera
import client.engine.objects.camera.FirstPersonCamera
import client.engine.objects.GameObject
import client.engine.objects.camera.ThirdPersonCamera
import net.client.IClientHandler
import net.client.NetClient
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import net.Net
import net.packet.Packet
import server.Player
import net.packet.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*

/**
 * TODO: (1) Render only visible block sides on frame (probably ~x6 performance)
 *              ** visible means that no blocks have same side with current **
 *       (2) Get real player rotation
 */
class Window(private var width: Int = 1280,
             private var height: Int = 720,
             private var title: String = "Title"): IClientHandler, Runnable
{

    companion object {
        private var window: Window? = null

        fun get(): Window {
            if (window == null) {
                window = Window()
            }

            return window!!
        }
    }

    private lateinit var client: NetClient
    private var window: Long = NULL
    private lateinit var input: Input
    private lateinit var renderer: Renderer
    private lateinit var shader: Shader

    private var background = Vector3f(0F, 0F, 0F)
    private var backgroundA = 1F
    private var isRunning = false
    private var isResized = false
    private var isFullscreen = false
    private var windowPosX = IntArray(1)
    private var windowPosY = IntArray(1)

    val meshes = arrayListOf<Mesh>()
    val dirtMesh = Mesh.createCube("floor.jpg").also { meshes.add(it) }
    val playerMesh = Mesh.createCube().also { meshes.add(it) }
    var gameObjects = Vector<GameObject>()

    val projectionMatrix = Matrix4f.projection(70F, (width.toFloat() / height.toFloat()), 0.1F, 1000F)

    val player = GameObject(
        Vector3f(0F, 5F, 0F),
        Vector3f(0F, 0F, 0F),
        Vector3f(1F, 1F, 1F),
        playerMesh
    )
    var players = Vector<GameObject>()

    lateinit var camera: Camera

    private val fps = 60

    fun start() {
        init()
        connect(name = "Jeff")
        //loop()
    }

    private fun connect(host: String = "localhost", port: Int = 8181, name: String) {
        client = Net.createClient(host, port, this)
        client.connect()
        client.sendPacket(LoginPacket().apply { this.name = name }, true)
        //val t = Thread(this, "main")
        isRunning = true
        //t.start()
        run()
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

    private fun createFloor() {
        val mesh = Mesh.createCube("floor.jpg")
        meshes.add(mesh)
        val list = arrayListOf<GameObject>()
        for (x in -8..8)
            for (y in 0..4)
                for (z in -8..8)
                    list.add(GameObject(Vector3f(x.toFloat(), y.toFloat(), z.toFloat()), mesh = mesh))
        gameObjects.addAll(list)
    }

    override fun run() {
        //createFloor()
        var frames = 0
        var fpsTime = 1000 / fps
        var change = 0
        var time = System.currentTimeMillis()
        shader = Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl")
        renderer = Renderer(this, shader)
        camera = FirstPersonCamera(player)
        meshes.forEach {
            it.create()
        }
        //mesh.create()
        shader.create()
        while (!glfwWindowShouldClose(window) && !Input.isKeyDown(GLFW_KEY_ESCAPE) && isRunning) {
            val t = System.currentTimeMillis()
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
                change = 0
            }

            if (Input.isKeyDown(GLFW_KEY_F11)) setFullscreen(!isFullscreen)
            if (Input.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) mouseState(true)
            if (Input.isKeyDown(GLFW_KEY_LEFT_ALT)) mouseState(false)
            if (Input.isKeyDown(GLFW_KEY_F5) && change != 1) {
                camera = when (camera) {
                    is FirstPersonCamera -> ThirdPersonCamera(
                        player,
                        renderer
                    )
                    //is ThirdPersonCamera -> Camera(player.position, player.rotation)
                    else -> FirstPersonCamera(player)
                }
                change = 1
            }
            if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) camera.update()
            players.forEach {
                if (it.id != player.id) renderer.renderGameObject(it, camera)
            }
            gameObjects.forEach {
                renderer.renderGameObject(it, camera)
                it.update()
            }
            glfwSwapBuffers(window)
            if (fps > 0) {
                val delay = fpsTime.toLong() - (System.currentTimeMillis() - t)
                if (delay > 0) {
                    Thread.sleep(delay)
                }
            }
            client.sendPacket(UpdatePlayerPacket().apply {
                player = Player().apply {
                    id = this@Window.player.id
                    position = this@Window.player.position
                    rotation = this@Window.player.rotation
                }
            })
        }
        close()
    }

    override fun handlePacket(packet: Packet) {
        try {
            when(packet) {
                is LoginSuccessPacket -> successLogin(packet)
                is UpdateObjectsPacket -> updateObjects(packet)
                is UpdatePlayersPacket -> updatePlayers(packet)
                else -> println("Unknown packet: $packet")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun successLogin(packet: LoginSuccessPacket) {
        player.id = packet.id
    }

    private fun updateObjects(packet: UpdateObjectsPacket) {
        val objects = Vector<GameObject>()
        //gameObjects.clear()
        packet.serverObjects.forEach {
            objects.add(GameObject(
                position = it.position,
                rotation = it.rotation,
                mesh = if (it.id == 1) dirtMesh else {
                    println("WRONG MESH ID")
                    dirtMesh
                }
            ))
        }
        gameObjects = objects
    }

    private fun updatePlayers(packet: UpdatePlayersPacket) {
        val objects = Vector<GameObject>()
        packet.players.forEach {
            objects.add(GameObject(
                position = it.position,
                rotation = it.rotation,
                mesh = playerMesh
            ).apply {
                id = it.id
            })
        }
        players = objects
    }

    override fun serverDisconnect() {
        isRunning = false
        println("Server disconnect")
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

    fun mouseState(lock: Boolean) {
        glfwSetInputMode(window, GLFW_CURSOR, if (lock) GLFW_CURSOR_DISABLED else GLFW_CURSOR_NORMAL)
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
        client.sendPacket(ClosePacket(), true)
        destroy()
        meshes.forEach {
            it.destroy()
        }
        //mesh.destroy()
        shader.destroy()
    }

    fun destroy() {
        input.destroy()
    }
    
}