package com.pixel_perversion_engine.box2d;

import clojure.lang.AFn;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Travis on 11/24/2015.
 *
 * Creates a simple box2d body, usually for a character controller.
 */

public class BodySimple_Triangle extends Body{
    private AFn beginContact;
    private AFn endContact;
    public BodySimple_Triangle(World world, float x, float y, float width, float height, String name, Body.Type type, AFn beginContact, AFn endContact) {
        this.beginContact = beginContact;
        this.endContact = endContact;
        this.name = name;
        this.world = world;

        setup(x, y, width, height, type);
    }

    private void setup (float x, float y, float width, float height, Type type) {
        //com.pixel_perversion_engine.box2d
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, 0.0f);
        vertices[1] = new Vector2(-0.5f, 1f);
        vertices[2] = new Vector2(0.5f, 0.0f);


        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        //shape.setAsBox(width/2, height/2);
        //shape.set(worldCoordinate_Verts);//worldCoordinate_Verts
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.filter.groupIndex = groupIndex;
        //fd.friction = 1.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.density = 1.2f;//80.7f / (float) Math.pow(1.75, 2);
        BodyDef bodyDef = new BodyDef();
        bodyDef.linearDamping = 0.0f;
        bodyDef.fixedRotation = true;
        //bodyDef.angularDamping = 500f;
        switch (type) {
            case Static:
                bodyDef.type = BodyDef.BodyType.StaticBody;
                break;
            case Dynamic:
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            default:
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                break;
        }
        bodyDef.position.set(x + (width/2), y + (height/2));
        body = this.world.createBody(bodyDef);
        //controller.setLinearVelocity(0f, 0f);
        Fixture f = body.createFixture(fd);
        //assign THIS to the fixture's userData for the contact listener
        f.setUserData(this);
        shape.dispose();
    }

    public void beginContact(Body a, Body b) {
        beginContact.invoke(a, b);
    }

    public void endContact(Body a, Body b) {
        endContact.invoke(a, b);
    }

    @Override
    public void preSolve(Body a, Body b) {

    }

    @Override
    public void postSolve(Body a, Body b) {

    }
}
