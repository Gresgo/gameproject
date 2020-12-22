#version 460 core

in vec3 position;
in vec3 color;
in vec2 textureCord;

out vec3 passColor;
out vec2 passTextureCord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(position, 1.0);
    passColor = color;
    passTextureCord = textureCord;
}