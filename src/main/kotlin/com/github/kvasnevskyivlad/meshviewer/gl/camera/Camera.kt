package com.github.kvasnevskyivlad.meshviewer.gl.camera

import com.jogamp.opengl.GL2
import com.jogamp.opengl.glu.GLU
import javax.vecmath.Matrix4f
import javax.vecmath.Vector3f

class Camera {

    private val glu = GLU()

    private var aspectRatio = 0.0f

    private var eye = Vector3f(0f, 0f, 10f) // Initial eye position
    private var center = Vector3f(0f, 0f, 0f) // Initial center position
    private var up = Vector3f(0f, 1f, 0f) // Initial up vector
    private var fov = 45f // Initial field of view

    private var rotation = CameraRotation()
    private var panning = CameraPanning()

    private var dragMode: CameraDragMode = CameraDragMode.NONE

    fun startDrag(x: Int, y: Int, mode: CameraDragMode) {
        dragMode = mode

        when (mode) {
            CameraDragMode.PAN -> {
                panning.startPan(x, y, eye, center)
            }
            CameraDragMode.ROTATE -> {
                rotation.startRotate(x, y)
            }
            else -> {}
        }
    }
    fun drag(x: Int, y: Int) {
        when (dragMode) {
            CameraDragMode.PAN -> {
                val (newEye, newCenter) = panning.pan(x, y, eye, center, 0.1f)
                eye = newEye
                center = newCenter
            }
            CameraDragMode.ROTATE -> {
                rotation.rotate(x, y)
            }
            else -> {}
        }
    }
    fun endDrag() {
        dragMode = CameraDragMode.NONE
    }

    fun resize(gl: GL2, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        aspectRatio = width.toFloat() / height.toFloat()
        rotation.resize(width, height)
    }

    /**
     * Adjusts the camera's distance from the center point for zooming.
     * @param factor The zoom factor. Values > 1.0 zoom in, and values < 1.0 zoom out.
     */
    fun zoom(factor: Float) {
        // Calculate the direction from eye to center
        val direction = Vector3f(center)
        direction.sub(eye)

        // Get the current distance between eye and center
        val currentDistance = direction.length()

        // Normalize the direction vector
        direction.normalize()

        // Calculate the zoom increment and scale the direction
        direction.scale(currentDistance * (factor - 1.0f))

        // Adjust the eye position by the zoom increment
        eye.add(direction)
    }

    /**
     * Moves the camera position horizontally and vertically based on mouse drag.
     * @param deltaX The horizontal movement.
     * @param deltaY The vertical movement.
     */
    fun pan(deltaX: Float, deltaY: Float) {
        // Calculate the direction vector from eye to center
        val direction = Vector3f(center)
        direction.sub(eye)

        // Calculate the right vector (perpendicular to the up vector and the direction vector)
        val right = Vector3f()
        right.cross(up, direction)
        right.normalize()

        // Scale the right vector by the horizontal movement amount
        right.scale(deltaX * 0.01f) // Adjust the sensitivity if needed

        // Scale the up vector by the vertical movement amount
        val upMove = Vector3f(up)
        upMove.scale(deltaY * 0.01f) // Adjust the sensitivity if needed

        // Adjust the eye and center positions by adding the scaled right and up vectors
        eye.add(right)
        eye.add(upMove)
        center.add(right)
        center.add(upMove)
    }

    fun applyTransform(gl: GL2) {
        gl.glMatrixMode(GL2.GL_PROJECTION)
        gl.glLoadIdentity()
        glu.gluPerspective(fov.toDouble(), aspectRatio.toDouble(), 0.1, 1000.0)

        gl.glMatrixMode(GL2.GL_MODELVIEW)
        gl.glLoadIdentity()
        glu.gluLookAt(
            eye.x.toDouble(), eye.y.toDouble(), eye.z.toDouble(),
            center.x.toDouble(), center.y.toDouble(), center.z.toDouble(),
            up.x.toDouble(), up.y.toDouble(), up.z.toDouble()
        )

        gl.glMultMatrixf(rotation.rotation.toFloatArray(), 0)
    }

    companion object {
        fun Matrix4f.toFloatArray(): FloatArray = floatArrayOf(
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32,
            m03, m13, m23, m33
        )
    }
}

enum class CameraDragMode {
    NONE,
    ROTATE,
    PAN
}