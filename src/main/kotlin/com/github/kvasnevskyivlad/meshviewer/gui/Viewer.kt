package com.github.kvasnevskyivlad.meshviewer.gui

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.github.kvasnevskyivlad.meshviewer.gl.camera.CameraController
import com.github.kvasnevskyivlad.meshviewer.gl.render.Renderer
import com.github.kvasnevskyivlad.meshviewer.gl.render.scene.ISceneItem
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import javax.swing.*
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.table.AbstractTableModel

class Viewer : JBPanel<JBPanel<*>>() {

    private val glCanvas = GLCanvas()
    private val animator = Animator(glCanvas)
    private val camera = Camera()
    private val cameraController = CameraController(camera)
    private val renderer = Renderer(camera)

    private val meshListModel = SceneItemsTableModel()

    init {
        layout = GridBagLayout()

        val label = JBLabel("Meshes:")
        val meshTable = JTable(meshListModel)
        meshTable.columnModel.getColumn(1).cellEditor = DefaultCellEditor(JCheckBox())

        // Define constraints for the glCanvas
        val glCanvasConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            weightx = 1.0
            weighty = 1.0
            fill = GridBagConstraints.BOTH
        }

        // Add glCanvas to the main panel with defined constraints
        add(glCanvas, glCanvasConstraints)

        // Define constraints for the label
        val labelConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            fill = GridBagConstraints.HORIZONTAL
        }

        // Add label to the main panel with defined constraints
        add(label, labelConstraints)

        // Define constraints for the meshTable
        val tableConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            weightx = 1.0
            weighty = 1.0
            fill = GridBagConstraints.BOTH
        }

        // Add table to the main panel with defined constraints
        val scrollPane = JScrollPane(meshTable)
        add(scrollPane, tableConstraints)

        glCanvas.addGLEventListener(renderer)
        glCanvas.addMouseListener(cameraController)
        glCanvas.addMouseMotionListener(cameraController)
        glCanvas.addMouseWheelListener(cameraController)

        animator.start()
    }

    fun addMesh(mesh: Mesh) {
        // Add the mesh to the scene and model, using the updated add method
        val sceneItem = renderer.scene.add(mesh)
        meshListModel.add(sceneItem)
    }

    // Custom Table Model for meshes
    private class SceneItemsTableModel : AbstractTableModel() {
        private val columnNames = arrayOf("Mesh Name", "Visibility")
        private val items = mutableListOf<ISceneItem>()

        override fun getRowCount(): Int = items.size
        override fun getColumnCount(): Int = columnNames.size
        override fun getColumnName(column: Int): String = columnNames[column]

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val item = items[rowIndex]
            return when (columnIndex) {
                0 -> "Mesh - [${item.id}]" // Display mesh with id
                1 -> item.visible // Visibility status
                else -> ""
            }
        }

        override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
            val item = items[rowIndex]

            if (columnIndex == 1) {
                // Switch visibility
                item.visible = !item.visible
            }
        }

        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
            return columnIndex == 1 // Only the "Visible" column is editable
        }

        fun add(item: ISceneItem) {
            items.add(item)
            fireTableRowsInserted(items.size - 1, items.size - 1)
        }
    }
}
