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
    public Fixture main;
    public Fixture detectPlayer;

    public BodySimple_Triangle(World world, float x, float y, float width, float height, String name, Body.Type type,
                               AFn beginContact, AFn endContact, AFn preSolve, AFn postSolve) {

        super(world, name, beginContact, endContact, preSolve, postSolve);
        setup(x, y, width, height, type);
        buildOffset();
    }

    protected void setup (float x, float y, float width, float height, Type type) {
        //com.pixel_perversion_engine.box2d

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

        createTriangleFixture(main, "slope");
        createSquareFixture(detectPlayer, "detectPlayer");
    }

    private void createTriangleFixture(Fixture bind, String userDataName){
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(-0.5f, 0.5f);
        vertices[2] = new Vector2(0.5f, -0.5f);

        point1 = vertices[1];
        point2 = vertices[2];


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
        fd.isSensor = false;
        fd.filter.categoryBits = Category.Slope.getNumVal();//0x0004;
        //fd.filter.maskBits = 0x0002;//(short) Category.Player.getNumVal();

        bind = super.body.createFixture(fd);
        bind.setUserData(super.makeUserData(this, userDataName));
        shape.dispose();
    }
    private void createSquareFixture(Fixture bind, String userDataName){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.2f, new Vector2(0.0f, 0.5f), 0.0f);
        //shape.setAsBox(width/2, height/2);
        //shape.set(worldCoordinate_Verts);//worldCoordinate_Verts
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.filter.groupIndex = groupIndex;
        //fd.friction = 1.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.density = 1.2f;//80.7f / (float) Math.pow(1.75, 2);
        fd.isSensor = true;
        fd.filter.categoryBits = Category.SlopeTrigger.getNumVal();//0x0010;
        //fd.filter.maskBits = 0x0002;//(short) Category.Player.getNumVal();

        bind = super.body.createFixture(fd);
        bind.setUserData(super.makeUserData(this, userDataName));
        shape.dispose();
    }
}
