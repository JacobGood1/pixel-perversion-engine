package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Travis on 12/3/2015.
 *
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Body a_body = (Body) a.getUserData();
        Body b_body = (Body) b.getUserData();

        a_body.beginContact(a_body, b_body);
        b_body.beginContact(b_body, a_body);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Body a_body = (Body) a.getUserData();
        Body b_body = (Body) b.getUserData();

        a_body.endContact(a_body, b_body);
        b_body.endContact(b_body, a_body);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
