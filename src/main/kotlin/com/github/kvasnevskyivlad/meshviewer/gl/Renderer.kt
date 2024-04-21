package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.geometry.MeshExamples
import com.github.kvasnevskyivlad.meshviewer.geometry.Point
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import javax.vecmath.Matrix4f
import kotlin.math.sqrt


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
        render(gl, MeshExamples.cube)
    }

    private fun render(gl: GL2, mesh: Mesh) {
        mesh.triangles.forEach { triangle ->
            val vertexA = mesh.vertices[triangle.a]
            val vertexB = mesh.vertices[triangle.b]
            val vertexC = mesh.vertices[triangle.c]

            gl.glBegin(GL.GL_TRIANGLES)

            // Calculate surface normal for lighting if needed
            val normal = calculateSurfaceNormal(vertexA, vertexB, vertexC)
            gl.glNormal3d(normal.x, normal.y, normal.z)

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

    private fun calculateSurfaceNormal(vertexA: Point, vertexB: Point, vertexC: Point): Point {
        // Calculate vectors for two edges of the triangle
        val edge1 = Point(vertexB.x - vertexA.x, vertexB.y - vertexA.y, vertexB.z - vertexA.z)
        val edge2 = Point(vertexC.x - vertexA.x, vertexC.y - vertexA.y, vertexC.z - vertexA.z)

        // Calculate cross product of edge1 and edge2 to get the normal vector
        val normal = Point(
            y = edge1.z * edge2.y - edge1.y * edge2.z,
            z = edge1.x * edge2.z - edge1.z * edge2.x,
            x = edge1.y * edge2.x - edge1.x * edge2.y
        )

        // Normalize the normal vector
        val length = sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z)
        return Point(normal.x / length, normal.y / length, normal.z / length)
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

