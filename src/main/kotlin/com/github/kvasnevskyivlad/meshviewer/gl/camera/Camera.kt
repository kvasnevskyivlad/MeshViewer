package com.github.kvasnevskyivlad.meshviewer.gl.camera

import com.jogamp.opengl.GL2
import org.joml.Vector3f
import org.joml.Matrix4f

class Camera {

    private var _aspectRatio = 0.0f

    private var _eye = Vector3f(0f, 0f, 5f) // Initial eye position
    private var _center = Vector3f(0f, 0f, 0f) // Initial center position
    private var _up = Vector3f(0f, 1f, 0f) // Initial up vector
    private var _fov = 45f // Initial field of view

    private var _rotation = CameraRotation()
    private var _panning = CameraPanning()

    private var _dragMode: CameraDragMode = CameraDragMode.NONE

    private val rotation: Matrix4f
        get() = _rotation.rotation

    private val projection: Matrix4f
        get() = Matrix4f().perspective(Math.toRadians(_fov.toDouble()).toFloat(), _aspectRatio, 0.1f, 1000f)

    private val view: Matrix4f
        get() = Matrix4f().lookAt(_eye, _center, _up)

    val transform: Matrix4f
        get() {
            val result = projection
            result.mul(view)
            result.mul(rotation)
            return result
        }

    fun startDrag(x: Int, y: Int, mode: CameraDragMode) {
        _dragMode = mode
        when (mode) {
            CameraDragMode.PAN -> {
                _panning.startPan(x, y, _eye, _center)
            }
            CameraDragMode.ROTATE -> {
                _rotation.startRotate(x, y)
            }
            else -> {}
        }
    }
    fun drag(x: Int, y: Int) {
        when (_dragMode) {
            CameraDragMode.PAN -> {
                val (newEye, newCenter) = _panning.pan(x, y, _eye, _center, 0.01f)
                _eye = newEye
                _center = newCenter
            }
            CameraDragMode.ROTATE -> {
                _rotation.rotate(x, y)
            }
            else -> {}
        }
    }
    fun endDrag() {
        _dragMode = CameraDragMode.NONE
    }

    fun resize(gl: GL2, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        _aspectRatio = width.toFloat() / height.toFloat()
        _rotation.resize(width, height)
    }

    /**
     * Adjusts the camera's distance from the center point for zooming.
     * @param factor The zoom factor. Values > 1.0 zoom in, and values < 1.0 zoom out.
     */
    fun zoom(factor: Float) {
        // Calculate the direction from eye to center
        val direction = Vector3f(_center)
        direction.sub(_eye)

        // Get the current distance between eye and center
        val currentDistance = direction.length()

        // Normalize the direction vector
        direction.normalize()

        // Calculate the zoom increment
        val zoomIncrement = currentDistance * (factor - 1.0f)

        // Calculate the zoom increment and scale the direction
        direction.mul(zoomIncrement)

        // Adjust the eye position by the zoom increment
        _eye.add(direction)
    }
}

enum class CameraDragMode {
    NONE,
    ROTATE,
    PAN
}