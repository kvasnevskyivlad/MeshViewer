package com.github.kvasnevskyivlad.meshviewer.gl.render

import com.github.kvasnevskyivlad.meshviewer.geometry.MeshExamples
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.MeshSceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.NormalsSceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.shaders.ShadersProvider
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener

class Renderer(private val camera: Camera) : GLEventListener {
    private lateinit var scene: Scene

    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)

        // Enable depth testing and backface culling
        gl.glEnable(GL2.GL_DEPTH_TEST)     // Enable depth testing
        gl.glEnable(GL2.GL_CULL_FACE)      // Enable backface culling
        gl.glCullFace(GL2.GL_BACK)         // Cull the back faces
        gl.glFrontFace(GL2.GL_CCW)         // Set counter-clockwise winding as front-facing

        // Initialize shaders provider
        val shaders = ShadersProvider(gl)

        // Initialize scene
        scene = Scene(camera)

        // Add mesh item to scene
        val mesh = MeshSceneItem(gl, MeshExamples.tetrahedron, shaders)
        scene.add(mesh)

        // Add mesh normals to scene
        val normals = NormalsSceneItem(gl, MeshExamples.tetrahedron, shaders)
        scene.add(normals)
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)

        // Render scene
        scene.render(gl)

        // Draw axes
        drawAxes(gl)
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        val gl = drawable.gl.gL2
        camera.resize(gl, width, height)
    }

    override fun dispose(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        scene.dispose(gl)
    }

    private fun drawAxes(gl: GL2) {
        // Store current matrix (modelview) state
        gl.glPushMatrix()

        // Move the axes to the bottom-right corner of the screen
        gl.glTranslatef(0.8f, -0.8f, 0.0f)  // Adjust this to move the axes to the bottom-right corner

        // Apply camera transformation (rotation and translation)
        val array = FloatArray(16)
        camera.rotation.get(array)
        gl.glMultMatrixf(array, 0)  // Apply camera's rotation

        // Draw the X axis (red)
        gl.glColor3f(1.0f, 0.0f, 0.0f)  // Red
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(0.2f, 0.0f, 0.0f)
        gl.glEnd()

        // Draw the Y axis (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f)  // Green
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(0.0f, 0.2f, 0.0f)
        gl.glEnd()

        // Draw the Z axis (blue)
        gl.glColor3f(0.0f, 0.0f, 1.0f)  // Blue
        gl.glBegin(GL.GL_LINES)
        gl.glVertex3f(0.0f, 0.0f, 0.0f)
        gl.glVertex3f(0.0f, 0.0f, 0.2f)
        gl.glEnd()

        // Restore the previous matrix state
        gl.glPopMatrix()
    }
}

