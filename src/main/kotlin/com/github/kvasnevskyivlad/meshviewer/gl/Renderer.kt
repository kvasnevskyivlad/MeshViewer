package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.geometry.MeshExamples
import com.github.kvasnevskyivlad.meshviewer.geometry.toFloatArray
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener

class Renderer(private val camera: Camera) : GLEventListener {

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)

        // Apply transform
        transform(gl)

        // Draw axes
        drawAxes(gl)

        // Render your OpenGL content here
        render(gl, MeshExamples.triangle)
    }

    private fun transform(gl: GL2) {

        // Set Projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glMultMatrixf(camera.projection.toFloatArray(), 0)

        // Set Modelview matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW)
        gl.glLoadIdentity()
        gl.glMultMatrixf(camera.view.toFloatArray(), 0)

        // Set Rotation matrix
        gl.glMultMatrixf(camera.rotation.toFloatArray(), 0)
    }

    private fun render(gl: GL2, mesh: Mesh) {

        mesh.triangles.forEach { triangle ->
            val vertexA = mesh.vertices[triangle.a]
            val vertexB = mesh.vertices[triangle.b]
            val vertexC = mesh.vertices[triangle.c]

            gl.glBegin(GL.GL_TRIANGLES)

            // Render vertices of the triangle
            gl.glColor3f(1.0f, 0.0f, 0.0f)
            gl.glVertex3d(vertexA.x, vertexA.y, vertexA.z)

            gl.glColor3f(0.0f, 1.0f, 0.0f)
            gl.glVertex3d(vertexB.x, vertexB.y, vertexB.z)

            gl.glColor3f(0.0f, 0.0f, 1.0f)
            gl.glVertex3d(vertexC.x, vertexC.y, vertexC.z)

            gl.glEnd()
        }
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        val gl = drawable.gl.gL2
        camera.resize(gl, width, height)
        transform(gl)
    }

    override fun dispose(drawable: GLAutoDrawable) {}

    private fun drawAxes(gl: GL2) {
        // X axis (red)
        gl.glColor3f(1.0f, 0.0f, 0.0f) // Red
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(1.0f, 0.0f, 0.0f)
        gl.glEnd()

        // Y axis (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f) // Green
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(0.0f, 1.0f, 0.0f)
        gl.glEnd()

        // Z axis (blue)
        gl.glColor3f(0.0f, 0.0f, 1.0f) // Blue
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(0.0f, 0.0f, 1.0f)
        gl.glEnd()
    }
}

