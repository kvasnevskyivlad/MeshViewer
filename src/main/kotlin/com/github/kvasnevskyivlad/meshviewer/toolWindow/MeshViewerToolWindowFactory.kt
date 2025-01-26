package com.github.kvasnevskyivlad.meshviewer.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.gl.Viewer
import com.github.kvasnevskyivlad.meshviewer.services.MeshService
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

class MeshViewerToolWindowFactory() : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MeshViewerToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MeshViewerToolWindow(toolWindow: ToolWindow) {

        private var viewer: Viewer = Viewer()
        private val service = toolWindow.project.service<MeshService>()

        init {

            // init list model
            service.meshes.forEach { mesh ->
                //meshListModel.addElement(mesh.toJson())
            }

            // add listener to react on mesh add event
            service.setMeshAddedListener { mesh: Mesh ->
                viewer.setSingle(mesh)
                //meshListModel.addElement(mesh.toJson())
            }
        }

        fun getContent() = JBPanel<JBPanel<*>>().apply {

            layout = GridBagLayout()

            // Define constraints for viewer
            val viewerConstraints = GridBagConstraints().apply {
                gridx = 0
                gridy = 0
                weightx = 1.0
                weighty = 1.0
                fill = GridBagConstraints.BOTH
            }

            add(viewer, viewerConstraints)
        }
    }
}
