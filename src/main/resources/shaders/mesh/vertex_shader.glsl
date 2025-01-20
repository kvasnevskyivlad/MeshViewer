#version 330

layout(location = 0) in vec3 position; // Vertex position
layout(location = 1) in vec3 color;    // Vertex color
layout(location = 2) in vec3 normal;    // Vertex normal

uniform mat4 view;                    // View matrix
uniform mat4 projection;              // Projection matrix

out vec3 vertexColor;                 // Pass color to fragment shader
out vec3 vertexNormal;                // Pass normal to fragment shader
out vec3 fragPosition;                 // Position of the fragment in view space

void main() {
    gl_Position = projection * view * vec4(position, 1.0); // Apply View and Projection
    vertexColor = color;                                   // Pass the color
    vertexNormal = normal;                                 // Pass the normal

    // Calculate the fragment position in view space (since model is skipped, viewProjection * position suffices)
    fragPosition = vec3(projection * view * vec4(position, 1.0));
}