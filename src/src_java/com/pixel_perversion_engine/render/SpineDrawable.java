package com.pixel_perversion_engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixel_perversion_engine.spine.Spine;


import java.util.HashMap;

/**
 * Created by Travis on 6/30/2015.
 *
 */
public class SpineDrawable implements Drawable{
    protected Array invalid = new Array();
    protected int drawableSize;

    public boolean useShader = false;
    public ShaderProgram shaderProgram;
    FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 480, false);

    private HashMap<Integer, Array<Spine>> spineDrawOrder;
    public SpineDrawable(int drawableSize){
        this.drawableSize = drawableSize;
        //construct the spineDrawOrder default map
        spineDrawOrder = new HashMap<Integer, Array<Spine>>();
        for (int i = 0; i < drawableSize; i++) {
            spineDrawOrder.put(i, new Array<Spine>());
        }
    }

    public void draw(Render render, Viewport viewport){
        invalid.clear();
        //draw all spine objects

        //viewport.apply();

        //TESTING GREYSCALE SHADER!

        fbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        com.badlogic.gdx.graphics.Camera camera = viewport.getCamera();
        camera.update();

        render.getPolygonBatch().setShader(null);
        render.getPolygonBatch().setProjectionMatrix(camera.combined);
        render.getPolygonBatch().begin();

        for (int i = 0; i < drawableSize; i++) {
            if (spineDrawOrder.get(i).size > 0) {
                for (int j = 0; j < spineDrawOrder.get(i).size; j++) {
                    //draw spine object
                    Spine spine = (Spine) spineDrawOrder.get(i).get(j);
                    if (true) {
                        //apply mask
                        if (spine.isMaskAA()) {
                            //System.out.println("applying mask to render...");

                            render.getPolygonBatch().end();
                            //enable masking
                            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
                            render.getPolygonBatch().begin();

                            Gdx.gl.glScissor(
                                    (int)spine.getMaskX(),
                                    (int)spine.getMaskY(),
                                    (int)spine.getMaskWidth(),
                                    (int)spine.getMaskHeight()
                            );
                            render.getSkeletonRenderer().draw(render.getPolygonBatch(), spine.getSkeleton());
                            render.getPolygonBatch().end();
                            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
                            render.getPolygonBatch().begin();

                        } else {
                            render.getSkeletonRenderer().draw(render.getPolygonBatch(), spine.getSkeleton());
                        }

                    } else {
                        //add zindex
                        invalid.add(i);
                        //add spine object
                        invalid.add(spine);
                    }
                }
            }
        }
        render.getPolygonBatch().end();

        fbo.end();

        TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, 800, 480);
        fboRegion.flip(false, true);

        //render.getSpriteBatch().setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        render.getSpriteBatch().begin();
        if (useShader) render.getSpriteBatch().setShader(shaderProgram);
        //render.getSpriteBatch().setColor(1f, 1f, 1f, 1f);
        //if (useShader) render.getSpriteBatch().setShader(shaderProgram);
        render.getSpriteBatch().draw(fboRegion, 0, 0);
        render.getSpriteBatch().end();

        for (int i = 0; i < invalid.size; i+=2) {
            //count by 2
            int zIndex = (Integer) invalid.get(0);
            Spine spine = (Spine) invalid.get(1);
            spineDrawOrder.get(zIndex).removeValue(spine, true);
        }
    }

    private void clearSpineDrawOrder(){
        for (int i = 0; i < drawableSize; i++){
            spineDrawOrder.get(i).clear();
        }
    }

    public int getDrawables_size() {return drawableSize;}
    public void setDrawables_size(int drawableSize) {
        this.drawableSize = drawableSize;}

    public HashMap<Integer, Array<Spine>> getSpineDrawOrder() {
        return spineDrawOrder;
    }
    public void setSpineDrawOrder(HashMap<Integer, Array<Spine>> spineDrawOrder) {
        this.spineDrawOrder = spineDrawOrder;}

    public void add(Spine spine, int zIndex){
        if (spineDrawOrder.get(zIndex) == null) {
            throw new IllegalArgumentException("zIndex is out of range!");
        }
        spineDrawOrder.get(zIndex).add(spine);
    }

    /*

    @Override
    public Drawable getDrawable() {
        return this;
    }

    */


}
