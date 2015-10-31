package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Travis on 6/6/2015.
 *
 */
public abstract class Component_Box2d{
    private boolean isColliding = false;
    private Enum objectType;
    private Array<Enum> collidingObjectType = new Array<Enum>();

    public Component_Box2d( Enum objectType) {
        this.objectType = objectType;
    }

    public void contactBegin(Component_Box2d a, Component_Box2d b) {
        collidingObjectType.add(b.objectType);
        isColliding = true;

        //System.out.println(a);
        //System.out.println("begin: " + collidingObjectType);
        //System.out.println();
    }

    public void contactEnd(Component_Box2d a, Component_Box2d b) {
        collidingObjectType.removeValue(b.objectType, true);

        if (collidingObjectType.size == 0) {
            isColliding = false;
            //System.out.println("colliding = false");
        }

        //System.out.println(a);
        //System.out.println("end: " + collidingObjectType);
        //System.out.println();

    }

    public void contactPre(Component_Box2d a, Component_Box2d b) {

    }

    public void contactPost(Component_Box2d a, Component_Box2d b) {

    }

    public boolean isColliding() {return isColliding;}
    public void setIsColliding(boolean isColliding) {this.isColliding = isColliding;}

    public Enum getObjectType() {return objectType;}

    public Array<Enum> getCollidingObjectType() {return collidingObjectType;}
}
