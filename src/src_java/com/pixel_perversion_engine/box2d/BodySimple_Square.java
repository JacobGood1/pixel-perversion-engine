package com.pixel_perversion_engine.box2d;

import clojure.lang.AFn;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Travis on 11/24/2015.
 *
 * Creates a simple box2d body, usually for a character controller.
 */

public class BodySimple_Square extends Body {

    public BodySimple_Square(World world, float x, float y, float width, float height, String name, Body.Type type,
                             AFn beginContact, AFn endContact, AFn preSolve, AFn postSolve) {

        super(world, name, beginContact, endContact, preSolve, postSolve);
        setup(x, y, width, height, type);
        buildOffset();
    }

    protected void setup (float x, float y, float width, float height, Body.Type type) {
        //com.pixel_perversion_engine.box2d
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-0.5f, 0.5f);
        vertices[1] = new Vector2(0.5f, 0.5f);
        vertices[2] = new Vector2(0.5f, -0.5f);
        vertices[3] = new Vector2(-0.5f, -0.5f);

        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(vertices);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        point1.x = -width/2; point1.y = height/2;//width/2; point1.y = height/2;
        point2.x = width/2; point2.y = height/2;//width/2; point2.y = -height/2;
        //shape.set(worldCoordinate_Verts);//worldCoordinate_Verts
        FixtureDef fd = new FixtureDef();
        fd.shape = chainShape;//shape;
        fd.filter.groupIndex = super.groupIndex;
        fd.filter.categoryBits = Category.Square.getNumVal();//0x0008;
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
        super.body = this.world.createBody(bodyDef);
        //controller.setLinearVelocity(0f, 0f);
        Fixture f = body.createFixture(fd);
        //assign THIS to the fixture's userData for the contact listener
        f.setUserData(super.makeUserData(this, "square"));//ground //this
        shape.dispose();
    }
}
