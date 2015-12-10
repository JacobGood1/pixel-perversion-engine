package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashMap;

/**
 * Created by Travis on 12/3/2015.
 *
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        HashMap<String, Object> a_hm = (HashMap<String, Object>) a.getUserData();
        HashMap<String, Object> b_hm = (HashMap<String, Object>) b.getUserData();

        Body a_body = (Body) a_hm.get("body");
        Body b_body = (Body) b_hm.get("body");

        a_body.beginContact(a_hm, b_hm);
        b_body.beginContact(b_hm, a_hm);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        HashMap<String, Object> a_hm = (HashMap<String, Object>) a.getUserData();
        HashMap<String, Object> b_hm = (HashMap<String, Object>) b.getUserData();

        Body a_body = (Body) a_hm.get("body");
        Body b_body = (Body) b_hm.get("body");

        a_body.endContact(a_hm, b_hm);
        b_body.endContact(b_hm, a_hm);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        HashMap<String, Object> a_hm = (HashMap<String, Object>) a.getUserData();
        HashMap<String, Object> b_hm = (HashMap<String, Object>) b.getUserData();

        Body a_body = (Body) a_hm.get("body");
        Body b_body = (Body) b_hm.get("body");

        a_body.preSolve(a_hm, b_hm, contact, manifold);
        b_body.preSolve(b_hm, a_hm, contact, manifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        HashMap<String, Object> a_hm = (HashMap<String, Object>) a.getUserData();
        HashMap<String, Object> b_hm = (HashMap<String, Object>) b.getUserData();

        Body a_body = (Body) a_hm.get("body");
        Body b_body = (Body) b_hm.get("body");


        a_body.postSolve(a_hm, b_hm, contact, contactImpulse);
        b_body.postSolve(b_hm, a_hm, contact, contactImpulse);
    }
}
