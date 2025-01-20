package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.jogamp.opengl.GL2

interface ISceneItem {

    fun render(gl: GL2, context: SceneContext)
    fun dispose(gl: GL2)
}