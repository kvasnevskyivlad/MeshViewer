package com.github.kvasnevskyivlad.meshviewer.gl.camera

import javax.vecmath.Vector2f
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

        val right = Vector3f()
        val up = Vector3f(0f, 1f, 0f) // Assuming Y-axis is up

        // Calculate the right vector based on the eye and center positions
        right.sub(center, eye)
        right.cross(right, up)
        right.normalize()

        // Scale the right and up vectors by the pan amount
        right.scale(deltaX * sensitivity)
        up.scale(deltaY * sensitivity)

        // Move the eye and center positions accordingly
        val eyeOffset = Vector3f(right)
        val centerOffset = Vector3f(right)
        eyeOffset.add(up)
        centerOffset.add(up)

        val newEye = Vector3f(startEye)
        val newCenter = Vector3f(startCenter)
        newEye.add(eyeOffset)
        newCenter.add(centerOffset)

        return Pair(newEye, newCenter)
    }
}