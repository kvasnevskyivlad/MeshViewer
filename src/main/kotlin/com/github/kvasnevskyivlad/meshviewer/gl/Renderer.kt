package com.github.kvasnevskyivlad.meshviewer.gl

import com.jogamp.opengl.GL
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
        gl.glLoadIdentity()

        //gl.glTranslatef(0.0f, 0.0f, -6.0f)
        gl.glRotatef(camera.rotateX, 1.0f, 0.0f, 0.0f)
        gl.glRotatef(camera.rotateY, 0.0f, 1.0f, 0.0f)

        // Render your OpenGL content here
        gl.glBegin(GL.GL_TRIANGLES)
        gl.glColor3f(1.0f, 0.0f, 0.0f)
        gl.glVertex3f(-1.0f, -1.0f, 0.0f)
        gl.glColor3f(0.0f, 1.0f, 0.0f)
        gl.glVertex3f(1.0f, -1.0f, 0.0f)
        gl.glColor3f(0.0f, 0.0f, 1.0f)
        gl.glVertex3f(0.0f, 1.0f, 0.0f)
        gl.glEnd()
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        val gl = drawable.gl.gL2
        gl.glViewport(0, 0, width, height)
    }

    override fun dispose(drawable: GLAutoDrawable) {}
}