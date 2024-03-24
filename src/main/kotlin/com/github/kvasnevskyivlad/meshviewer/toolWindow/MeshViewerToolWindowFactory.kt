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
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.GL
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import javax.swing.DefaultListModel
import com.jogamp.opengl.awt.GLCanvas
import java.awt.Dimension

class MeshViewerToolWindowFactory() : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MeshViewerToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MeshViewerToolWindow(toolWindow: ToolWindow) {

        private var glCanvas: GLCanvas? = null

        private val service = toolWindow.project.service<MeshService>()
        private var meshListModel = DefaultListModel<String>()

        init {

            // init list model
            service.meshes.forEach { mesh ->
                meshListModel.addElement(mesh.toJson())
            }

            // add listener to react on mesh add event
            service.setMeshAddedListener { mesh: Mesh ->
                meshListModel.addElement(mesh.toJson())
            }

            createOpenGLView()
        }

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val label = JBLabel("Meshes:")
            val jbList = JBList(meshListModel)

            add(label)
            add(jbList)
            add(glCanvas)

            //add(JButton(MyBundle.message("shuffle")).apply {
            //    addActionListener {
            //        //label.text = MyBundle.message("randomLabel", service.getRandomNumber())
            //    }
            //})
        }

        private fun createOpenGLView() {
            glCanvas = GLCanvas()
            glCanvas!!.addGLEventListener(object : GLEventListener {
                override fun init(drawable: GLAutoDrawable) {
                    val gl = drawable.gl.gL2
                    gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
                }

                override fun dispose(drawable: GLAutoDrawable) {}

                override fun display(drawable: GLAutoDrawable) {
                    val gl = drawable.gl.gL2
                    gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)

                    // Render your OpenGL content here
                    gl.glBegin(GL.GL_TRIANGLES)
                    gl.glColor3f(1.0f, 0.0f, 0.0f)
                    gl.glVertex3f(-1.0f, -1.0f, 0.0f)
                    gl.glColor3f(0.0f, 1.0f, 0.0f)
                    gl.glVertex3f(1.0f, -1.0f, 0.0f)
                    gl.glColor3f(0.0f, 0.0f, 1.0f)
                    gl.glVertex3f(0.0f, 1.0f, 0.0f)
                    gl.glEnd()
                }

                override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
                    val gl = drawable.gl.gL2
                    gl.glViewport(0, 0, width, height)
                }
            })

            glCanvas!!.preferredSize = Dimension(400, 400)
        }
    }
}
