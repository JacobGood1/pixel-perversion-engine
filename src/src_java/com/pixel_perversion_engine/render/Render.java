package com.pixel_perversion_engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.pixel_perversion_engine.spine.Spine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Travis on 12/4/2014.
 *
 * Initiates Objects required for rendering and provides common rendering fns.
 */

public class Render {
    private boolean debug = true;

    //TODO TouchDiagnostic makes heavy use of this camera object.
    //TODO It eventually needs to be refactored to work with Viewports.
    public Camera camera;
    private SpriteBatch spriteBatch;
    private PolygonSpriteBatch polygonBatch;
    private ShapeRenderer shapeRenderer;
    private SkeletonRenderer skeletonRenderer;
    private SkeletonRendererDebug skeletonRendererDebug;
    private Box2DDebugRenderer box2DDebugRenderer;
    //static FrameBuffer frameBuffer;
    //static BitmapFont font = new BitmapFont();

    private FreeTypeFontGenerator generator;
    //FreeTypeFontGenerator.FreeTypeBitmapFontData font15 = generator.generateData(15);
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Map<String, BitmapFont> font;

    private Map<String, Color> color;
    private Color mutibleColor = new Color();

    private HashMap<Enum, Drawable> drawables = new HashMap<Enum, Drawable>();
    public SpineDrawable spineDrawable;

    public Render(){
        //GdxNativesLoader.load(); THIS IS FROM OLD CODE, WHAT IS IT FOR THO?

        camera = new Camera();

        spriteBatch = new SpriteBatch();
        polygonBatch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();
        skeletonRenderer = new SkeletonRenderer();
        //TODO figure out the weird rendering problems with premulAlpha
        skeletonRenderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
        skeletonRendererDebug = new SkeletonRendererDebug();
        //skeletonRendererDebug.setBoundingBoxes(false);
        //skeletonRendererDebug.setRegionAttachments(false);
        box2DDebugRenderer = new Box2DDebugRenderer();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font = new HashMap<String, BitmapFont>();
        color = new HashMap<String, Color>();
        //generateFont();
        setColor();

        spineDrawable = new SpineDrawable(5);
        drawables.put(Drawable.DrawableType.Spine, spineDrawable);
    }

    public void generateFont(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fontName.ttf"));

        Array<Integer> sizes = new Array<Integer>();
        sizes.add(16);
        sizes.add(20);
        sizes.add(32);
        sizes.add(42);
        sizes.add(64);
        sizes.add(128);

        for(Integer s:sizes){
            parameter.size = (int)s;
            font.put(s.toString(), generator.generateFont(parameter));
        }

        //dispose generator after all fonts have been created
        generator.dispose();
        generator = null;
    }

    public void setColor() {
        color.put("white", new Color(1.0f, 1.0f, 1.0f, 1.0f));
        color.put("white-50%", new Color(1.0f, 1.0f, 1.0f, 0.5f));
        color.put("black", new Color(0.0f, 0.0f, 0.0f, 1.0f));
        color.put("black-50%", new Color(0.0f, 0.0f, 0.0f, 0.5f));
        color.put("orange", new Color(1.0f, 0.5f, 0.0f, 1.0f));
        color.put("red", new Color(1.0f, 0.0f, 0.0f, 1.0f));
        color.put("blue", new Color(0.0f, 0.5f, 1.0f, 1.0f));
        color.put("pink", new Color(1.0f, 0.0f, 1.0f, 1.0f));
        color.put("green", new Color(0.0f, 1.0f, 0.0f, 1.0f));
    }

    public void dispose(){
        spriteBatch.dispose();
        polygonBatch.dispose();
        shapeRenderer.dispose();
        box2DDebugRenderer.dispose();
        for (BitmapFont value : font.values()) {
            value.dispose();
        }
    }

    static public Sprite screenToTexture (int x, int y, int width, int height) {
        TextureRegion frameBufferRegion = ScreenUtils.getFrameBufferTexture(x, y, width, height);
        return new Sprite(frameBufferRegion);
    }

    public void setProjectionMatrix_Sprite(OrthographicCamera camera){
        spriteBatch.setProjectionMatrix(camera.combined);
    }
    public void setProjectionMatrix_Polygon(OrthographicCamera camera){
        polygonBatch.setProjectionMatrix(camera.combined);
    }
    public void setProjectionMatrix_Shape(OrthographicCamera camera){
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public PolygonSpriteBatch getPolygonBatch() {
        return polygonBatch;
    }

    public void setPolygonBatch(PolygonSpriteBatch polygonBatch) {
        this.polygonBatch = polygonBatch;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public SkeletonRenderer getSkeletonRenderer() {
        return skeletonRenderer;
    }

    public void setSkeletonRenderer(SkeletonRenderer skeletonRenderer) {
        this.skeletonRenderer = skeletonRenderer;
    }

    public SkeletonRendererDebug getSkeletonRendererDebug() {
        return skeletonRendererDebug;
    }

    public void setSkeletonRendererDebug(SkeletonRendererDebug skeletonRendererDebug) {
        this.skeletonRendererDebug = skeletonRendererDebug;
    }

    public Box2DDebugRenderer getBox2DDebugRenderer() {return box2DDebugRenderer;}

    public void setBox2DDebugRenderer(Box2DDebugRenderer box2DDebugRenderer) {this.box2DDebugRenderer = box2DDebugRenderer;}

    public Map<String, Color> getColor() {
        return color;
    }

    public void setColor(Map<String, Color> color) {
        this.color = color;
    }

    public boolean isDebug() {return debug;}
    public void setDebug(boolean debug) {this.debug = debug;}

    public void debug_viewCenter(Viewport viewport, Color color){
        //OrthographicCamera cam = camera.getCamera(cameraType);
        OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();
        float x = camera.position.x;
        float y = camera.position.y;
        float width = viewport.getWorldWidth();//cam.viewportWidth;
        float height = viewport.getWorldHeight();//cam.viewportHeight;
        shapeRenderer.setColor(color.r, color.g, color.b, color.a);
        //x
        shapeRenderer.line(
                x - width/2,
                height/2 + y - height/2,
                width + x - width/2,
                height/2 + y - height/2

        );
        //y
        shapeRenderer.line(
                width/2 + x - width/2,
                height + y - height/2,
                width/2 + x - width/2,
                y - height/2
        );
    }

    public void debug_viewBoundaries(Viewport viewport, Color color){
        //OrthographicCamera cam = (OrthographicCamera) viewport.getCamera();
        float width = viewport.getWorldWidth();//cam.viewportWidth;
        float height = viewport.getWorldHeight();//cam.viewportHeight;
        shapeRenderer.setColor(color.r, color.g, color.b, color.a);
        shapeRenderer.rect(
                -width / 2,
                -height / 2,
                width,
                height);
    }

    public void debug_Spine (Render render, HashMap<Enum, Drawable> drawable) {
        float sclF = 1f;
        render.getShapeRenderer().setProjectionMatrix(render.getSpriteBatch().getProjectionMatrix().scl(sclF));
        render.getShapeRenderer().setTransformMatrix(render.getSpriteBatch().getTransformMatrix().scl(sclF));

        SpineDrawable spineDrawable = (SpineDrawable) drawable.get(Drawable.DrawableType.Spine);
        int drawables_size = spineDrawable.getDrawables_size();
        HashMap<Integer, Array<Spine>> spineDrawOrder = spineDrawable.getSpineDrawOrder();

        ShapeRenderer renderer = skeletonRendererDebug.getShapeRenderer();//getSkeletonRendererDebug().getShapeRenderer();
        //this.camera.getCamera(Camera.CameraType.world).combined
        renderer.getProjectionMatrix().set(spriteBatch.getProjectionMatrix());
        renderer.getTransformMatrix().set(spriteBatch.getTransformMatrix());
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //spine objects
        for (int i = 0; i < drawables_size; i++) {
            if (spineDrawOrder.get(i).size > 0) {
                for (int j = 0; j < spineDrawOrder.get(i).size; j++) {
                    Skeleton skeleton = spineDrawOrder.get(i).get(j).getSkeleton();
                    getSkeletonRendererDebug().draw(skeleton);
                }
            }
        }
        //shapeRenderer.end();
    }

    public void draw(Render render, Viewport viewport){
        spineDrawable.draw(render, viewport);
    }

    //TODO hardcoded gradient, this should be changed at some point.
    public void rect (Viewport viewport, float alpha) {

        Color color = getColor().get("black");
        mutibleColor.set(color);
        mutibleColor.a = alpha;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        getShapeRenderer().setProjectionMatrix(viewport.getCamera().combined);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getShapeRenderer().rect(
                0,// - Gdx.graphics.getWidth() / 2,
                0,// - Gdx.graphics.getHeight() / 2,
                viewport.getWorldWidth(),
                viewport.getWorldHeight()

                /*gradient*/
                //bottom
                ,Color.CLEAR, Color.CLEAR,
                //top
                mutibleColor, mutibleColor
        );
        getShapeRenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    //TODO hardcoded gradient, this should be changed at some point.
    public void rect_gradient_Spine (Viewport viewport, float alpha) {

        Color color = getColor().get("black");
        mutibleColor.set(color);
        mutibleColor.a = alpha;

        //getShapeRenderer().setColor(mutibleColor);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        getShapeRenderer().setProjectionMatrix(viewport.getCamera().combined);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getShapeRenderer().rect(
                -800 / 2,// - Gdx.graphics.getWidth() / 2,
                -480 / 2,// - Gdx.graphics.getHeight() / 2,
                viewport.getWorldWidth(),
                viewport.getWorldHeight()

                /*gradient*/
                //bottom
                ,Color.CLEAR, Color.CLEAR,
                //top
                mutibleColor, mutibleColor
        );
        getShapeRenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    public void rect_Spine (Viewport viewport, float alpha) {

        Color color = getColor().get("black");
        mutibleColor.set(color);
        mutibleColor.a = alpha;

        //preserve original color
        Color c = getShapeRenderer().getColor();
        getShapeRenderer().setColor(mutibleColor);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        getShapeRenderer().setProjectionMatrix(viewport.getCamera().combined);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getShapeRenderer().rect(
                -800 / 2,// - Gdx.graphics.getWidth() / 2,
                -480 / 2,// - Gdx.graphics.getHeight() / 2,
                viewport.getWorldWidth(),
                viewport.getWorldHeight()
        );
        getShapeRenderer().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //set back to original color
        getShapeRenderer().setColor(c);
    }
}
