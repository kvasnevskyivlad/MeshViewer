package com.github.kvasnevskyivlad.meshviewer.gl.camera

import com.jogamp.opengl.GL2
import com.jogamp.opengl.glu.GLU
import javax.vecmath.Matrix4f
import javax.vecmath.Vector3f


class Camera {

    private val glu = GLU()

    private var aspectRatio = 0.0f

    private var eye = Vector3f(0f, 0f, 5f) // Initial eye position
    private var center = Vector3f(0f, 0f, 0f) // Initial center position
    private var up = Vector3f(0f, 1f, 0f) // Initial up vector
    private var fov = 45f // Initial field of view

    private var rotation = CameraRotation()

    fun startDrag(x: Int, y: Int) = rotation.startDrag(x, y)
    fun drag(x: Int, y: Int) = rotation.drag(x, y)
    fun endDrag() = rotation.endDrag()

    fun resize(gl: GL2, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        aspectRatio = width.toFloat() / height.toFloat()
        rotation.resize(width, height)
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