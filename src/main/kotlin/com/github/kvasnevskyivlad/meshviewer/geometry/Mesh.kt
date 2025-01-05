package com.github.kvasnevskyivlad.meshviewer.geometry

import com.jogamp.opengl.GL2
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.FloatBuffer
import java.nio.IntBuffer

@Serializable
data class Point(val x: Double, val y: Double, val z: Double)

@Serializable
data class Triangle(val a: Int, val b: Int, val c: Int)

@Serializable
class Mesh(val vertices: List<Point>, val triangles: List<Triangle>) {
    private var vbo: Int = 0
    var vao: Int = 0

    companion object {
        fun createFromJson(json: String): Mesh {
            return Json.decodeFromString(json)
        }
    }

    // Initialize the VBO and VAO once
    fun initializeBuffers(gl: GL2) {
        // Check if VBO and VAO are already initialized
        if (vbo != 0 || vao != 0) return

        // Combine vertex positions and colors into one array
        val vertexData = mutableListOf<Float>()
        triangles.forEach { triangle ->
            val vertexA = vertices[triangle.a]
            val vertexB = vertices[triangle.b]
            val vertexC = vertices[triangle.c]

            // Format: [x, y, z, r, g, b]
            vertexData.addAll(listOf(
                vertexA.x.toFloat(), vertexA.y.toFloat(), vertexA.z.toFloat(), 1.0f, 0.0f, 0.0f,  // red
                vertexB.x.toFloat(), vertexB.y.toFloat(), vertexB.z.toFloat(), 0.0f, 1.0f, 0.0f,  // green
                vertexC.x.toFloat(), vertexC.y.toFloat(), vertexC.z.toFloat(), 0.0f, 0.0f, 1.0f   // blue
            ))
        }

        // Create VBO (Vertex Buffer Object)
        val buffer = FloatBuffer.wrap(vertexData.toFloatArray())
        val vbo = IntBuffer.allocate(1)
        gl.glGenBuffers(1, vbo)
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0])
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (vertexData.size * 4).toLong(), buffer, GL2.GL_STATIC_DRAW)
        this.vbo = vbo[0]

        // Create VAO (Vertex Array Object)
        val vao = IntBuffer.allocate(1)
        gl.glGenVertexArrays(1, vao)
        gl.glBindVertexArray(vao[0])
        this.vao = vao[0]

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

    // Delete the buffers when the mesh is no longer needed
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

    fun toJson() : String {
        return Json.encodeToString(this)
    }
}