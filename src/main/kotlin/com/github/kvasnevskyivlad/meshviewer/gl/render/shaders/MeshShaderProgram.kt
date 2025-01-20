package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext
import com.jogamp.opengl.GL2
import org.joml.Matrix4f
import org.joml.Vector3f

class MeshShaderProgram(gl: GL2, vertexShaderSource: String, fragmentShaderSource: String)
    : ShaderProgram(gl, vertexShaderSource, fragmentShaderSource), IShaderProgram {

    private var viewLocation: Int = 0
    private var projectionLocation: Int = 0
    private var eyePositionLocation: Int = 0
    //private var lightPositionLocation: Int = 0

    override fun registerLocations() {
        viewLocation = gl.glGetUniformLocation(id, "view")
        projectionLocation = gl.glGetUniformLocation(id, "projection")
        eyePositionLocation = gl.glGetUniformLocation(id, "eyePosition")
        //lightPositionLocation = gl.glGetUniformLocation(id, "lightPosition")
    }

    override fun setLocations(context: SceneContext) {
        setUniformMatrix4(viewLocation, context.camera.view)
        setUniformMatrix4(projectionLocation, context.camera.projection)
        setUniform3(eyePositionLocation, context.camera.eyePosition)
        //setUniform3(lightPositionLocation, context.lightPosition)
    }
}