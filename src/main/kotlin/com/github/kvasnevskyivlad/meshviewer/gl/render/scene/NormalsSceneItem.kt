package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.github.kvasnevskyivlad.meshviewer.algorithms.NormalComputationAlgorithm
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.gl.render.shaders.ShadersProvider
import com.jogamp.opengl.GL2
import java.nio.FloatBuffer
import java.nio.IntBuffer

class NormalsSceneItem(private val mesh: Mesh, private val shaders: ShadersProvider) : ISceneItem {
    private var vboBuffer: IntBuffer? = null
    private var vaoBuffer: IntBuffer? = null

    // This value controls the length of the normal lines
    private val normalLineLength = 0.3f

    private fun initBuffers(gl: GL2) {
        val vertexData = mutableListOf<Float>()
        val colorData = mutableListOf<Float>()
        val lineData = mutableListOf<Float>()

        // Compute normals
        val normals = NormalComputationAlgorithm(mesh).computeSmoothShadingNormals()

        mesh.vertices.forEachIndexed { index, vertex ->
            // Add positions for the line (each vertex position + normal direction)
            vertexData.addAll(listOf(
                vertex.x, vertex.y, vertex.z,
                vertex.x + normals[index]!!.x * normalLineLength,
                vertex.y + normals[index]!!.y * normalLineLength,
                vertex.z + normals[index]!!.z * normalLineLength
            ))

            // Color for the normal lines (yellow for example)
            colorData.addAll(listOf(
                0.0f, 0.0f, 0.0f,  // black for normals
                0.0f, 0.0f, 0.0f   // black for normals
            ))

            // Line data: start and end points for each normal
            lineData.addAll(listOf(
                vertex.x, vertex.y, vertex.z,
                vertex.x + normals[index]!!.x * normalLineLength,
                vertex.y + normals[index]!!.y * normalLineLength,
                vertex.z + normals[index]!!.z * normalLineLength
            ))
        }

        // Create and bind VBO (Vertex Buffer Object)
        val vertexBuffer = FloatBuffer.wrap(vertexData.toFloatArray())
        val colorBuffer = FloatBuffer.wrap(colorData.toFloatArray())
        val lineBuffer = FloatBuffer.wrap(lineData.toFloatArray()) // Buffer for lines

        vboBuffer = IntBuffer.allocate(3) // Position, color, line data
        gl.glGenBuffers(3, vboBuffer)

        // Position VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![0])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (vertexData.size * 4).toLong(), vertexBuffer, GL2.GL_STATIC_DRAW)

        // Color VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![1])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (colorData.size * 4).toLong(), colorBuffer, GL2.GL_STATIC_DRAW)

        // Line data VBO (for normals visualization)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![2])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (lineData.size * 4).toLong(), lineBuffer, GL2.GL_STATIC_DRAW)

        // Create and bind VAO (Vertex Array Object)
        vaoBuffer = IntBuffer.allocate(1)
        gl.glGenVertexArrays(1, vaoBuffer)
        gl.glBindVertexArray(vaoBuffer!![0])

        // Enable and specify the layout of vertex data
        // Position Attribute (location = 0)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![0])
        gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(0)

        // Color Attribute (location = 1)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![1])
        gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(1)

        // Line data Attribute (location = 2)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![2])
        gl.glVertexAttribPointer(2, 3, GL2.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(2)

        // Unbind the VAO and VBO
        gl.glBindVertexArray(0)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0)
    }

    override fun render(gl: GL2, sceneContext: ISceneContext) {
        // Make sure to initialize OpenGL buffers before rendering
        if (vaoBuffer == null || vboBuffer == null) {
            initBuffers(gl)
        }

        shaders.normals.execute(sceneContext) {
            gl.glBindVertexArray(vaoBuffer!![0])
            // Draw lines to visualize normals (using GL_LINES for line rendering)
            gl.glDrawArrays(GL2.GL_LINES, 0, mesh.vertices.size * 2)
            gl.glBindVertexArray(0)
        }
    }

    override fun dispose(gl: GL2) {
        // Delete VBOs
        vboBuffer?.let { buffer ->
            gl.glDeleteBuffers(buffer.capacity(), buffer)
            vboBuffer = null
        }

        // Delete vao
        vaoBuffer?.let { buffer ->
            gl.glDeleteVertexArrays(buffer.capacity(), buffer)
            vaoBuffer = null
        }
    }
}