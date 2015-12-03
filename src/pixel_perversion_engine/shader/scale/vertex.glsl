attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform sampler2D u_texture;
uniform float scalefac;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    //float scalefac = 2;
    mat4 scale = mat4(
            vec4(    scalefac, 0.0,      0.0,      0.0),
            vec4(    0.0,      scalefac, 0.0,      0.0),
            vec4(    0.0,      0.0,      1.0,      0.0),
            vec4(    0.0,      0.0,      0.0,      1.0)
        );

    v_color = a_color;
    v_texCoords = a_texCoord0;

    gl_Position = scale * u_projTrans * a_position; // u_projTrans * a_position
}