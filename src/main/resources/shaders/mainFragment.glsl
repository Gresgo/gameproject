#version 330 core

in vec3 passColor;
in vec2 passTextureCord;

out vec4 outColor;

uniform sampler2D tex;

void main() {
    outColor = texture(tex, passTextureCord);
}