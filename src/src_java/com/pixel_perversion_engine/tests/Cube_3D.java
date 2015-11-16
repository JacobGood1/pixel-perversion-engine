package com.pixel_perversion_engine.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Travis on 11/16/2015.
 *
 */

public class Cube_3D {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private ModelBuilder modelBuilder;
    private Model box;
    private ModelInstance modelInstance;
    private Environment environment;
    private Vector3 cameraPivot;
    private Vector3 cameraRot;

    public Cube_3D(){
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 10f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 300f;
        cameraPivot = new Vector3(0,0,0f);
        cameraRot = new Vector3(0f, 1f, 0f);

        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        box = modelBuilder.createBox(2f, 2f, 2f,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(box, 0, 0f, 0f);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
    }

    public void render(){
        //Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        camera.rotateAround(cameraPivot, cameraRot, 1f);
        camera.update();
        modelBatch.begin(camera);
        modelBatch.render(modelInstance);
        modelBatch.end();
    }

    public void dispose(){
        modelBatch.dispose();
        box.dispose();
    }
}
