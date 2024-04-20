package com.github.kvasnevskyivlad.meshviewer.gl

import javax.vecmath.Vector2f
import javax.vecmath.Vector3f

class Viewport {
    var height: Int = 0
        private set
    var x: Int = 0
        private set
    var y: Int = 0
        private set
    var width: Int = 0
        private set

    fun setViewport(x: Int, y: Int, width: Int, height: Int) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }

    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    fun windowToNormalizedDevice(window: Vector2f): Vector3f {
        val x = -1.0f + 2.0f * (window.x - x) / width
        var y = -1.0f + 2.0f * (window.y - y) / height
        y = -y // Windows <-> OpenGL
        return Vector3f(x, y, -1.0f)
    }

    fun normalizedDeviceToWindow(normalized: Vector2f): Vector2f {
        val x = (normalized.x + 1.0f) * width / 2.0f + x
        var y = (normalized.y + 1.0f) * height / 2.0f + y
        y = -y // Windows <-> OpenGL
        return Vector2f(x, y)
    }
}