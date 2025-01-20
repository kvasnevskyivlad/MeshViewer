package com.github.kvasnevskyivlad.meshviewer.geometry

class MeshExamples {

    companion object {
        val triangle: Mesh by lazy {
            val vertices = listOf(
                Point(-1.0f, -1.0f, 0.0f),
                Point(1.0f, -1.0f, 0.0f),
                Point(0.0f, 1.0f, 0.0f)
            )
            val triangles = listOf(Triangle(0, 1, 2))
            Mesh(vertices, triangles)
        }

        val cube: Mesh by lazy {
            val vertices = listOf(
                Point(-1.0f, -1.0f, -1.0f), // 0
                Point(1.0f, -1.0f, -1.0f),  // 1
                Point(1.0f, 1.0f, -1.0f),   // 2
                Point(-1.0f, 1.0f, -1.0f),  // 3
                Point(-1.0f, -1.0f, 1.0f),  // 4
                Point(1.0f, -1.0f, 1.0f),   // 5
                Point(1.0f, 1.0f, 1.0f),    // 6
                Point(-1.0f, 1.0f, 1.0f)    // 7
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

        // Added two triangles representing an edge of a cube
        val edge: Mesh by lazy {
            val vertices = listOf(
                Point(0.0f, 0.0f, 0.0f), // Vertex 0
                Point(1.0f, 0.0f, 0.0f), // Vertex 1
                Point(0.0f, 1.0f, 0.0f), // Vertex 2
                Point(1.0f, 1.0f, 0.0f)  // Vertex 3
            )
            val triangles = listOf(
                Triangle(0, 1, 2), // First triangle (edge of the cube)
                Triangle(1, 2, 3)  // Second triangle (edge of the cube)
            )
            Mesh(vertices, triangles)
        }

        val tetrahedron: Mesh by lazy {
            val vertices = listOf(
                Point(0.0f, 0.5f, 0.0f), // Vertex 0
                Point(-0.5f, -0.5f, -0.5f),  // Vertex 1
                Point(0.5f, -0.5f, -0.5f),  // Vertex 2
                Point(0.0f, -0.5f, 0.5f),  // Vertex 3
            )

            val triangles = listOf(
                Triangle(1, 0, 2),
                Triangle(1, 2, 3),
                Triangle(3, 2, 0),
                Triangle(3, 0, 1)
            )

            Mesh(vertices, triangles)
        }

        val pyramid: Mesh by lazy {
            // Define the vertices of the pyramid
            val vertices = listOf(
                Point(0.0f, 0.0f, 0.0f),  // Vertex 0: base left front
                Point(1.0f, 0.0f, 0.0f),  // Vertex 1: base right front
                Point(1.0f, 1.0f, 0.0f),  // Vertex 2: base right back
                Point(0.0f, 1.0f, 0.0f),  // Vertex 3: base left back
                Point(0.5f, 0.5f, 1.0f)   // Vertex 4: apex (top)
            )

            // Define the triangles of the pyramid
            val triangles = listOf(
                Triangle(0, 1, 4),  // Triangle 1: front face
                Triangle(1, 2, 4),  // Triangle 2: right face
                Triangle(2, 3, 4),  // Triangle 3: back face
                Triangle(3, 0, 4),  // Triangle 4: left face
                Triangle(0, 1, 2),  // Triangle 5: base (bottom)
                Triangle(0, 2, 3)   // Triangle 6: base (bottom)
            )

            // Create the mesh with the defined vertices and triangles
            Mesh(vertices, triangles)
        }
    }
}