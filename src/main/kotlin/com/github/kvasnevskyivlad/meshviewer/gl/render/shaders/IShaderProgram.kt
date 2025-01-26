package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItemContext

interface IShaderProgram {

    fun execute(sceneContext: ISceneContext, action: () -> Unit)
    fun execute(sceneContext: ISceneContext, itemContext: ISceneItemContext, action: () -> Unit)
}