package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext

interface IShaderProgram {
    // Executes an action inside the activate/deactivate lifecycle
    fun execute(context: SceneContext, action: () -> Unit)
}