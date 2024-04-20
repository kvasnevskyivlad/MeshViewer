package com.github.kvasnevskyivlad.meshviewer.gl

import com.github.kvasnevskyivlad.meshviewer.geometry.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseMotionListener
class Camera() : MouseMotionListener, MouseListener {
    var rotateX = 0f
    var rotateY = 0f

    private var prevMouseX = 0
    private var prevMouseY = 0

    override fun mouseDragged(e: MouseEvent?) {
        val dx = e!!.x - prevMouseX
        val dy = e.y - prevMouseY
        rotateY += dx * 0.5f
        rotateX += dy * 0.5f
        prevMouseX = e.x
        prevMouseY = e.y
    }

    override fun mouseMoved(e: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun mouseClicked(e: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun mousePressed(e: MouseEvent?) {
        prevMouseX = e!!.x
        prevMouseY = e.y
    }

    override fun mouseReleased(e: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun mouseEntered(e: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun mouseExited(e: MouseEvent?) {
        TODO("Not yet implemented")
    }
}