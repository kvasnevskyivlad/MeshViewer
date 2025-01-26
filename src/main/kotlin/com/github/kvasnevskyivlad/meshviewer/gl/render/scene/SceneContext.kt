package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import org.joml.Matrix4f
import org.joml.Vector3f

interface ISceneContext {
    val view: Matrix4f
    val projection: Matrix4f
    val eyePosition: Vector3f
    val lightColor: Vector3f
    val lightPosition : Vector3f
}

interface ISceneItemContext {
}

class SceneContext(
    private val camera: Camera,
    override val lightColor: Vector3f,
    override val lightPosition: Vector3f
) : ISceneContext {

    override val view: Matrix4f
        get() = camera.view

    override val projection: Matrix4f
        get() = camera.projection

    override val eyePosition: Vector3f
        get() = camera.eyePosition
}

class DefaultSceneItemContext(
) : ISceneItemContext

class MeshSceneItemContext(
    val color: Vector3f
) : ISceneItemContext