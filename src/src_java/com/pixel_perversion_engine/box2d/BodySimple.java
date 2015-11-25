package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Travis on 11/24/2015.
 *
 * Creates a simple box2d body, usually for a character controller.
 */

public class BodySimple {
    public enum Type {
        Static,
        Dynamic,
        Kinematic
    }
    public Body body;
    private short groupIndex;
    private World world;
    public String name;

    public BodySimple (World world, float x, float y, float width, float height, String name, Type type) {
        this.name = name;
        this.world = world;

        setup(x, y, width, height, type);
    }

    private void setup (float x, float y, float width, float height, Type type) {
        //com.pixel_perversion_engine.box2d
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        //shape.set(worldCoordinate_Verts);//worldCoordinate_Verts
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.filter.groupIndex = groupIndex;
        fd.friction = 1.0f;
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
}
