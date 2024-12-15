package com.github.kvasnevskyivlad.meshviewer.gl.camera

import javax.vecmath.Vector3f

class CameraPanning {

    private var startEye = Vector3f()
    private var startCenter = Vector3f()

    private var startX = 0
    private var startY = 0

    fun startPan(x: Int, y: Int, eye: Vector3f, center: Vector3f) {
        startEye.set(eye)
        startCenter.set(center)
        startX = x
        startY = y
    }

    fun pan(x: Int, y: Int, eye: Vector3f, center: Vector3f, sensitivity: Float): Pair<Vector3f, Vector3f> {
        val deltaX = x - startX
        val deltaY = y - startY

        // Calculate the camera's direction vector
        val direction = Vector3f()
        direction.sub(center, eye) // Direction from eye to center
        direction.normalize()

        // Calculate the right vector (perpendicular to up and direction)
        val right = Vector3f()
        val up = Vector3f(0f, 1f, 0f) // Assuming Y-axis as up
        right.cross(up, direction)
        right.normalize()

        // Dynamically adjust up vector if needed
        up.cross(direction, right)
        up.normalize()

        // Scale by mouse movement and sensitivity
        right.scale(deltaX * sensitivity)
        up.scale(deltaY * sensitivity)

        // Apply offsets to eye and center positions
        val eyeOffset = Vector3f()
        val centerOffset = Vector3f()
        eyeOffset.add(right)
        eyeOffset.add(up)
        centerOffset.add(right)
        centerOffset.add(up)

        val newEye = Vector3f(startEye)
        val newCenter = Vector3f(startCenter)
        newEye.add(eyeOffset)
        newCenter.add(centerOffset)

        return Pair(newEye, newCenter)
    }
}