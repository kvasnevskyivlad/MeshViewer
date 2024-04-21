package com.github.kvasnevskyivlad.meshviewer.gl.camera

import java.awt.event.*

class CameraController(private val camera: Camera) : MouseMotionListener, MouseListener, MouseWheelListener {

    override fun mousePressed(e: MouseEvent) = camera.startDrag(e.x, e.y)
    override fun mouseDragged(e: MouseEvent) = camera.drag(e.x, e.y)
    override fun mouseReleased(e: MouseEvent) = camera.endDrag()

    override fun mouseMoved(e: MouseEvent) {
        TODO("Not yet implemented")
    }

    override fun mouseClicked(e: MouseEvent) {
        TODO("Not yet implemented")
    }

    override fun mouseEntered(e: MouseEvent) {
        TODO("Not yet implemented")
    }

    override fun mouseExited(e: MouseEvent) {
        TODO("Not yet implemented")
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        TODO("Not yet implemented")
    }
}