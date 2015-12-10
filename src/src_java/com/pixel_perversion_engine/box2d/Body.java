package com.pixel_perversion_engine.box2d;

import clojure.lang.AFn;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Travis on 12/3/2015.
 *
 */
public abstract class Body{
    public enum Type {
        Static,
        Dynamic,
        Kinematic
    }
    public enum Category {
        Player (0x0002),
        Slope (0x0004),
        SlopeTrigger (0x0010),
        Square (0x0008);

        private int numVal;

        Category(int numVal) {
            this.numVal = numVal;
        }

        public short getNumVal() {
            return (short) numVal;
        }
    }
    public String name;
    public short groupIndex;
    public World world;
    public float offset;

    public Vector2 point1 = new Vector2();
    public Vector2 point2 = new Vector2();
    protected Vector2 worldPoint1 = new Vector2();
    protected Vector2 worldPoint2 = new Vector2();
    public Array<Vector2> worldPoints = new Array<Vector2>();
    public com.badlogic.gdx.physics.box2d.Body body;

    protected AFn beginContact;
    protected AFn endContact;
    protected AFn preSolve;
    protected AFn postSolve;

    public Body (World world, String name, AFn beginContact, AFn endContact, AFn preSolve, AFn postSolve) {
        this.beginContact = beginContact;
        this.endContact = endContact;
        this.preSolve = preSolve;
        this.postSolve = postSolve;
        this.world = world;
        this.name = name;
    }

    public Array<Vector2> getWorldPoints(){
        worldPoint1.x = body.getPosition().x + point1.x;
        worldPoint1.y = body.getPosition().y + point1.y;

        worldPoint2.x = body.getPosition().x + point2.x;
        worldPoint2.y = body.getPosition().y + point2.y;

        worldPoints.clear();
        worldPoints.add(worldPoint1);
        worldPoints.add(worldPoint2);

        return worldPoints;
    }

    protected void buildOffset(){
        float y = body.getPosition().y;
        offset = y + 1.0f;
    }

    protected HashMap<String, Object> makeUserData (Body body, String name) {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("body", body);
        hm.put("name", name);
        return hm;
    }

    public void beginContact(HashMap<String, Object> a, HashMap<String, Object> b){
        beginContact.invoke(a, b);
    }

    public void endContact(HashMap<String, Object> a, HashMap<String, Object> b){
        endContact.invoke(a, b);
    }

    public void preSolve(HashMap<String, Object> a, HashMap<String, Object> b, Contact contact, Manifold manifold){
        preSolve.invoke(a, b, contact, manifold);
    }

    public void postSolve(HashMap<String, Object> a, HashMap<String, Object> b, Contact contact, ContactImpulse contactImpulse){
        postSolve.invoke(a, b, contact, contactImpulse);
    }

    abstract protected void setup(float x, float y, float width, float height, Type type);
}
