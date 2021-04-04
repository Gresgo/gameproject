package client.engine.io

import client.engine.math.Matrix4f
import client.engine.math.Vector2f
import client.engine.math.Vector3f
import client.engine.objects.camera.Camera
import org.lwjglx.opengl.Display
import org.lwjglx.util.vector.Vector4f

class MousePicker(
    private val camera: Camera,
    private val projectionMatrix: Matrix4f
) {

    private var viewMatrix = Matrix4f.view(camera.position, camera.rotation)
    private val projection = org.lwjglx.util.vector.Matrix4f()
    private val view = org.lwjglx.util.vector.Matrix4f()
    var currentRay: Vector3f = Vector3f(0f, 0f, 0f)

    fun update() {
        viewMatrix = Matrix4f.view(camera.position, camera.rotation)
        currentRay = calculateMouseRay()
    }

    init {
        projection.m00 = projectionMatrix.get(0, 0)
        projection.m01 = projectionMatrix.get(0, 1)
        projection.m02 = projectionMatrix.get(0, 2)
        projection.m03 = projectionMatrix.get(0, 3)
        projection.m10 = projectionMatrix.get(1, 0)
        projection.m11 = projectionMatrix.get(1, 1)
        projection.m12 = projectionMatrix.get(1, 2)
        projection.m13 = projectionMatrix.get(1, 3)
        projection.m20 = projectionMatrix.get(2, 0)
        projection.m21 = projectionMatrix.get(2, 1)
        projection.m22 = projectionMatrix.get(2, 2)
        projection.m23 = projectionMatrix.get(2, 3)
        projection.m30 = projectionMatrix.get(3, 0)
        projection.m31 = projectionMatrix.get(3, 1)
        projection.m32 = projectionMatrix.get(3, 2)
        projection.m33 = projectionMatrix.get(3, 3)

        view.m00 = viewMatrix.get(0, 0)
        view.m01 = viewMatrix.get(0, 1)
        view.m02 = viewMatrix.get(0, 2)
        view.m03 = viewMatrix.get(0, 3)
        view.m10 = viewMatrix.get(1, 0)
        view.m11 = viewMatrix.get(1, 1)
        view.m12 = viewMatrix.get(1, 2)
        view.m13 = viewMatrix.get(1, 3)
        view.m20 = viewMatrix.get(2, 0)
        view.m21 = viewMatrix.get(2, 1)
        view.m22 = viewMatrix.get(2, 2)
        view.m23 = viewMatrix.get(2, 3)
        view.m30 = viewMatrix.get(3, 0)
        view.m31 = viewMatrix.get(3, 1)
        view.m32 = viewMatrix.get(3, 2)
        view.m33 = viewMatrix.get(3, 3)
    }

    private fun calculateMouseRay(): Vector3f {
        val mouseX = Input.mouseX
        val normalizedCoors = getNormalizedDeviceCords(Input.mouseX.toFloat(), Input.mouseY.toFloat())
        val clipCords = Vector4f(normalizedCoors.x, normalizedCoors.y, -1f, 1f)
        val eyeCords = toEyeCords(clipCords)
        val worldRay = toWorldCoords(eyeCords)
        return worldRay
    }

    private fun getNormalizedDeviceCords(mouseX: Float, mouseY: Float): Vector2f {
        val x = (2f * mouseX) / 1280f - 1f
        val y = (2f * mouseY) / 720f - 1f
        return Vector2f(x, y)
    }

    private fun toEyeCords(clip: Vector4f): Vector4f {
        val inverted = org.lwjglx.util.vector.Matrix4f.invert(projection, null)
        val eyeCords = org.lwjglx.util.vector.Matrix4f.transform(inverted, clip, null)
        return Vector4f(eyeCords.x, eyeCords.y, -1f, 0f)
    }

    private fun toWorldCoords(eyeCords: Vector4f): Vector3f {
        val invertedView = org.lwjglx.util.vector.Matrix4f.invert(view, null)
        val rayWorld = org.lwjglx.util.vector.Matrix4f.transform(invertedView, eyeCords, null)
        val mouseRay = Vector3f(rayWorld.x, rayWorld.y, rayWorld.z)
        return Vector3f.normalize(mouseRay)
    }

}