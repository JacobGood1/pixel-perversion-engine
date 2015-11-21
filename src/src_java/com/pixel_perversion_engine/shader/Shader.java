package com.pixel_perversion_engine.shader;

import com.badlogic.gdx.Gdx;
import com.pixel_perversion_engine.render.SpineDrawable;

/**
 * Created by Travis on 11/21/2015.
 *
 */
public abstract class Shader {
    //TODO drawable size hardcoded for testing...
    SpineDrawable spineDrawable = new SpineDrawable(10);
    //SpriteDrawable spriteDrawable = new SpriteDrawable(10);
    public abstract void render();
    public abstract void resize(int width, int height);
    public abstract void dispose();

    public String loadFromDisk (String path) {
        return Gdx.files.internal(path).readString();
    }
}
