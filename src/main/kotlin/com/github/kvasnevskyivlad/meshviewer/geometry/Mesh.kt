package com.github.kvasnevskyivlad.meshviewer.geometry

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Point(val x: Double, val y: Double, val z: Double)

@Serializable
data class Triangle(val a: Int, val b: Int, val c: Int)

@Serializable
class Mesh(val vertices: List<Point>, val triangles: List<Triangle>) {
    companion object {
        fun createFromJson(json: String): Mesh {
            return Json.decodeFromString(json)
        }
    }
}