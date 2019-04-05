#version 150 core

in vec2 position;
in vec2 texcoord;

out vec3 vertexColor;
out vec2 textureCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 color;

void main() {
    vertexColor = color;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 0.0, 1.0);
}