package com.github.kvasnevskyivlad.meshviewer.gl.render

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.geometry.MeshExamples
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.MeshSceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.NormalsSceneItem
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.SceneContext
import com.github.kvasnevskyivlad.meshviewer.gl.render.shaders.ShadersProvider
import com.jogamp.opengl.GL2
import org.joml.Vector3f

class Scene(private val camera: Camera, private val shaders: ShadersProvider) {

    private var lightPosition = Vector3f(1.2f, 1.0f, 2.0f)
    private var lightColor = Vector3f(1.0f, 1.0f, 1.0f)
    private val items = mutableListOf<ISceneItem>()
    private val context: SceneContext
        get() = SceneContext(camera, lightColor, lightPosition)


    fun render(gl: GL2) {
        items.forEach {
            it.render(gl, context)
        }
    }

    fun setSingle(mesh: Mesh) {
        // Mesh
        val item = MeshSceneItem(mesh, shaders);
        items.add(item)

//        // Normals
//        val normals = NormalsSceneItem(mesh, shaders)
//        items.add(normals)
    }

    fun clear() {
        items.clear()
    }

    fun remove(item: ISceneItem) {
        items.remove(item)
    }

    fun dispose(gl: GL2) {
        items.forEach { it.dispose(gl) }
        items.clear()
    }
}