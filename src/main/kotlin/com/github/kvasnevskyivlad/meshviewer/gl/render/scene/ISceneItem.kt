package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.jogamp.opengl.GL2

interface ISceneItem {

    val id: Int
    var visible: Boolean
    fun render(gl: GL2, sceneContext: ISceneContext)
    fun dispose(gl: GL2)
}