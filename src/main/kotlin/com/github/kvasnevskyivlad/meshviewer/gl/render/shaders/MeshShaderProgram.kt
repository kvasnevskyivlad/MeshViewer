package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItemContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.MeshSceneItemContext
import com.jogamp.opengl.GL2

class MeshShaderProgram(gl: GL2, vertexShaderSource: String, fragmentShaderSource: String)
    : ShaderProgram(gl, vertexShaderSource, fragmentShaderSource), IShaderProgram {

    private var viewLocation: Int = 0
    private var projectionLocation: Int = 0
    private var eyePositionLocation: Int = 0
    private var lightColorLocation: Int = 0
    private var lightPositionLocation: Int = 0
    private var objectColorLocation: Int = 0

    override fun registerUniformLocations() {
        viewLocation = gl.glGetUniformLocation(id, "view")
        projectionLocation = gl.glGetUniformLocation(id, "projection")
        eyePositionLocation = gl.glGetUniformLocation(id, "eyePosition")
        lightColorLocation = gl.glGetUniformLocation(id, "lightColor")
        lightPositionLocation = gl.glGetUniformLocation(id, "lightPosition")
        objectColorLocation = gl.glGetUniformLocation(id, "objectColor")
    }

    override fun setUniformValues(sceneContext: ISceneContext, itemContext: ISceneItemContext) {
        // Scene
        setUniformMatrix4(viewLocation, sceneContext.view)
        setUniformMatrix4(projectionLocation, sceneContext.projection)
        setUniform3(eyePositionLocation, sceneContext.eyePosition)
        setUniform3(lightPositionLocation, sceneContext.lightPosition)
        setUniform3(lightColorLocation, sceneContext.lightColor)

        // Item
        if (itemContext is MeshSceneItemContext) {
            setUniform3(objectColorLocation, itemContext.color)
        }
    }
}