package com.github.kvasnevskyivlad.meshviewer.gl

import com.jogamp.opengl.GL2
import java.util.jar.JarEntry
import java.util.jar.JarFile

class ShaderProgramManager(private val gl: GL2) {
    private val shaderPrograms: MutableMap<String, ShaderProgram> = mutableMapOf()

    init {
        loadShaders("shaders")
    }

    fun use(shaderName: String) : ShaderProgram? {
        val shaderProgram = shaderPrograms[shaderName]
        shaderProgram?.use()
        return shaderProgram
    }

    private fun loadShaders(resource: String) {
        val resourceUrl = this.javaClass.classLoader.getResource(resource)

        if (resourceUrl != null) {
            val uri = resourceUrl.toURI()

            if (uri.scheme == "jar") {
                // Handle JAR resources
                val jarPath = uri.rawSchemeSpecificPart.substringBefore("!")
                val jarFile = JarFile(jarPath.removePrefix("file:"))
                val shadersBasePath = "$resource/"

                jarFile.entries().asSequence()
                    .filter { it.name.startsWith(shadersBasePath) && !it.isDirectory }
                    .groupBy { it.name.substringAfter(shadersBasePath).substringBefore("/") }
                    .forEach { (shaderName, files) -> loadShaderProgram(shaderName, files, jarFile)}
            } else {
                println("Unsupported URI scheme: ${uri.scheme}")
            }
        } else {
            println("Shaders folder not found in resources.")
        }
    }

    private fun loadShaderProgram(shaderName: String, files: List<JarEntry>, jarFile: JarFile) {

        val vertexShaderPath = "shaders/$shaderName/vertex_shader.glsl"
        val fragmentShaderPath = "shaders/$shaderName/fragment_shader.glsl"

        val vertexShaderSource = jarFile.getInputStream(jarFile.getJarEntry(vertexShaderPath)).bufferedReader().readText()
        val fragmentShaderSource = jarFile.getInputStream(jarFile.getJarEntry(fragmentShaderPath)).bufferedReader().readText()

        val shaderProgram = ShaderProgram(gl, vertexShaderSource, fragmentShaderSource)
        shaderPrograms[shaderName] = shaderProgram
    }
}