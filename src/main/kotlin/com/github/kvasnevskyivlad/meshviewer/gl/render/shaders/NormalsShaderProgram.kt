package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItemContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext
import com.jogamp.opengl.GL2

class NormalsShaderProgram(gl: GL2, vertexShaderSource: String, fragmentShaderSource: String)
    : ShaderProgram(gl, vertexShaderSource, fragmentShaderSource), IShaderProgram {

    private var viewLocation: Int = 0
    private var projectionLocation: Int = 0

    override fun registerUniformLocations() {
        viewLocation = gl.glGetUniformLocation(id, "view")
        projectionLocation = gl.glGetUniformLocation(id, "projection")
    }

    override fun setUniformValues(sceneContext: ISceneContext, itemContext: ISceneItemContext) {
        setUniformMatrix4(viewLocation, sceneContext.view)
        setUniformMatrix4(projectionLocation, sceneContext.projection)
    }
}