#version 330

layout(location = 0) in vec3 position; // Vertex position
layout(location = 1) in vec3 color;    // Vertex color

uniform mat4 transform;               // Transformation matrix

out vec3 vertexColor;                 // Pass color to fragment shader

void main() {
    gl_Position = transform * vec4(position, 1.0); // Apply transformation
    vertexColor = color;                           // Pass the color
}