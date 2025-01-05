package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.jogamp.opengl.GL2
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MeshRenderable(private val mesh: Mesh) {
    private var vbo: Int = 0
    private var vao: Int = 0

    // Check whether buffers are initialized
    private val isInitialized: Boolean
        get() = vbo != 0 && vao != 0

    // Initialize OpenGL buffers (if not already initialized)
    private fun initializeBuffers(gl: GL2) {
        if (isInitialized) return // Already initialized

        val vertexData = mutableListOf<Float>()
        mesh.triangles.forEach { triangle ->
            val vertexA = mesh.vertices[triangle.a]
            val vertexB = mesh.vertices[triangle.b]
            val vertexC = mesh.vertices[triangle.c]

            // Format: [x, y, z, r, g, b]
            vertexData.addAll(listOf(
                vertexA.x.toFloat(), vertexA.y.toFloat(), vertexA.z.toFloat(), 1.0f, 0.0f, 0.0f,  // red
                vertexB.x.toFloat(), vertexB.y.toFloat(), vertexB.z.toFloat(), 0.0f, 1.0f, 0.0f,  // green
                vertexC.x.toFloat(), vertexC.y.toFloat(), vertexC.z.toFloat(), 0.0f, 0.0f, 1.0f   // blue
            ))
        }

        // Create and bind VBO
        val buffer = FloatBuffer.wrap(vertexData.toFloatArray())
        val vboBuffer = IntBuffer.allocate(1)
        gl.glGenBuffers(1, vboBuffer)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer[0])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (vertexData.size * 4).toLong(), buffer, GL2.GL_STATIC_DRAW)
        vbo = vboBuffer[0]

        // Create and bind VAO
        val vaoBuffer = IntBuffer.allocate(1)
        gl.glGenVertexArrays(1, vaoBuffer)
        gl.glBindVertexArray(vaoBuffer[0])
        vao = vaoBuffer[0]

        // Enable and specify the layout of vertex data
        // Position attribute (location = 0)
        gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 6 * 4, 0)
        gl.glEnableVertexAttribArray(0)

        // Color attribute (location = 1)
        gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 6 * 4, 3 * 4)
        gl.glEnableVertexAttribArray(1)

        // Unbind the VAO and VBO
        gl.glBindVertexArray(0)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0)
    }

    // Render the mesh
    fun render(gl: GL2) {
        if (!isInitialized) {
            initializeBuffers(gl)
        }

        // Bind VAO and render
        gl.glBindVertexArray(vao)
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, mesh.triangles.size * 3)
        gl.glBindVertexArray(0)
    }

    // Cleanup OpenGL resources
    fun deleteBuffers(gl: GL2) {
        if (vbo != 0) {
            gl.glDeleteBuffers(1, IntBuffer.wrap(intArrayOf(vbo)))
            vbo = 0
        }
        if (vao != 0) {
            gl.glDeleteVertexArrays(1, IntBuffer.wrap(intArrayOf(vao)))
            vao = 0
        }
    }
}