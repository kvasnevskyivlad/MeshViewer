package com.github.kvasnevskyivlad.meshviewer.gl.render.shaders

import com.jogamp.opengl.GL2
import java.util.jar.JarFile

class ShadersProvider(private val gl: GL2) {
    var mesh: MeshShaderProgram
    var normals: NormalsShaderProgram

    init {
        mesh = load("mesh") { vertexShaderSource, fragmentShaderSource ->
             MeshShaderProgram(gl, vertexShaderSource, fragmentShaderSource)
        }
        normals = load("normals") { vertexShaderSource, fragmentShaderSource ->
            NormalsShaderProgram(gl, vertexShaderSource, fragmentShaderSource)
        }
    }

    private fun <T : IShaderProgram> load(name: String, creator: (String, String) -> T): T {
        val vertexShaderPath = "shaders/$name/vertex_shader.glsl"
        val fragmentShaderPath = "shaders/$name/fragment_shader.glsl"

        val vertexShaderSource = loadShaderSource(vertexShaderPath)
        val fragmentShaderSource = loadShaderSource(fragmentShaderPath)

        return creator(vertexShaderSource, fragmentShaderSource)
    }

    private fun loadShaderSource(shaderPath: String) : String {
        val resourceUrl = this.javaClass.classLoader.getResource(shaderPath)

        if (resourceUrl != null) {
            val uri = resourceUrl.toURI()

            if (uri.scheme == "jar") {
                // Handle JAR resources
                val jarPath = uri.rawSchemeSpecificPart.substringBefore("!")
                val jarFile = JarFile(jarPath.removePrefix("file:"))
                return jarFile.getInputStream(jarFile.getJarEntry(shaderPath)).bufferedReader().readText()
            } else {
                throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
            }
        } else {
            throw IllegalArgumentException("Shader not found in resources: $shaderPath")
        }
    }
}