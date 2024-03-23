package com.github.kvasnevskyivlad.meshviewer.actions
import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase

internal class AddMeshToViewerAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        val selectedNode =  XDebuggerTreeActionBase.getSelectedNode(e.dataContext)
        val mesh = Mesh.createFromJson(selectedNode?.rawValue!!.substring(1, selectedNode.rawValue!!.length - 1))
        Messages.showMessageDialog(e.project, selectedNode.rawValue, "Mesh Viewer", Messages.getInformationIcon())
    }
}