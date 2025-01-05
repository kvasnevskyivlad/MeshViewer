#version 330

in vec3 vertexColor;                // Interpolated color from vertex shader
out vec4 fragColor;                 // Final fragment color

void main() {
    fragColor = vec4(vertexColor, 1.0); // Use the vertex color with full opacity
}