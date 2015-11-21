package com.pixel_perversion_engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;

/**
 * Created by Travis on 11/21/2015.
 *
 */
public class Spine {
    static public void draw(
            Render render, Viewport viewport,
            Array<HashMap> spines, ShaderProgram shaderProgram){ //com.pixel_perversion_engine.spine.Spine
        //viewport.apply();

        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888,
                800, 480, false);

        fbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        com.badlogic.gdx.graphics.Camera camera = viewport.getCamera();
        camera.update();

        //fully render spine object BEFORE applying shader!
        render.getPolygonBatch().setShader(null);
        render.getPolygonBatch().setProjectionMatrix(camera.combined);

        //draw spine object

        render.getPolygonBatch().begin();

        for (HashMap map : spines) {
            com.pixel_perversion_engine.spine.Spine spine = (com.pixel_perversion_engine.spine.Spine) map.get("renderable");
            render.getSkeletonRenderer().draw(render.getPolygonBatch(), spine.getSkeleton());
        }

        render.getPolygonBatch().end();

        fbo.end();

        TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, 800, 480);
        fboRegion.flip(false, true);

        //render.getSpriteBatch().setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        render.getSpriteBatch().begin();
        render.getSpriteBatch().setShader(shaderProgram);
        //render.getSpriteBatch().setColor(1f, 1f, 1f, 1f);
        //if (useShader) render.getSpriteBatch().setShader(shaderProgram);
        render.getSpriteBatch().draw(fboRegion, 0, 0);
        render.getSpriteBatch().end();

        fbo.dispose();
        //return fbo;
    }
}
