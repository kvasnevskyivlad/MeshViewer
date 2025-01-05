package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.geometry.MeshExamples
import com.github.kvasnevskyivlad.meshviewer.geometry.toFloatArray
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Renderer(private val camera: Camera) : GLEventListener {

    private lateinit var shaderProgramManager : ShaderProgramManager
    private var vertexArrayObject = 0
    private var vertexBufferObject = 0

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
        shaderProgramManager = ShaderProgramManager(gl)

        // init mesh
        MeshExamples.triangle.initializeBuffers(gl)
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)

        // Use shader
        val shaderProgram = shaderProgramManager.use("basic")
        shaderProgram?.setTransform(camera.transform)

        // Apply transform
        //transform(gl)

        // Draw axes
        //drawAxes(gl)

        // Render your OpenGL content here
        render(gl, MeshExamples.triangle)

        val error = gl.glGetError()

        // Re-enable culling after rendering the transparent object (optional)
        //gl.glEnable(GL.GL_CULL_FACE)


    }

    private fun render(gl: GL2, mesh: Mesh) {

        // Bind VAO (Mesh was already initialized in init)
        gl.glBindVertexArray(mesh.vao)

        // Draw the triangles
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, mesh.triangles.size * 3)

        // Unbind the VAO
        gl.glBindVertexArray(0)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        val gl = drawable.gl.gL2
        camera.resize(gl, width, height)
    }

    override fun dispose(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2

        // Delete buffers for mesh
        gl.glDeleteBuffers(1, intArrayOf(vertexBufferObject), 0)
        gl.glDeleteVertexArrays(1, intArrayOf(vertexArrayObject), 0)

        // delete buffers for mesh
        MeshExamples.triangle.deleteBuffers(gl)
    }
    
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

