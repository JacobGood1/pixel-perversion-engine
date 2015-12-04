package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.World;

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
    public com.badlogic.gdx.physics.box2d.Body body;
    public short groupIndex;
    public World world;
    public String name;

    public abstract void beginContact(Body a, Body b);

    public abstract void endContact(Body a, Body b);

    public abstract void preSolve(Body a, Body b);

    public abstract void postSolve(Body a, Body b);
}
