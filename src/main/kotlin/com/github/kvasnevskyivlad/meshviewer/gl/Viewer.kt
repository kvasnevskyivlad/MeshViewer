package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import com.github.kvasnevskyivlad.meshviewer.gl.camera.Camera
import com.github.kvasnevskyivlad.meshviewer.gl.camera.CameraController
import com.github.kvasnevskyivlad.meshviewer.gl.render.Renderer
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

class Viewer : JBPanel<JBPanel<*>>() {

    private val glCanvas = GLCanvas()
    private val animator = Animator(glCanvas)
    private val camera = Camera()
    private val cameraController = CameraController(camera)
    private val renderer = Renderer(camera)

    //private var meshListModel = DefaultListModel<String>()

    init {

        layout = GridBagLayout()

        val label = JBLabel("Meshes:")
        //val jbList = JBList(meshListModel)

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

        // Define constraints for the jbList
        val jbListConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            fill = GridBagConstraints.HORIZONTAL
        }

        // Add jbList to the main panel with defined constraints
        //add(jbList, jbListConstraints)

        glCanvas.addGLEventListener(renderer)
        glCanvas.addMouseListener(cameraController)
        glCanvas.addMouseMotionListener(cameraController)
        glCanvas.addMouseWheelListener(cameraController)

        animator.start()
    }

    fun setSingle(mesh: Mesh) {
        renderer.scene.setSingle(mesh)
    }
}