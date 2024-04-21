package com.github.kvasnevskyivlad.meshviewer.gl

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import javax.swing.SwingUtilities
import javax.vecmath.*
import kotlin.math.PI

class Camera : MouseMotionListener, MouseListener, MouseWheelListener {

    private var viewport = Viewport()

    private var rotationQuaternion = Quat4f(0f, 0f, 0f, 1f) // Identity quaternion
    private var zoom = 1f

    private var cameraPosition = Vector3f(0f, 0f, 0f)

    private var prevMousePos = Vector2f()
    private var isDragging = false

    fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        viewport.setViewport(x, y, width, height)
    }

    private fun normalizeWithAspectRatio(point: Vector2f): Vector2f {
        val vec = viewport.windowToNormalizedDevice(point)
        return Vector2f(vec.x * viewport.aspectRatio, vec.y)
    }

    override fun mouseDragged(e: MouseEvent) {
        val currMousePos = Vector2f(e.x.toFloat(), e.y.toFloat())

        when {
            SwingUtilities.isRightMouseButton(e) -> {
                if (!isDragging) {
                    prevMousePos = currMousePos
                    isDragging = true
                } else {

                    val angularRotationSpeed = PI.toFloat()
                    val source = normalizeWithAspectRatio(prevMousePos);
                    val target = normalizeWithAspectRatio(currMousePos);
                    val move = Vector3f(target.x - source.x, target.y - source.y, 0.0f)

                    if (move.length() > 0) {

                        val angle = angularRotationSpeed * move.length()

                        val axis = Vector3f()
                        axis.cross(move, Vector3f(0.0f, 0.0f, -1.0f))
                        axis.normalize()

                        val axisAngle = AxisAngle4f(axis, angle)
                        val newRotationQuaternion = Quat4f()
                        newRotationQuaternion.set(axisAngle)
                        rotationQuaternion.mul(newRotationQuaternion)

                        prevMousePos = currMousePos
                    }
                }
            }
            SwingUtilities.isLeftMouseButton(e) -> {
                //val delta = Vector2f(currMousePos.x - prevMousePos.x, currMousePos.y - prevMousePos.y)
                //cameraPosition.add(delta.x * 0.01f, -delta.y * 0.01f, 0f)
                //prevMousePos = currMousePos
            }
        }
    }

    override fun mouseMoved(e: MouseEvent) {
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        val wheelRotation = e.wheelRotation
        if (wheelRotation < 0) {
            zoomIn()
        } else {
            zoomOut()
        }
    }

    override fun mouseClicked(e: MouseEvent) {
    }

    override fun mousePressed(e: MouseEvent) {
        prevMousePos = Vector2f(e.x.toFloat(), e.y.toFloat())
    }

    override fun mouseReleased(e: MouseEvent) {
        isDragging = false
    }

    override fun mouseEntered(e: MouseEvent) {
    }

    override fun mouseExited(e: MouseEvent) {
    }

    val viewMatrix: Matrix4f
        get() {
            val viewMatrix = Matrix4f()

            val rotationMatrix = Matrix4f()
            rotationMatrix.set(rotationQuaternion)

            val translationMatrix = Matrix4f()
            translationMatrix.setIdentity()
            translationMatrix.setTranslation(cameraPosition)

            viewMatrix.setIdentity()
            viewMatrix.mul(rotationMatrix)
            viewMatrix.mul(translationMatrix)
            viewMatrix.setScale(zoom)

            return viewMatrix
        }

    /*fun windowToView(windowPoint: Vector2f): Vector3f {
        val clipPoint = viewport.windowToNormalizedDevice(windowPoint)
        return projection.transform.applyInverse(clipPoint)
    }*/

    private fun zoomIn() {
        zoom *= 1.1f
    }

    private fun zoomOut() {
        zoom /= 1.1f
    }
}