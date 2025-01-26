package com.github.kvasnevskyivlad.meshviewer.geometry

import kotlin.math.cos
import kotlin.math.sin

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


        val sphere: Mesh by lazy {
            val radius = 1.0f
            val sectorCount = 36  // Longitudinal slices
            val stackCount = 18   // Latitudinal slices

            val vertices = mutableListOf<Point>()
            val triangles = mutableListOf<Triangle>()

            // Generate vertices
            for (stack in 0..stackCount) {
                val phi = Math.PI * stack / stackCount  // Elevation angle (0 to PI)
                for (sector in 0..sectorCount) {
                    val theta = 2.0 * Math.PI * sector / sectorCount  // Azimuth angle (0 to 2PI)
                    val x = (radius * sin(phi) * cos(theta)).toFloat()
                    val y = (radius * cos(phi)).toFloat()
                    val z = (radius * sin(phi) * sin(theta)).toFloat()
                    vertices.add(Point(x, y, z))
                }
            }

            // Generate triangles with CCW order
            for (stack in 0 until stackCount) {
                for (sector in 0 until sectorCount) {
                    val first = (stack * (sectorCount + 1)) + sector
                    val second = first + sectorCount + 1

                    // First triangle (CCW)
                    triangles.add(Triangle(first, second, first + 1))
                    // Second triangle (CCW)
                    triangles.add(Triangle(second, second + 1, first + 1))
                }
            }

            Mesh(vertices, triangles)
        }

        val cube: Mesh by lazy {
            val vertices = listOf(
                // Front face
                Point(-1.0f, -1.0f, 1.0f), // 0
                Point( 1.0f, -1.0f, 1.0f), // 1
                Point( 1.0f,  1.0f, 1.0f), // 2
                Point(-1.0f,  1.0f, 1.0f), // 3
                // Back face
                Point(-1.0f, -1.0f, -1.0f), // 4
                Point( 1.0f, -1.0f, -1.0f), // 5
                Point( 1.0f,  1.0f, -1.0f), // 6
                Point(-1.0f,  1.0f, -1.0f), // 7
            )

            val triangles = listOf(
                // Front face
                Triangle(0, 1, 2),
                Triangle(0, 2, 3),
                // Right face
                Triangle(1, 5, 6),
                Triangle(1, 6, 2),
                // Back face
                Triangle(5, 4, 7),
                Triangle(5, 7, 6),
                // Left face
                Triangle(4, 0, 3),
                Triangle(4, 3, 7),
                // Bottom face
                Triangle(4, 5, 1),
                Triangle(4, 1, 0),
                // Top face
                Triangle(3, 2, 6),
                Triangle(3, 6, 7)
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
    }
}