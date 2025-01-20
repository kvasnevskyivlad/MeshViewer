package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import org.joml.Matrix4f
import org.joml.Vector3f
import java.nio.ByteBuffer
import java.nio.IntBuffer

abstract class ShaderProgram(protected val gl: GL2, vertexShaderSource: String, fragmentShaderSource: String) : IShaderProgram{
    protected var id: Int = 0

    init {
        load(vertexShaderSource, fragmentShaderSource)
    }

    abstract fun registerLocations()

    private fun load(vertexShaderSource: String, fragmentShaderSource: String) {
        val vertexShader = compileShader(GL2.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShader = compileShader(GL2.GL_FRAGMENT_SHADER, fragmentShaderSource)

        id = gl.glCreateProgram()

        // Attach shaders to the program
        gl.glAttachShader(id, vertexShader)
        gl.glAttachShader(id, fragmentShader)

        // Link the program
        gl.glLinkProgram(id)

        // Check for linking errors
        val linkStatus = IntBuffer.allocate(1)
        gl.glGetProgramiv(id, GL2.GL_LINK_STATUS, linkStatus)
        if (linkStatus[0] == GL.GL_FALSE) {
            val logLength = IntBuffer.allocate(1)
            gl.glGetProgramiv(id, GL2.GL_INFO_LOG_LENGTH, logLength)
            val log = ByteBuffer.allocate(logLength[0])
            gl.glGetProgramInfoLog(id, logLength[0], null, log)
            println("Shader linking failed: ${String(log.array())}")
        }

        // Register locations
        registerLocations()

        // Clean up
        gl.glDeleteShader(vertexShader)
        gl.glDeleteShader(fragmentShader)
    }

    private fun compileShader(type: Int, source: String): Int {
        val shader = gl.glCreateShader(type)
        gl.glShaderSource(shader, 1, arrayOf(source), null)
        gl.glCompileShader(shader)

        // Check for compilation errors
        val compileStatus = IntBuffer.allocate(1)
        gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, compileStatus)
        if (compileStatus[0] == GL.GL_FALSE) {
            val logLength = IntBuffer.allocate(1)
            gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, logLength)
            val log = ByteBuffer.allocate(logLength[0])
            gl.glGetProgramInfoLog(id, logLength[0], null, log)
            println("Shader compilation failed: ${String(log.array())}")
        }

        return shader
    }

    override fun execute(context: SceneContext, action: () -> Unit) {
        activate()
        try {
            setLocations(context)
            action()
        } finally {
            deactivate()
        }
    }

    abstract fun setLocations(context: SceneContext)

    private fun activate() {
        gl.glUseProgram(id)
    }

    private fun deactivate() {
        gl.glUseProgram(0)
    }

    protected fun setUniformMatrix4(location: Int, matrix: Matrix4f) {
        val array = FloatArray(16)
        matrix.get(array)
        gl.glUniformMatrix4fv(location, 1, false, array, 0)
    }

    protected fun setUniform3(location: Int, value: Vector3f) {
        gl.glUniform3f(location, value.x, value.y, value.z)
    }
}