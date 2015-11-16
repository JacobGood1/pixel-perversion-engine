package com.pixel_perversion_engine.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.lwjgl.input.Mouse;

public class TestShader_NMap{
    Texture cmap, nmap;

    SpriteBatch batch;
    OrthographicCamera cam;

    ShaderProgram shader;

    //our constants...
    public static final float DEFAULT_LIGHT_Z = 0.075f;
    public static final float AMBIENT_INTENSITY = 0.1f;
    public static final float LIGHT_INTENSITY = 2f;

    public static final Vector3 LIGHT_POS = new Vector3(0f,0f,DEFAULT_LIGHT_Z);

    //Light RGB and intensity (alpha)
    public static final Vector3 LIGHT_COLOR = new Vector3(1f, 0.8f, 0.6f);

    //Ambient RGB and intensity (alpha)
    public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);

    //Attenuation coefficients for light falloff
    public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);


    final String VERT =
            Gdx.files.internal("src/pixel_perversion_engine/shader/nmap/vertex.glsl").readString();

    //no changes except for LOWP for color values
    //we would store this in a file for increased readability
    final String FRAG =
            //GL ES specific stuff
            Gdx.files.internal("src/pixel_perversion_engine/shader/nmap/fragment.glsl").readString();

    public TestShader_NMap(Texture colorMap, Texture normalMap){
        this.cmap = colorMap;
        this.nmap = normalMap;

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(VERT, FRAG);
        //ensure it compiled
        if (!shader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: "+shader.getLog());
        //print any warnings
        if (shader.getLog().length()!=0)
            System.out.println(shader.getLog());

        //setup default uniforms
        shader.begin();

        //our normal map
        shader.setUniformi("u_normals", 1); //GL_TEXTURE1

        //light/ambient colors
        //LibGDX doesn't have Vector4 class at the moment, so we pass them individually...
        shader.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
        shader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
        shader.setUniformf("Falloff", FALLOFF);

        //LibGDX likes us to end the shader program
        shader.end();

        //batch = new SpriteBatch();
        batch = new SpriteBatch(1000, shader);
        batch.setShader(shader);

        cam = new OrthographicCamera(800, 480);
        cam.setToOrtho(false);
        batch.setProjectionMatrix(cam.combined);

        shader.begin();
        shader.setUniformf("Resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.end();

        //handle mouse wheel
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean scrolled(int delta) {
                //LibGDX mouse wheel is inverted compared to lwjgl-basics
                LIGHT_POS.z = Math.max(0f, LIGHT_POS.z - (delta * 0.005f));
                System.out.println("New light Z: "+LIGHT_POS.z);
                return true;
            }
        });
    }

    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);

        shader.begin();
        shader.setUniformf("Resolution", width, height);
        shader.end();
    }


    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();

        //reset light Z
        if (Gdx.input.isTouched()) {
            LIGHT_POS.z = DEFAULT_LIGHT_Z;
            System.out.println("New light Z: "+LIGHT_POS.z);
        }

        batch.begin();
        //batch.draw(cmap, 0, 0);

        //shader will now be in use...

        //update light position, normalized to screen resolution
        float x = Mouse.getX() / (float) Gdx.graphics.getWidth();
        float y = Mouse.getY() / (float) Gdx.graphics.getHeight();//Display.getHeight();

        LIGHT_POS.x = x;
        LIGHT_POS.y = y;

        //send a Vector4f to GLSL
        shader.setUniformf("LightPos", LIGHT_POS);

        //bind normal map to texture unit 1
        nmap.bind(1);

        //bind diffuse color to texture unit 0
        //important that we specify 0 otherwise we'll still be bound to glActiveTexture(GL_TEXTURE1)
        cmap.bind(0);

        //draw the texture unit 0 with our shader effect applied
        batch.draw(cmap, 0, 0);

        batch.end();
    }


    public void pause() {

    }


    public void resume() {

    }


    public void dispose() {
        batch.dispose();
        cmap.dispose();
        nmap.dispose();
        shader.dispose();
    }

    //object which describes a point light which will typically be stored within an array
    //for the normalMap shader
    public class PointLight{

    }
}