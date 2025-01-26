#version 330

in vec3 vertexNormal;               // Normal from vertex shader
in vec3 fragPosition;               // Fragment position in view space
out vec4 fragColor;                 // Final fragment color

uniform vec3 lightColor;           // Light color
uniform vec3 lightPosition;           // Light position
uniform vec3 eyePosition;           // Eye position
uniform vec3 objectColor;           // Object color


void main() {

    // Check if the triangle is back-facing
    if (!gl_FrontFacing) {
        // Color back-facing triangles red
        fragColor = vec4(1.0, 0.0, 0.0, 1.0);
        return;
    }

    // Ambient
    float ambientStrength = 0.5;
    vec3 ambient = ambientStrength * lightColor;

    // Diffuse
    vec3 normal = normalize(vertexNormal);
    vec3 lightDirection = normalize(lightPosition - fragPosition);
    float lambertian = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = lambertian * lightColor;

    // Specular
    float specularStrength = 0.3;
    vec3 viewDirection = normalize(eyePosition - fragPosition);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float specularAngle = max(dot(reflectDirection, lightDirection), 0.0);
    float specularFactor = pow(specularAngle, 32);

    if (lambertian <= 0)
    {
        specularFactor = 0.0;
    }

    vec3 specular = specularStrength * specularFactor * lightColor;


    // Result
    vec3 result = (ambient + diffuse + specular) * objectColor;
    fragColor = vec4(result, 1.0);
}