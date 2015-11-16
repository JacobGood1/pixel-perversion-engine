package com.pixel_perversion_engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Travis on 12/2/2014.
 *
 */
public class InputProcessor extends InputAdapter {

    Input input;

    public InputProcessor(Input input){
        super();
        this.input = input;

        //catch back key from exiting app
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public boolean keyDown(int keycode) {
        //String key = input.Keys.toString(keycode);
        char key = com.badlogic.gdx.Input.Keys.toString(keycode).charAt(0);
        input.setKeyTyped(true);
        input.getKeyPressedMap().put(key, true);
        input.getKeyActiveMap().put(key, true);
        input.getKeyReleasedMap().put(key, false);

        /*if (keycode == input.Keys.BACK) {
            input.setBackKeyReleased(true);
        }*/

        return true;
        //return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        //String key = input.Keys.toString(keycode);
        char key = com.badlogic.gdx.Input.Keys.toString(keycode).charAt(0);

        input.getKeyPressedMap().put(key, false);
        input.getKeyActiveMap().put(key, false);
        input.getKeyReleasedMap().put(key, true);

        if (keycode == com.badlogic.gdx.Input.Keys.BACK
                || keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
            input.setBackKeyReleased(true);
        }

        return true;
        //return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {


        //override dragged input
        input.touchDiagnostic.get(pointer).setCoordinateDragged(screenX, screenY); //AppManager.HEIGHT - screenY
        input.touchDiagnostic.get(pointer).setCoordinateDragged_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinatePressed(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinatePressed_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinateHeld(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinateHeld_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setTouchPressed(true);
        input.touchDiagnostic.get(pointer).setTouchHeld(true);
        input.touchDiagnostic.get(pointer).setTouchReleased(false);

        //input.touchDiagnostic.get(pointer).setTouchIndex(pointer);

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        //override pressed input
        //input.setCoordinatePressed(-1, -1);
        input.touchDiagnostic.get(pointer).setCoordinatePressed(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinatePressed_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinateHeld(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinateHeld_raw(screenX, screenY);

        //override dragged input.touchDiagnostic.get(pointer)
        input.touchDiagnostic.get(pointer).setCoordinateDragged(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinateDragged_raw(screenX, screenY);
        //override hover input.touchDiagnostic.get(pointer)
        input.touchDiagnostic.get(pointer).setCoordinateHover(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinateReleased(screenX, screenY); //AppManager.HEIGHT - screenY

        input.touchDiagnostic.get(pointer).setTouchPressed(false);
        input.touchDiagnostic.get(pointer).setTouchHeld(false);
        input.touchDiagnostic.get(pointer).setTouchReleased(true);

        //input.setTouchIndex(pointer);


        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //override pressed input
        input.touchDiagnostic.get(pointer).setCoordinatePressed(screenX, screenY); //AppManager.HEIGHT - screenY
        input.touchDiagnostic.get(pointer).setCoordinatePressed_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinateHeld(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinateHeld_raw(screenX, screenY);

        //override hover input.touchDiagnostic.get(pointer)
        input.touchDiagnostic.get(pointer).setCoordinateHover(screenX, screenY);

        input.touchDiagnostic.get(pointer).setCoordinateDragged(screenX, screenY);
        input.touchDiagnostic.get(pointer).setCoordinateDragged_raw(screenX, screenY);

        input.touchDiagnostic.get(pointer).setTouchDragged(true);

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        input.touchDiagnostic.get(0).setCoordinateHover(screenX, screenY); //AppManager.HEIGHT - screenY
        return super.mouseMoved(screenX, screenY);
    }
}
