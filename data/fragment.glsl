#version 150 core

in vec3 vertexColor;
in vec2 textureCoords;

out vec4 fragColor;

uniform sampler2D texImage;

void main() {

    vec4 textureColor = texture(texImage, textureCoords);
    fragColor = vec4(vertexColor, 1.0f);
}