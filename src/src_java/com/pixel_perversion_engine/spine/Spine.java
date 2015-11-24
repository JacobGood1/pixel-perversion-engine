package com.pixel_perversion_engine.spine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.pixel_perversion_engine.render.Render;

/**
 * Created by Travis on 6/2/2015.
 *
 *
 */
public class Spine {
    //private SkeletonJson skeletonJson;
    private SkeletonData skeletonData;
    private AnimationStateData animationStateData;
    private AnimationState state;

    TextureAtlas atlas;
    Skeleton skeleton;
    public String textureAtlasPath;
    public String jsonPath;

    private float scale;
    //private float uniformScale = 0.1f;

    private boolean maskAA = false;
    private float maskX = 0f, maskY = 0f, maskHeight = 0f, maskWidth = 0f;

    public Spine(Render render,
                 AssetManager assetManager,
                 String textureAtlasPath,
                 String jsonPath,
                 float x, float y, float scale) {

        this.textureAtlasPath = textureAtlasPath;
        this.jsonPath = jsonPath;
        this.scale = scale;

        atlas = assetManager.get("resources/spine/" + textureAtlasPath + ".atlas", TextureAtlas.class);
        skeletonData = assetManager.get("resources/spine/" + jsonPath + ".json", SkeletonData.class);

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.getRootBone().setScaleX(skeleton.getRootBone().getScaleX() * scale);
        skeleton.getRootBone().setScaleY(skeleton.getRootBone().getScaleY() * scale);
        //skeleton.getRootBone().setX(x);
        //skeleton.getRootBone().setY(y);
        //System.out.println("before update: " + skeleton.getRootBone().getScaleX());

        skeleton.setPosition(x, y);

        this.animationStateData = new AnimationStateData(skeleton.getData()); // Defines mixing (crossfading) between animations.
        state = new AnimationState(animationStateData); // Holds the animation state for a skeleton (current animation, time, etc).

        skeleton.updateWorldTransform();

        //System.out.println("after update: " + skeleton.getRootBone().getScaleX());

        //add to render object //TODO z-index is hardcoded to 0 for testing...
        //render.spineDrawable.add(this, 0);
    }

    public Spine(AssetManager assetManager,
                 String textureAtlasPath,
                 String jsonPath,
                 float scale) {
        this.jsonPath = jsonPath;
        this.scale = scale;

        atlas = assetManager.get("resources/spine/" + textureAtlasPath + ".atlas", TextureAtlas.class);
        skeletonData = assetManager.get("resources/spine/" + jsonPath + ".json", SkeletonData.class);

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.getRootBone().setScaleX(skeleton.getRootBone().getScaleX() * scale);
        skeleton.getRootBone().setScaleY(skeleton.getRootBone().getScaleY() * scale);

        this.animationStateData = new AnimationStateData(skeleton.getData()); // Defines mixing (crossfading) between animations.
        state = new AnimationState(animationStateData); // Holds the animation state for a skeleton (current animation, time, etc).

        //skeleton.getRootBone().setX(x);
        //skeleton.getRootBone().setY(y);
        //System.out.println("before update: " + skeleton.getRootBone().getScaleX());

        //skeleton.setPosition(x, y);
        skeleton.updateWorldTransform();

        //System.out.println("after update: " + skeleton.getRootBone().getScaleX());

    }

    //secondary constructor for cloning
    public Spine(Render render, AssetManager assetManager, Spine spine, float x, float y){
        this(render, assetManager, spine.getTextureAtlasPath(), spine.getJsonPath(), x, y, spine.getScale());
    }

    public void update() {
        //(.update animation-state (.getDeltaTime Gdx/graphics))
        //(.apply animation-state (.getSkeleton spider))
        //(.updateWorldTransform (.getSkeleton spider))

        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        skeleton.updateWorldTransform();
    }

    public AnimationState.TrackEntry setAnimation(int track, String name, boolean loop){
        AnimationState.TrackEntry trackEntry = state.setAnimation(track, name, loop);
        state.apply(skeleton);
        return trackEntry;
        // Queue animations on track 0.
        //state.setAnimation(0, "hit", true);
        //state.addAnimation(0, "jump", false, 2); // Jump after 2 seconds.
        //state.addAnimation(0, "run", true, 0); // Run after the jump.
    }
    public AnimationState.TrackEntry setAnimationNow(int track, String name, boolean loop){
        AnimationState.TrackEntry trackEntry = state.setAnimation(track, name, loop);
        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        return trackEntry;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public String getTextureAtlasPath() {
        return textureAtlasPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }
    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    public SkeletonData getSkeletonData() {return skeletonData;}
    public void setSkeletonData(SkeletonData skeletonData) {this.skeletonData = skeletonData;}

    //public void setScale(float scale){skeletonJson.setScale(scale);}

    //public float getUniformScale(){return uniformScale;}

    //TODO getX getY assum the first bone is the root bone
    public float getX(){return skeleton.getRootBone().getX();} //skeletonData.getBones().get(0).getX();
    //TODO wtf get Y returns X?!?!
    public float getY(){return skeleton.getRootBone().getX();} //skeletonData.getBones().get(0).getY();

    public void setX(float x){skeleton.getRootBone().setX(x);} //skeletonData.getBones().get(0).getX();
    public void setY(float y){skeleton.getRootBone().setY(y);} //skeletonData.getBones().get(0).getY();

    public float getScale() {return scale;}
    public void setScale(float scale) {this.scale = scale;}

    public boolean isMaskAA() {
        return maskAA;
    }
    public void setMaskAA(boolean maskAA) {
        this.maskAA = maskAA;
    }
    public void setMaskDimensions(float x, float y, float width, float height){
        maskX = x; maskY = y; maskWidth = width; maskHeight = height;
    }

    public float getMaskX() {
        return maskX;
    }

    public float getMaskY() {
        return maskY;
    }

    public float getMaskHeight() {
        return maskHeight;
    }

    public float getMaskWidth() {
        return maskWidth;
    }
}