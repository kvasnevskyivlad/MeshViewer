package com.github.kvasnevskyivlad.meshviewer.geometry

class MeshExamples {

    companion object {
        val triangle: Mesh by lazy {
            val vertices = listOf(
                Point(-1.0, -1.0, 0.0),
                Point(1.0, -1.0, 0.0),
                Point(0.0, 1.0, 0.0)
            )
            val triangles = listOf(Triangle(0, 1, 2))
            Mesh(vertices, triangles)
        }

        val cube: Mesh by lazy {
            val vertices = listOf(
                Point(-1.0, -1.0, -1.0), // 0
                Point(1.0, -1.0, -1.0),  // 1
                Point(1.0, 1.0, -1.0),   // 2
                Point(-1.0, 1.0, -1.0),  // 3
                Point(-1.0, -1.0, 1.0),  // 4
                Point(1.0, -1.0, 1.0),   // 5
                Point(1.0, 1.0, 1.0),    // 6
                Point(-1.0, 1.0, 1.0)    // 7
            )
            val triangles = listOf(
                Triangle(0, 1, 2), Triangle(2, 3, 0), // Front face
                Triangle(4, 5, 6), Triangle(6, 7, 4), // Back face
                Triangle(3, 2, 6), Triangle(6, 7, 3), // Top face
                Triangle(0, 1, 5), Triangle(5, 4, 0), // Bottom face
                Triangle(0, 3, 7), Triangle(7, 4, 0), // Left face
                Triangle(1, 2, 6), Triangle(6, 5, 1)  // Right face
            )
            Mesh(vertices, triangles)
        }
    }
}