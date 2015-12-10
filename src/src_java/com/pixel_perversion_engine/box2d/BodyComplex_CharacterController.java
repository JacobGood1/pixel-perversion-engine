package com.pixel_perversion_engine.box2d;

import clojure.lang.AFn;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Travis on 11/24/2015.
 *
 * Creates a simple box2d body, usually for a character controller.
 */

public class BodyComplex_CharacterController extends Body {
    public Fixture main;
    public Fixture groundSensor;
    public HashSet<Body> contacts = new HashSet<Body>();
    public boolean slopeConnection = false;

    public BodyComplex_CharacterController(World world, float x, float y, float width, float height, String name, Type type,
                                           AFn beginContact, AFn endContact, AFn preSolve, AFn postSolve) {

        super(world, name, beginContact, endContact, preSolve, postSolve);
        setup(x, y, width, height, type);
        buildOffset();
    }

    protected void setup (float x, float y, float width, float height, Type type) {
        //com.pixel_perversion_engine.box2d
        PolygonShape shape_main = new PolygonShape();
        shape_main.setAsBox(width/2, height/2);

        PolygonShape shape_slopeCollider = new PolygonShape();
        shape_slopeCollider.setAsBox(0.01f, height/2, new Vector2(0.0f, -0.1f), 0.0f);

        point1.x = 0.0f; point1.y = height/2;//width/2; point1.y = height/2;
        point2.x = 0.0f; point2.y = -height/2;//width/2; point2.y = -height/2;
        //shape_main.set(worldCoordinate_Verts);//worldCoordinate_Verts

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


        main = body.createFixture(createFixtureDef(shape_main, false, (short) 0x0008));
        main.setUserData(super.makeUserData(this, "character_main"));//this

        groundSensor = body.createFixture(createFixtureDef(shape_slopeCollider, true, (short) 0xffff));
        groundSensor.setUserData(super.makeUserData(this, "character_slope"));//this

        shape_main.dispose();
    }

    private FixtureDef createFixtureDef(Shape shape, boolean sensor, short mask){
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.filter.groupIndex = super.groupIndex;
        //fd.friction = 1.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;
        fd.density = 1.2f;//80.7f / (float) Math.pow(1.75, 2);
        fd.isSensor = sensor;

        fd.filter.categoryBits = Category.Player.getNumVal();
        //short mask = Category.Slope.getNumVal() & Category.Square.getNumVal();
        fd.filter.maskBits = mask;//0x0004 | 0x0008;

        return fd;
    }

    @Override
    public void beginContact(HashMap<String, Object> a, HashMap<String, Object> b) {
        Body other = (Body) b.get("body");
        contacts.add(other);

        super.beginContact(a, b);
    }

    @Override
    public void endContact(HashMap<String, Object> a, HashMap<String, Object> b) {
        Body other = (Body) b.get("body");
        contacts.remove(other);

        super.endContact(a, b);
    }

    public boolean checkSet (String name) {
        boolean check = false;
        for (Body b : contacts) {
            System.out.println(b.name);
            if (b.name.equals(name)) {
                check = true;
            }
        }

        return check;
    }
}
