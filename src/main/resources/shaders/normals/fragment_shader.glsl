#version 330

in vec3 fragColor;  // Color passed from vertex shader
out vec4 fragColorOutput;  // Output color to fragment

void main() {
    fragColorOutput = vec4(fragColor, 1.0);  // Set the fragment color
}