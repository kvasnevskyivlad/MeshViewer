#version 330

in vec3 vertexColor;                // Interpolated color from vertex shader
in vec3 vertexNormal;               // Normal from vertex shader
in vec3 fragPosition;               // Fragment position in view space
out vec4 fragColor;                 // Final fragment color

uniform vec3 eyePosition;                   // Camera position for view direction

const vec3 lightDirection = vec3(0, 0, 1);
const float shininess = 32.0f;

void main() {
    // Normalize the input normal
    vec3 normal = normalize(vertexNormal);

    // Calculate light direction (normalize to avoid intensity problems)
    vec3 lightDir = normalize(lightDirection);

    // Calculate the view direction (from fragment to camera)
    vec3 viewDir = normalize(eyePosition - fragPosition);

    // Diffuse reflection (Lambertian reflectance)
    float diff = max(dot(normal, lightDir), 0.0);

    // Specular reflection (Phong model)
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);

    // Ambient term
    vec3 ambient = 0.1 * vertexColor;  // 10% ambient light

    // Diffuse term
    vec3 diffuse = diff * vertexColor;

    // Specular term (added for shininess)
    vec3 specular = spec * vec3(1.0, 1.0, 1.0);  // White specular highlight

    // Combine all components
    vec3 color = ambient + diffuse + specular;

    // Final output color (with alpha = 1.0 for full opacity)
    fragColor = vec4(color, 1.0);
}