package com.github.kvasnevskyivlad.meshviewer.gl.render.scene

import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import org.joml.Vector3f

class SceneContext(
    val camera: Camera,
    val lightPosition: Vector3f
)
