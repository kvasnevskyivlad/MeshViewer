package com.github.kvasnevskyivlad.meshviewer.gl.camera

import java.awt.event.*

class CameraController(private val camera: Camera) : MouseMotionListener, MouseListener, MouseWheelListener {

    override fun mousePressed(e: MouseEvent) = camera.startDrag(e.x, e.y, getDragMode(e))
    override fun mouseDragged(e: MouseEvent) = camera.drag(e.x, e.y)
    override fun mouseReleased(e: MouseEvent) = camera.endDrag()

    override fun mouseMoved(e: MouseEvent) {
    }

    override fun mouseClicked(e: MouseEvent) {
    }

    override fun mouseEntered(e: MouseEvent) {
    }

    override fun mouseExited(e: MouseEvent) {
    } 

    override fun mouseWheelMoved(e: MouseWheelEvent) {

        // Determine the direction and magnitude of the mouse wheel movement
        val wheelRotation = e.preciseWheelRotation
        val zoomFactor = if (wheelRotation < 0) 1.1f else 0.9f

        // Update the zoom level of the camera
        camera.zoom(zoomFactor)
    }

    private fun getDragMode(e: MouseEvent): CameraDragMode {
        return when (e.button) {
            MouseEvent.BUTTON2 -> CameraDragMode.PAN // Middle button is pressed
            MouseEvent.BUTTON3 -> CameraDragMode.ROTATE // Right button is pressed
            else -> CameraDragMode.NONE // No drag operation for other buttons
        }
    }
}