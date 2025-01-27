#version 330

in vec3 vertexNormal;               // Normal from vertex shader
in vec3 fragPosition;               // Fragment position in view space
out vec4 fragColor;                 // Final fragment color

uniform vec3 lightColor;           // Light color
uniform vec3 lightPosition;           // Light position
uniform vec3 eyePosition;           // Eye position
uniform vec3 objectColor;           // Object color


void main() {

    // Ambient
    float ambientStrength = 0.7;
    vec3 ambient = ambientStrength * lightColor;

    // Diffuse
    vec3 normal = gl_FrontFacing ? normalize(vertexNormal) : -normalize(vertexNormal);
    vec3 lightDirection = normalize(lightPosition - fragPosition);
    float lambertian = max(dot(normal, lightDirection), 0.0);
    float diffuseFalloff = 0.5; // Smoothness factor for diffuse lighting
    vec3 diffuse = pow(lambertian, 1.0 / diffuseFalloff) * lightColor;

    // Specular
    float specularStrength = 0.2;
    float specularExponent = 16.0; // Adjusted for a broader highlight
    vec3 viewDirection = normalize(eyePosition - fragPosition);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float specularAngle = max(dot(reflectDirection, lightDirection), 0.0);
    float specularFactor = pow(specularAngle, specularExponent);

    // Ensure specular highlights only occur on lit areas of the surface.
    // If the surface is not lit (lambertian <= 0), the specular effect is disabled.
    if (lambertian <= 0)
    {
        specularFactor = 0.0;
    }

    vec3 specular = specularStrength * specularFactor * lightColor;

    // Back-facing adjustment: scale down light for back-facing triangles
    float backFaceFactor = gl_FrontFacing ? 1.0 : 0.5; // Reduce light for back-facing triangles
    vec3 color = gl_FrontFacing ? objectColor : vec3(1.0, 0.0, 0.0); // Color back-facing triangles red

    // Final computation
    vec3 result = (ambient + diffuse + specular) * color * backFaceFactor;
    fragColor = vec4(result, 1.0);
}