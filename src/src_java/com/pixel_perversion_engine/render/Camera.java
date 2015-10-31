package com.pixel_perversion_engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

/**
 * Created by Travis on 12/7/2014.
 *
 * Camera wrapper which handles all cameras added to the world
 * and will uniformly adjust all cameras.
 */

public class Camera {
    //TODO figure out how to better handle custom cameras
    public enum CameraType{
        world, gui//, bg, box2d
    }

    private float width = 800f;
    private float height = 480f;
    private float x;
    private float y;
    private HashMap<Enum, OrthographicCamera> cameras = new HashMap<Enum, OrthographicCamera>();

    public Camera(){
        //create empty camera object
    }


    public void update(){
        for (OrthographicCamera camera : cameras.values()) {//for (Render.CameraType cameraType : Render.CameraType.values()) {
            camera.update();
            //System.out.println(camera.viewportWidth);
        }
    }

    public void addCamera(CameraType cameraType, float width, float height, float x, float y){ //Enum name
        //create and store a new camera
        OrthographicCamera orthographicCamera = new OrthographicCamera(width, height);
        orthographicCamera.position.x = x;
        orthographicCamera.position.y = y;
        cameras.put(cameraType, orthographicCamera);
    }
    public OrthographicCamera getCamera(Enum name){
        return cameras.get(name);
    }

    public void setDimensions(float width, float height){
        for (OrthographicCamera camera : cameras.values()) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;

            camera.update();
        }
    }
    public void setWidth(float width){
        for (OrthographicCamera camera : cameras.values()) {
            camera.viewportWidth = width;
        }
    }
    public void setHeight(float height){
        for (OrthographicCamera camera : cameras.values()) {
            camera.viewportHeight = height;
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector3 getPosition(){
        return cameras.get(CameraType.world).position;
    }
    public void setPosition(float x, float y){
        for (OrthographicCamera camera : cameras.values()) {
            camera.position.x = x;
            camera.position.y = y;
        }
    }

    public void setZoom(float zoom){
        for (OrthographicCamera camera : cameras.values()) {
            camera.zoom = zoom;
        }
    }

    public Vector3 screenToWorld (OrthographicCamera camera, Vector3 coordinate){
        //convert screen coordinate to world coordinate
        return camera.project(coordinate);
    }
}