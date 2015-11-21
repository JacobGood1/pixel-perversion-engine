package com.pixel_perversion_engine.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Travis on 11/21/2015.
 *
 */
public class Greyscale extends Shader {
    final String VERT = loadFromDisk("src/pixel_perversion_engine/shader/greyscale/vertex.glsl");
    final String FRAG = loadFromDisk("src/pixel_perversion_engine/shader/greyscale/fragment.glsl");
    final ShaderProgram shaderProgram = new ShaderProgram(VERT, FRAG);
    public Greyscale(){
        //
    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
