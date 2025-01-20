package com.github.kvasnevskyivlad.meshviewer.gl.render

import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext
import com.jogamp.opengl.GL2
import org.joml.Vector3f

class Scene(private val camera: Camera) {

    private var lightPosition = Vector3f(0.0f, 10.0f, 10.0f)
    private val items = mutableListOf<ISceneItem>()
    private val context: SceneContext
        get() = SceneContext(camera, lightPosition)

    fun render(gl: GL2) {
        items.forEach {
            it.render(gl, context)
        }
    }

    fun add(item: ISceneItem) {
        items.add(item)
    }

    fun remove(item: ISceneItem) {
        items.remove(item)
    }

    fun dispose(gl: GL2) {
        items.forEach { it.dispose(gl) }
        items.clear()
    }
}