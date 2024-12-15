package com.github.kvasnevskyivlad.meshviewer.gl.camera

import javax.vecmath.*
import kotlin.math.sqrt

class CameraRotation {

    private var width = 0
    private var height = 0

    private var startVector = Vector3f()
    private var endVector = Vector3f()

    private var _rotation = Matrix4f()
    private var startRotation = Matrix4f()

    init {
        _rotation.setIdentity()
    }

    val rotation: Matrix4f
        get() = _rotation

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun startRotate(x: Int, y: Int) {
        startVector = getArcballVector(x, y)
        startRotation = _rotation
    }

    fun rotate(x: Int, y: Int) {
        endVector = getArcballVector(x, y)

        val rotationAxis = Vector3f()
        rotationAxis.cross(startVector, endVector)
        val angle = startVector.angle(endVector)

        // Create a quaternion representing the rotation
        val axisAngle = AxisAngle4f(rotationAxis, angle)
        val quaternion = Quat4f()
        quaternion.set(axisAngle)

        // Convert the quaternion to a rotation matrix
        val newRotation = Matrix4f()
        newRotation.set(quaternion)

        // Apply new rotation to start rotation and then assign it to current rotation
        newRotation.mul(startRotation)
        _rotation = newRotation
    }

    private fun getArcballVector(x: Int, y: Int): Vector3f {
        val v = Vector3f()
        v.x = (2.0f * x - width) / width
        v.y = (height - 2.0f * y) / height
        val d = v.lengthSquared()
        if (d <= 1) {
            v.z = sqrt(1.0 - d).toFloat()
        } else {
            v.normalize()
        }
        return v
    }
}