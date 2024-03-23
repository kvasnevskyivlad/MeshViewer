package com.github.kvasnevskyivlad.meshviewer.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.kvasnevskyivlad.meshviewer.MyBundle
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.services.MeshService
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBList
import javax.swing.DefaultListModel
import javax.swing.JButton


class MeshViewerToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MeshViewerToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MeshViewerToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MeshService>()
        private var meshListModel = DefaultListModel<String>()

        init {
            service.setMeshAddedListener { mesh: Mesh ->
                meshListModel.addElement(mesh.toJson())
            }
        }

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val label = JBLabel("Meshes:")
            val jbList = JBList(meshListModel)

            add(label)
            add(jbList)

            //add(JButton(MyBundle.message("shuffle")).apply {
            //    addActionListener {
            //        //label.text = MyBundle.message("randomLabel", service.getRandomNumber())
            //    }
            //})
        }
    }
}
