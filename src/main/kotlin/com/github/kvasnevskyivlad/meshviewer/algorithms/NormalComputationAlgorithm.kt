package com.github.kvasnevskyivlad.meshviewer.algorithms

import com.github.kvasnevskyivlad.meshviewer.geometry.Mesh
import org.joml.Vector3f

class NormalComputationAlgorithm(private val mesh: Mesh) {

    fun computeFlatShadingNormals(): List<Vector3f> {
        val flatNormals = mutableListOf<Vector3f>()

        // Calculate a flat normal for each triangle
        mesh.triangles.forEach { triangle ->
            val a = mesh.vertices[triangle.a]
            val b = mesh.vertices[triangle.b]
            val c = mesh.vertices[triangle.c]

            // Compute the face normal
            val edge1 = Vector3f(b.x - a.x, b.y - a.y, b.z - a.z)
            val edge2 = Vector3f(c.x - a.x, c.y - a.y, c.z - a.z)
            val faceNormal = edge1.cross(edge2).normalize()

            // Add the normal for this triangle (one for each vertex of the triangle)
            flatNormals.add(faceNormal) // Normal for vertex A
            flatNormals.add(faceNormal) // Normal for vertex B
            flatNormals.add(faceNormal) // Normal for vertex C
        }

        return flatNormals
    }

    fun computeSmoothShadingNormals(): Map<Int, Vector3f> {
        val vertexNormals = mutableMapOf<Int, MutableList<Vector3f>>() // Vertex index -> list of normals

        // Step 1: Calculate the normal for each triangle
        mesh.triangles.forEach { triangle ->
            val a = mesh.vertices[triangle.a]
            val b = mesh.vertices[triangle.b]
            val c = mesh.vertices[triangle.c]

            // Compute the vectors representing two edges of the triangle
            val edge1 = Vector3f(b.x - a.x, b.y - a.y, b.z - a.z)
            val edge2 = Vector3f(c.x - a.x, c.y - a.y, c.z - a.z)

            // Compute the normal of the triangle using the cross product
            val normal = edge1.cross(edge2).normalize() // Cross product and normalization

            // Step 2: For each vertex, accumulate triangle normals
            listOf(triangle.a, triangle.b, triangle.c).forEach { vertexIndex ->
                if (vertexNormals[vertexIndex] == null) {
                    vertexNormals[vertexIndex] = mutableListOf()
                }
                vertexNormals[vertexIndex]?.add(normal)
            }
        }

        // Step 3: Average the normals for each vertex
        val averagedNormals = mutableMapOf<Int, Vector3f>()
        vertexNormals.forEach { (vertexIndex, normalsForVertex) ->
            // Sum the normals for this vertex
            val avgNormal = normalsForVertex.fold(Vector3f(0f, 0f, 0f)) { acc, normal ->
                acc.add(normal)
                acc
            }
            avgNormal.normalize() // Normalize the resulting averaged normal
            averagedNormals[vertexIndex] = avgNormal
        }

        return averagedNormals
    }
}