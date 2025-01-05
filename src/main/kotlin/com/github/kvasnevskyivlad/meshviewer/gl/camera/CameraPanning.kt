package com.github.kvasnevskyivlad.meshviewer.gl.camera

//import javax.vecmath.Vector3f
import org.joml.Vector3f

class CameraPanning {

    private var _startEye = Vector3f()
    private var _startCenter = Vector3f()

    private var _startX = 0
    private var _startY = 0

    fun startPan(x: Int, y: Int, eye: Vector3f, center: Vector3f) {
        _startEye.set(eye)
        _startCenter.set(center)
        _startX = x
        _startY = y
    }

    fun pan(x: Int, y: Int, eye: Vector3f, center: Vector3f, sensitivity: Float): Pair<Vector3f, Vector3f> {
        val deltaX = x - _startX
        val deltaY = y - _startY

        // Calculate the camera's direction vector
        val direction = Vector3f()
        center.sub(eye, direction) // Direction from eye to center
        direction.normalize()

        // Calculate the right vector (perpendicular to up and direction)
        val right = Vector3f()
        val up = Vector3f(0f, 1f, 0f) // Assuming Y-axis as up
        up.cross(direction, right)
        right.normalize()

        // Dynamically adjust up vector if needed
        right.cross(direction, up)
        up.normalize()

        // Scale by mouse movement and sensitivity
        right.mul(deltaX * sensitivity)
        up.mul(-deltaY * sensitivity)

        // Apply offsets to eye and center positions
        val eyeOffset = Vector3f()
        val centerOffset = Vector3f()
        eyeOffset.add(right)
        eyeOffset.add(up)
        centerOffset.add(right)
        centerOffset.add(up)

        val newEye = Vector3f(_startEye)
        val newCenter = Vector3f(_startCenter)
        newEye.add(eyeOffset)
        newCenter.add(centerOffset)

        return Pair(newEye, newCenter)
    }
}