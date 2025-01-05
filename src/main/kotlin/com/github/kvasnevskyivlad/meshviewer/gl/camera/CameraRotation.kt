package com.github.kvasnevskyivlad.meshviewer.gl.camera

import org.joml.AxisAngle4f
import org.joml.Vector3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import kotlin.math.sqrt


class CameraRotation {

    private var _width = 0
    private var _height = 0

    private var _startVector = Vector3f()
    private var _endVector = Vector3f()

    private var _rotation = Matrix4f()
    private var _startRotation = Matrix4f()

    private var sensitivity: Float = 1.0f

    val rotation: Matrix4f
        get() = _rotation

    fun resize(width: Int, height: Int) {
        this._width = width
        this._height = height
    }

    fun startRotate(x: Int, y: Int) {
        _startVector = getArcballVector(x, y)
        _startRotation = _rotation
    }

    fun rotate(x: Int, y: Int) {
        _endVector = getArcballVector(x, y)

        val rotationAxis = Vector3f(_startVector)
        rotationAxis.cross(_endVector)
        rotationAxis.normalize()
        val angle = _startVector.angle(_endVector) * sensitivity

        // Create a quaternion representing the rotation
        val axisAngle = AxisAngle4f(angle, rotationAxis)
        val quaternion = Quaternionf(axisAngle)

        // Convert the quaternion to a rotation matrix
        val newRotation = Matrix4f()
        newRotation.set(quaternion)

        // Apply new rotation to start rotation and then assign it to current rotation
        newRotation.mul(_startRotation)
        _rotation = newRotation
    }

    private fun getArcballVector(x: Int, y: Int): Vector3f {
        val v = Vector3f()
        v.x = (2.0f * x - _width) / _width
        v.y = (_height - 2.0f * y) / _height
        val d = v.lengthSquared()
        if (d <= 1) {
            v.z = sqrt(1.0 - d).toFloat()
        } else {
            v.normalize()
        }
        return v
    }
}