#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec4 color = texture2D(u_texture, v_texCoords).rgba;
        vec3 grey = vec3((color.r + color.g + color.b) / 3);
        gl_FragColor = vec4(grey, color.a);
}