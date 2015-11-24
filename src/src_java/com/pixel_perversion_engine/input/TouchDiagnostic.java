package com.pixel_perversion_engine.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixel_perversion_engine.render.Render;
/**
 * Created by Travis on 3/7/2015.
 *
 */
public class TouchDiagnostic {
    private Input input;
    private Render render;

    private Vector2 coordinate = new Vector2(0,0);
    private Vector3 unproject = new Vector3(0,0,0);

    private boolean touchPressed;
    private boolean touchHeld;
    private boolean touchDragged;
    private boolean touchReleased;

    private Vector2 coordinatePressed;
    private Vector2 coordinateHeld;
    private Vector2 coordinateReleased;
    private Vector2 coordinateDragged;
    private Vector2 coordinateHover;

    private Vector2 coordinatePressed_raw;
    private Vector2 coordinateHeld_raw;
    private Vector2 coordinateReleased_raw;
    private Vector2 coordinateDragged_raw;
    private Vector2 coordinateHover_raw;

    public enum ActionTypes {
        Pressed,
        Held,
        Dragged,
        Released
    }
    public Array<ActionTypes> postFrameAction = new Array<ActionTypes>();

    //swipe
    //private PointDragDisplacement pdd;
    private double swipeGravity = 0.1f;
    private Vector2 followTouchVector = new Vector2(0,0);
    private Vector2 swipeVector = new Vector2(0,0);

    //flick
    boolean flicked = false;

    TouchDiagnostic(Input input, Render render) {
        //pdd = new PointDragDisplacement(world.getInput());
        this.input = input;
        this.render = render;

        coordinatePressed = new Vector2(0,0);
        coordinateHeld = new Vector2(0,0);
        coordinateReleased = new Vector2(0,0);
        coordinateDragged = new Vector2(0,0);
        coordinateHover = new Vector2(0,0);

        coordinatePressed_raw = new Vector2(0,0);
        coordinateHeld_raw = new Vector2(0,0);
        coordinateReleased_raw = new Vector2(0,0);
        coordinateDragged_raw = new Vector2(0,0);
        coordinateHover_raw = new Vector2(0,0);
    }

    void update(){
        if (touchReleased()) {
            addPostFrameAction(ActionTypes.Released);
        } else if (touchDragged()) {
            addPostFrameAction(ActionTypes.Dragged);
        } else if (touchPressed()) {
            addPostFrameAction(ActionTypes.Pressed);
            addPostFrameAction(ActionTypes.Held);
        }

        //System.out.println(postFrameAction);

        clearPostFrameAction();


        if(touchReleased){
            //coordinatePressed.x = -1; coordinatePressed.y = -1;
            //coordinateHeld.x = -1; coordinateHeld.y = -1;
            //coordinateReleased.x = -1; coordinateReleased.y = -1;
            //coordinateDragged.x = -1; coordinateDragged.y = -1;
            //coordinateHover.x = -1; coordinateHover.y = -1;
        }

        touchPressed = false;
        touchDragged = false;
        touchReleased = false;
    }

    public void setCoordinatePressed(float x, float y){
        this.coordinatePressed.x = x;
        this.coordinatePressed.y = y;
    }
    public void setCoordinatePressed_raw(float x, float y){
        this.coordinatePressed_raw.x = x;
        this.coordinatePressed_raw.y = y;
    }

    public void setCoordinateHeld(float x, float y){
        this.coordinateHeld.x = x;
        this.coordinateHeld.y = y;
    }
    public void setCoordinateHeld_raw(float x, float y){
        this.coordinateHeld_raw.x = x;
        this.coordinateHeld_raw.y = y;
    }

    public void setCoordinateReleased(float x, float y){
        this.coordinateReleased.x = x;
        this.coordinateReleased.y = y;
    }
    public void setCoordinateReleased_raw(float x, float y){
        this.coordinateReleased_raw.x = x;
        this.coordinateReleased_raw.y = y;
    }

    public void setCoordinateDragged(float x, float y){
        this.coordinateDragged.x = x;
        this.coordinateDragged.y = y;
    }
    public void setCoordinateDragged_raw(float x, float y){
        this.coordinateDragged_raw.x = x;
        this.coordinateDragged_raw.y = y;
    }

    public void setCoordinateHover(float x, float y){
        this.coordinateHover.x = x;
        this.coordinateHover.y = y;
    }
    public void setCoordinateHover_raw(float x, float y){
        this.coordinateHover_raw.x = x;
        this.coordinateHover_raw.y = y;
    }

    public Vector2 getCoordinatePressed(Viewport viewport) {//Enum cameraType
        unproject.x = coordinatePressed.x;
        unproject.y = coordinatePressed.y;
        Vector3 coordinate = viewport.getCamera().unproject(unproject);//world.getRender().camera.getCamera(cameraType).unproject(unproject);
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        return this.coordinate;
    }
    public Vector2 getCoordinateHeld(Enum cameraType) {
        unproject.x = coordinateHeld.x;
        unproject.y = coordinateHeld.y;
        Vector3 coordinate = render.camera.getCamera(cameraType).unproject(unproject);
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        return this.coordinate;
    }
    public Vector2 getCoordinateReleased(Viewport viewport) {
        unproject.x = coordinateReleased.x;
        unproject.y = coordinateReleased.y;
        Vector3 coordinate = viewport.getCamera().unproject(unproject);//world.getRender().camera.getCamera(cameraType).unproject(unproject);
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        return this.coordinate;
        //return coordinateReleased;
    }
    public Vector2 getCoordinateDragged(Viewport viewport){
        unproject.x = coordinateDragged.x;
        unproject.y = coordinateDragged.y;
        Vector3 coordinate = viewport.getCamera().unproject(unproject);//world.getRender().camera.getCamera(cameraType).unproject(unproject);
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        return this.coordinate;
        //return coordinateDragged;
    }
    public Vector2 getCoordinateHover(Viewport viewport){
        unproject.x = coordinateHover.x;
        unproject.y = coordinateHover.y;
        Vector3 coordinate = viewport.getCamera().unproject(unproject);//world.getRender().camera.getCamera(cameraType).unproject(unproject);
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        return this.coordinate;
        //return coordinateHover;
    }

    public Vector2 getCoordinatePressed_raw() {
        return coordinatePressed_raw;
    }
    public Vector2 getCoordinateReleased_raw() {
        return coordinateReleased_raw;
    }
    public Vector2 getCoordinateDragged_raw(){ return coordinateDragged_raw; }
    public Vector2 getCoordinateHover_raw(){ return coordinateHover_raw; }

    public boolean touchReleased() {
        return touchReleased;
    }

    public void setTouchReleased(boolean touchReleased) {
        this.touchReleased = touchReleased;
    }

    public boolean touchPressed() {
        return touchPressed && !input.consumed;
    }

    public boolean touchHeld() { return touchHeld; }

    public boolean touchDragged(){ return touchDragged; }

    public void setTouchPressed(boolean touchPressed) {
        this.touchPressed = touchPressed;
    }

    public void setTouchHeld(boolean touchHeld) { this.touchHeld = touchHeld; }

    public void setTouchDragged(boolean touchDragged) {this.touchDragged = touchDragged;}

    //TODO PPD
    /*

    public void update_pdd (int x) {
        pdd.preUpdate(x);
    }

    */
//TODO PPD
   /*

    public PointDragDisplacement getPdd() {
        return pdd;
    }

   */


    public Vector2 followTouch () {
        //TODO pdd is being updated within the game loop, but perhaps it should be updated internally from this object?
        //pdd.preUpdate();

        //TODO PPD
        //followTouchVector.x = pdd.getTranslationVector().x;
        //followTouchVector.y = pdd.getTranslationVector().y;
        return followTouchVector;
    }

    public Vector2 swipe () {
        return swipeVector;
    }

    public void addPostFrameAction (ActionTypes actionType) {
        if (postFrameAction.size == 0) {
            postFrameAction.add(actionType);
        } else if (postFrameAction.size == 1) {
            postFrameAction.add(actionType);
        } else {
            postFrameAction.set(0, postFrameAction.get(1));
            postFrameAction.set(1, actionType);
        }
    }

    public void clearPostFrameAction () {
        flicked = false;

        if (postFrameAction.size == 2) {
            if (postFrameAction.get(0) == ActionTypes.Dragged
                    && postFrameAction.get(1) == ActionTypes.Released) {
                flicked = true;
            }

            //postFrameAction.clear();
            //postFrameAction.size = 0;
        }
    }
}
