package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.pixel_perversion_engine.spine.Spine;

/**
 * Created by Travis on 6/3/2015.
 *
 * Component: SpineBox2dController
 * This component is for linking the position of a Spine object to a Box2d object.
 */
public class SpineBox2dController{
    private Spine spine;
    private String bbAName;
    private String boneName;
    private Body controller;
    private Bone controlled;
    private short groupIndex;
    private World world;

    public SpineBox2dController(Spine spine, String bbAName, String boneName, short groupIndex, Enum objectType) {
        this.spine = spine;
        this.bbAName = bbAName;
        this.boneName = boneName;
        this.groupIndex = groupIndex;

        //build colliders and attach to box2dWorld
        setupBox2d();
    }

    public void setupBox2d(){
        //TODO this fn contains some duplicated code... such as creating a com.pixel_perversion_engine.box2d body which should be abstract

        //TODO spine object is currently being stored within this object. this idea is being reversed.
        spine.getSkeleton().updateWorldTransform();
        SkeletonBounds bounds = new SkeletonBounds();
        bounds.update(spine.getSkeleton(), true);

        boolean controllerFound = false;
        for(BoundingBoxAttachment bbA : bounds.getBoundingBoxes()){
            if (bbA.getName().equals(bbAName)) {
                controllerFound = true;
                Bone bbA_Bone = spine.getSkeleton().findBone(boneName);//bbA_name
                //world-coordinate verts
                float[] worldCoordinate_Verts = new float[bbA.getVertices().length]; //bbA.getVertices().length
                bbA.computeWorldVertices(bbA_Bone, worldCoordinate_Verts);
                //local-coordinate verts
                float[] localCoordinate_Verts = bbA.getVertices();

                float highBound_Y = 0;
                float lowBound_Y = 0;

                for (int i = 0; i < worldCoordinate_Verts.length; i+=2) {
                    float y = worldCoordinate_Verts[i + 1];
                    if (y > 0) {
                        highBound_Y = y;
                    } else {
                        lowBound_Y = y;
                    }
                }

                //System.out.println(spine.textureAtlasPath + " highbound: " + highBound_Y);
                //System.out.println(spine.textureAtlasPath + " lowbound: " + lowBound_Y);
                //System.out.println(spine.textureAtlasPath + " - controller - " + " - Height: " + (highBound_Y + -lowBound_Y));

                //System.out.println(spine.jsonPath + " - collider - " + bbAName + " - Height: " + (highBound_Y + -lowBound_Y));

                for (int i = 0; i < worldCoordinate_Verts.length; i++) {
                    //localCoordinate_Verts[i] *= spine.getScale();
                    //System.out.println(localCoordinate_Verts[i] * (spine.getScale() / localCoordinate_Verts[i]));

                    //worldCoordinate_Verts[i] *= conversion;
                }


                //com.pixel_perversion_engine.box2d
                PolygonShape shape = new PolygonShape();
                shape.set(worldCoordinate_Verts);//worldCoordinate_Verts
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
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                controller = this.world.createBody(bodyDef);
                //controller.setLinearVelocity(0f, 0f);
                Fixture f = controller.createFixture(fd);
                //assign THIS to the fixture's userData for the contact listener
                f.setUserData(this);
                shape.dispose();

                //assign bone which will be controlled by a com.pixel_perversion_engine.box2d body
                controlled = bbA_Bone;
            }
        }
        if (!controllerFound) {
            throw new RuntimeException("Component: SpineBox2dController:: Controller polygon not found!");
        }
    }

    public Body getController() {return controller;}
    public void setController(Body controller) {this.controller = controller;}
    public Bone getControlled() {return controlled;}
    public void setControlled(Bone controlled) {this.controlled = controlled;}

    public String getBbAName(){return bbAName;}


    public void safeDelete(){
        if (controller != null) {
            this.world.destroyBody(controller);
            controller = null;
        }
    }
}
