package com.github.kvasnevskyivlad.meshviewer.actions
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class AddMeshToViewerAction : AnAction() {

    @Serializable
    data class Point(val x: Double, val y: Double, val z: Double)

    @Serializable
    data class Triangle(val a: Int, val b: Int, val c: Int)

    @Serializable
    data class Mesh(val vertices: List<Point>, val triangles: List<Triangle>)

    // Function to parse JSON string to Mesh object
    fun parseJsonToMesh(json: String): Mesh {
        return Json.decodeFromString(json)
    }

    override fun actionPerformed(e: AnActionEvent) {

        val selectedNode =  XDebuggerTreeActionBase.getSelectedNode(e.dataContext)
        val mesh = parseJsonToMesh(selectedNode?.rawValue!!.substring(1, selectedNode?.rawValue!!.length - 1))
        Messages.showMessageDialog(e.project, selectedNode?.rawValue, "Mesh Viewer", Messages.getInformationIcon())
    }
}