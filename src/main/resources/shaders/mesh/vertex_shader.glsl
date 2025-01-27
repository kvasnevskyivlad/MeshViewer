#version 330

layout(location = 0) in vec3 position; // Vertex position
layout(location = 1) in vec3 normal;   // Vertex normal

uniform mat4 view;                     // View matrix
uniform mat4 projection;               // Projection matrix

out vec3 vertexNormal;                 // Pass normal to fragment shader
out vec3 fragPosition;                 // Position of the fragment in view space

void main() {
    gl_Position = projection * view * vec4(position, 1.0); // Apply View and Projection

    // Correctly transform the normal
    mat3 normalMatrix = mat3(transpose(inverse(view)));    // Extract normal matrix
    vertexNormal = normalize(normalMatrix * normal);       // Pass the transformed normal

    // Pass the fragment position in view space
    fragPosition = vec3(view * vec4(position, 1.0));
}