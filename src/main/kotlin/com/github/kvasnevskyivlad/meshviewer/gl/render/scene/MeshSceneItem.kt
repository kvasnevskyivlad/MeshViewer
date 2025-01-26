package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.github.kvasnevskyivlad.meshviewer.algorithms.NormalComputationAlgorithm
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.gl.render.shaders.ShadersProvider
import com.jogamp.opengl.GL2
import org.joml.Vector3f
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MeshSceneItem(private val mesh: Mesh, private val shaders: ShadersProvider) : ISceneItem{
    private var vboBuffer: IntBuffer? = null
    private var vaoBuffer: IntBuffer? = null
    private var context: MeshSceneItemContext = MeshSceneItemContext(Vector3f(0.5f, 0.5f, 0.5f))

    private fun initBuffers(gl: GL2) {
        val vertexData = mutableListOf<Float>()
        val normalData = mutableListOf<Float>()

        val normals = NormalComputationAlgorithm(mesh).computeSmoothShadingNormals()

        mesh.triangles.forEach { triangle ->
            val vertexA = mesh.vertices[triangle.a]
            val vertexB = mesh.vertices[triangle.b]
            val vertexC = mesh.vertices[triangle.c]

            // Add positions and normals (for each vertex of the triangle)
            vertexData.addAll(listOf(
                vertexA.x, vertexA.y, vertexA.z,
                vertexB.x, vertexB.y, vertexB.z,
                vertexC.x, vertexC.y, vertexC.z,
            ))

            // Add normals (fetch the normal for each vertex from the map)
            val normalA = normals[triangle.a] ?: Vector3f(0f, 0f, 0f)
            val normalB = normals[triangle.b] ?: Vector3f(0f, 0f, 0f)
            val normalC = normals[triangle.c] ?: Vector3f(0f, 0f, 0f)

            normalData.addAll(listOf(
                normalA.x, normalA.y, normalA.z,
                normalB.x, normalB.y, normalB.z,
                normalC.x, normalC.y, normalC.z
            ))
        }

        // Create and bind VBO (Vertex Buffer Object)
        val vertexBuffer = FloatBuffer.wrap(vertexData.toFloatArray())
        val normalBuffer = FloatBuffer.wrap(normalData.toFloatArray()) // Buffer for normals

        vboBuffer = IntBuffer.allocate(2) // Position, normal
        gl.glGenBuffers(2, vboBuffer)

        // Position VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![0])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (vertexData.size * 4).toLong(), vertexBuffer, GL2.GL_STATIC_DRAW)

        // Normal VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![1])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (normalData.size * 4).toLong(), normalBuffer, GL2.GL_STATIC_DRAW)

        // Create and bind VAO (Vertex Array Object)
        vaoBuffer = IntBuffer.allocate(1)
        gl.glGenVertexArrays(1, vaoBuffer)
        gl.glBindVertexArray(vaoBuffer!![0])

        // Enable and specify the layout of vertex data
        // Position Attribute (location = 0)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![0])
        gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(0)

        // Normal Attribute (location = 1)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboBuffer!![1])
        gl.glVertexAttribPointer(1, 3, GL2.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(1)

        // Unbind the VAO and VBO
        gl.glBindVertexArray(0)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0)
    }

    override fun render(gl: GL2, sceneContext: ISceneContext) {
        // Make sure to initialize OpenGL buffers before rendering
        if (vaoBuffer == null || vboBuffer == null) {
            initBuffers(gl)
        }

        shaders.mesh.execute(sceneContext, context) {
            gl.glBindVertexArray(vaoBuffer!![0])
            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, mesh.triangles.size * 3)
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