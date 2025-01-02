package com.github.kvasnevskyivlad.meshviewer.gl.camera

import com.github.kvasnevskyivlad.meshviewer.geometry.toColumnMajor
import com.jogamp.opengl.GL2
import com.jogamp.opengl.math.FloatUtil
import javax.vecmath.Matrix4f
import javax.vecmath.Vector3f

class Camera {

    private var _aspectRatio = 0.0f

    private var _eye = Vector3f(0f, 0f, 10f) // Initial eye position
    private var _center = Vector3f(0f, 0f, 0f) // Initial center position
    private var _up = Vector3f(0f, 1f, 0f) // Initial up vector
    private var _fov = 45f // Initial field of view

    private var _rotation = CameraRotation()
    private var _panning = CameraPanning()

    private var _dragMode: CameraDragMode = CameraDragMode.NONE

    val rotation: Matrix4f
        get() = _rotation.rotation.toColumnMajor()

    val projection: Matrix4f
        get() = createProjectionColumnMajorMatrix()

    val view: Matrix4f
        get() = createViewColumnMajorMatrix()

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

        // Calculate the zoom increment and scale the direction
        direction.scale(currentDistance * (factor - 1.0f))

        // Adjust the eye position by the zoom increment
        _eye.add(direction)
    }

    private fun createProjectionColumnMajorMatrix(): Matrix4f {
        val result = FloatArray(16)
        FloatUtil.makePerspective(result, 0, true, _fov * FloatUtil.PI / 180.0f, _aspectRatio, 0.1f, 1000.0f)
        return Matrix4f(result)
    }

    private fun createViewColumnMajorMatrix() : Matrix4f {
        val result = FloatArray(16)
        val input = FloatArray(16)
        val temp = FloatArray(16)

        input[0 + 0] = _eye.x
        input[1 + 0] = _eye.y
        input[2 + 0] = _eye.z
        input[0 + 4] = _center.x
        input[1 + 4] = _center.y
        input[2 + 4] = _center.z
        input[0 + 8] = _up.x
        input[1 + 8] = _up.y
        input[2 + 8] = _up.z

        FloatUtil.makeLookAt(result, 0, input, 0, input, 4, input, 8, temp)
        return Matrix4f(result)
    }
}

enum class CameraDragMode {
    NONE,
    ROTATE,
    PAN
}