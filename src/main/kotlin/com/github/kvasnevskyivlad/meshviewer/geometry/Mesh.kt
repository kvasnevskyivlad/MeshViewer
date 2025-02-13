package com.github.kvasnevskyivlad.meshviewer.geometry

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Point(val x: Float, val y: Float, val z: Float)

@Serializable
data class Triangle(val a: Int, val b: Int, val c: Int)

@Serializable
class Mesh(
    val vertices: List<Point>,
    val triangles: List<Triangle>
) {
    companion object {
        fun createFromJson(json: String): Mesh {
            return Json.decodeFromString(json)
        }
    }

    fun toJson() : String {
        return Json.encodeToString(this)
    }
}