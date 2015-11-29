#ifdef GL_ES
    precision mediump float;
#endif

attribute vec4 a_position;
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
//our screen resolution, set from Java whenever the display is resized
uniform vec2 resolution;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb;

        //determine center position
        vec2 p = (gl_FragCoord.y / resolution.y);
        //determine the vector length of the center position
        //float len = length(p);

        /*if (gl_FragCoord.y == 400.0)
        gl_FragColor = vec4(0.0,0.0,0.0 ,1.0);
                //gl_FragColor = vec4(texture2D(tex,uv).xyz ,1.0);
            else
                gl_FragColor = vec4(color, 1.0);*/

        //gl_FragColor = (gl_FragCoord.x<25.0) ? vec4(1.0, 0.0, 0.0, 1.0) : vec4(0.0, 1.0, 0.0, 1.0);
        //gl_FragColor = (gl_FragCoord.y<25.0) ? vec4(texture2D(u_texture, v_texCoords).rgb ,1.0) : vec4(0.0, 1.0, 0.0, 1.0);
        gl_FragColor = (mod(gl_FragCoord.y, 2) >= 1.0) ? vec4(color.rgb ,1.0) : vec4(color.r * 0.5, color.g * 0.5, color.b * 0.5 ,1.0);

        //gl_FragColor = vec4(color, 1.0);
}