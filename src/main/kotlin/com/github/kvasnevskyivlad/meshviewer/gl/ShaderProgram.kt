package com.github.kvasnevskyivlad.meshviewer.gl

import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.joml.Matrix4f

class ShaderProgram(private val gl: GL2, vertexShaderSource: String, fragmentShaderSource: String) {
    private var id: Int = 0
    private var transformLocation: Int = 0

    init {
        load(vertexShaderSource, fragmentShaderSource)
    }

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

        transformLocation = gl.glGetUniformLocation(id, "transform")

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

    fun use() {
        gl.glUseProgram(id)
    }

    fun setTransform(matrix: Matrix4f) {
        // Create a float array to hold the matrix values
        val array = FloatArray(16)

        // Get the matrix values in column-major order
        matrix.get(array)

        gl.glUniformMatrix4fv(transformLocation, 1, false, array, 0)
    }
}