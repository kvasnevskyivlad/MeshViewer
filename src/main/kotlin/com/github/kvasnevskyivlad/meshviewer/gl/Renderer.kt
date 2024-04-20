package com.github.kvasnevskyivlad.meshviewer.gl

import com.intellij.ui.plaf.beg.BegResources.m
import com.jogamp.opengl.GL
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import java.nio.FloatBuffer
import javax.vecmath.Matrix4f


class Renderer(private val camera: Camera) : GLEventListener {

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()
        gl.glLoadMatrixf(camera.viewMatrix.toFloatArray(), 0)

        // Render your OpenGL content here
        MeshExamples.renderCube(gl)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        val gl = drawable.gl.gL2

        camera.setViewport(x, y, width, height)

        gl.glViewport(0, 0, width, height)
    }

    override fun dispose(drawable: GLAutoDrawable) {}

    companion object {
        fun Matrix4f.toFloatArray(): FloatArray = floatArrayOf(
            m00, m10, m20, m30,
            m01, m11, m21, m31,
            m02, m12, m22, m32,
            m03, m13, m23, m33
        )
    }
}

