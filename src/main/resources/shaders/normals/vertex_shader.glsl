#version 330

layout(location = 0) in vec3 position;    // Vertex position
layout(location = 1) in vec3 color;       // Vertex color

uniform mat4 view;                    // View matrix
uniform mat4 projection;              // Projection matrix

out vec3 fragColor; // Pass color to fragment shader

void main() {
    gl_Position = projection * view * vec4(position, 1.0); // Apply View and Projection
    fragColor = color;                  // Pass color to fragment shader
}