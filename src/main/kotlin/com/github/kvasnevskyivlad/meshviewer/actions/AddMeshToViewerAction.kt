package com.github.kvasnevskyivlad.meshviewer.actions
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.services.MeshService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase

internal class AddMeshToViewerAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        val selectedNode =  XDebuggerTreeActionBase.getSelectedNode(e.dataContext)
        val json = selectedNode?.rawValue!!.substring(1, selectedNode.rawValue!!.length - 1)

        val service = e.project!!.service<MeshService>()
        service.addMesh(Mesh.createFromJson(json))
    }
}