package com.github.kvasnevskyivlad.meshviewer.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.kvasnevskyivlad.meshviewer.MyBundle
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import java.util.*

@Service(Service.Level.PROJECT)
class MeshService(project: Project) {

    private val meshList = mutableListOf<Mesh>()
    private var meshAddedListener: ((Mesh) -> Unit)? = null

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    val meshes: List<Mesh>
        get() = Collections.unmodifiableList(meshList)

    fun addMesh(mesh: Mesh) {
        meshList.add(mesh)
        meshAddedListener?.invoke(mesh)
    }

    fun setMeshAddedListener(listener: (Mesh) -> Unit) {
        meshAddedListener = listener
    }
}
