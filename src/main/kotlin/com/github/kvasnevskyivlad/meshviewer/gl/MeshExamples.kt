package com.github.kvasnevskyivlad.meshviewer.gl

import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2

class MeshExamples {

    companion object {

        fun renderTriangle(gl: GL2) {
            // Render the triangle
            gl.glBegin(GL.GL_TRIANGLES)
            gl.glColor3f(1.0f, 0.0f, 0.0f)
            gl.glVertex3f(-1.0f, -1.0f, 0.0f)
            gl.glColor3f(0.0f, 1.0f, 0.0f)
            gl.glVertex3f(1.0f, -1.0f, 0.0f)
            gl.glColor3f(0.0f, 0.0f, 1.0f)
            gl.glVertex3f(0.0f, 1.0f, 0.0f)
            gl.glEnd()
        }

        fun renderCube(gl: GL2) {
            // Define the vertices of the cube
            val vertices = arrayOf(
                floatArrayOf(-1.0f, -1.0f, -1.0f), // 0
                floatArrayOf(1.0f, -1.0f, -1.0f),  // 1
                floatArrayOf(1.0f, 1.0f, -1.0f),   // 2
                floatArrayOf(-1.0f, 1.0f, -1.0f),  // 3
                floatArrayOf(-1.0f, -1.0f, 1.0f),  // 4
                floatArrayOf(1.0f, -1.0f, 1.0f),   // 5
                floatArrayOf(1.0f, 1.0f, 1.0f),    // 6
                floatArrayOf(-1.0f, 1.0f, 1.0f)    // 7
            )

            // Define the indices for drawing triangles
            val indices = intArrayOf(
                // Front face
                0, 1, 2, 2, 3, 0,
                // Back face
                4, 5, 6, 6, 7, 4,
                // Top face
                3, 2, 6, 6, 7, 3,
                // Bottom face
                0, 1, 5, 5, 4, 0,
                // Left face
                0, 3, 7, 7, 4, 0,
                // Right face
                1, 2, 6, 6, 5, 1
            )

            // Render the cube
            gl.glBegin(GL.GL_TRIANGLES)
            for (i in indices.indices) {
                val vertexIndex = indices[i]
                val vertex = vertices[vertexIndex]
                gl.glColor3f(1.0f, 0.0f, 0.0f)
                gl.glVertex3f(vertex[0], vertex[1], vertex[2])
            }
            gl.glEnd()
        }
    }
}