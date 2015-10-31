package com.pixel_perversion_engine.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.pixel_perversion_engine.spine.Spine;

/**
 * Created by Travis on 6/2/2015.
 *
 * Component: SpineBox2d
 * This component is for creating Box2d objects based on a Spine object and then linking those
 * Box2d objects to follow the Spine object's animation (skeleton).
 */
public class SpineBox2d extends Component_Box2d {
    private Spine spine;
    private World world;
    private short groupIndex;
    private boolean sensor;
    private String bbaName;
    private String boneName;
    private Body body;
    private Enum bodyType = BodyDef.BodyType.StaticBody;
    private Enum objectType;
    private float gravityScale = 1.0f;

    public SpineBox2d(World world, Spine spine, String name, boolean sensor, short groupIndex, Enum objectType) {
        super(objectType);
        this.spine = spine;
        this.groupIndex = groupIndex;
        this.sensor = sensor;
        this.bbaName = name;
        this.objectType = objectType;

        //build colliders and attach to box2dWorld
        setupBox2d();

    }
    public SpineBox2d(Spine spine, String name, boolean sensor, short groupIndex, Enum objectType, Enum bodyType) {
        super(objectType);
        this.spine = spine;
        this.groupIndex = groupIndex;
        this.sensor = sensor;
        this.bbaName = name;
        this.objectType = objectType;
        this.bodyType = bodyType;
        this.gravityScale = 0.0f;

        //build colliders and attach to box2dWorld
        setupBox2d();

    }

    public void setupBox2d(){
        //TODO this fn contains some duplicated code... such as creating a com.pixel_perversion_engine.box2d body which should be abstract
        spine.getSkeleton().updateWorldTransform();

        SkeletonBounds bounds = new SkeletonBounds();
        bounds.update(spine.getSkeleton(), true);

        for(BoundingBoxAttachment bbA : bounds.getBoundingBoxes()) { //this does NOT represent bounding boxes, but bounding polygon attachments!
            String bbA_name = bbA.getName();
            if (bbA_name.equals(this.bbaName)) {
                this.boneName = spine.getSkeletonData().findSlot(bbA_name).getBoneData().getName();
                //world-coordinate verts
                //float[] worldCoordinate_Verts = new float[bbA.getVertices().length]; //bbA.getVertices().length
                //bbA.computeWorldVertices(bbA_Bone, worldCoordinate_Verts);
                //local-coordinate verts
                float[] localCoordinate_Verts_ = bbA.getVertices();
                float[] localCoordinate_Verts = new float[bbA.getVertices().length];
                System.arraycopy(localCoordinate_Verts_, 0, localCoordinate_Verts, 0, bbA.getVertices().length);

                for (int i = 0; i < localCoordinate_Verts.length; i+=2) {
                    localCoordinate_Verts[i] *= spine.getScale();
                    localCoordinate_Verts[i+1] *= spine.getScale();

                    //check for X axis flip
                    if (spine.getSkeleton().getFlipX()) {
                        localCoordinate_Verts[i] *= -1;
                    }
                    //check for Y axis flip
                    if (spine.getSkeleton().getFlipY()) {
                        localCoordinate_Verts[i+1] *= -1;
                    }
                }

                float highBound_Y = 0;
                float lowBound_Y = 0;
                for (int i = 0; i < localCoordinate_Verts.length; i += 2) {
                    float y = localCoordinate_Verts[i + 1];
                    if (y > 0) {
                        highBound_Y = y;
                    } else {
                        lowBound_Y = y;
                    }
                }
                //System.out.println(spine.textureAtlasPath + " highbound: " + highBound_Y);
                //System.out.println(spine.textureAtlasPath + " lowbound: " + lowBound_Y);
                //System.out.println(spine.jsonPath + " - collider - " + bbA_name + " - Height: " + (highBound_Y + -lowBound_Y));

                //com.pixel_perversion_engine.box2d
                PolygonShape shape = new PolygonShape();
                //shape.set(worldCoordinate_Verts);
                shape.set(localCoordinate_Verts);
                FixtureDef fd = new FixtureDef();
                fd.shape = shape;
                fd.filter.groupIndex = groupIndex;
                fd.friction = 1.0f;
                fd.restitution = 0.0f;
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = (BodyDef.BodyType) bodyType;
                bodyDef.gravityScale = gravityScale;
                Body body = world.createBody(bodyDef);
                Fixture f = body.createFixture(fd);
                //assign THIS to the fixture's userData for the contact listener
                f.setUserData(this);
                f.setSensor(sensor);
                //set all fixtures to sensor
                //for(Fixture fixture : body.getFixtureList()){
                //    fixture.setSensor(sensor);
                //}
                shape.dispose();

                //HashMap<String, Body> hashMap = new HashMap<String, Body>();
                //hashMap.put(boneName, body);
                //colliders.put(bbaName, hashMap);
                this.body = body;
                Array<String> names = new Array<String>();
                names.add(bbA_name);
                names.add(boneName);
                //colliderNames.add(names);
            }
        }
    }

    public int getGroupIndex() {return groupIndex;}
    public void setGroupIndex(short groupIndex) {this.groupIndex = groupIndex;}

    //public Array<Array<String>> getColliderNames() {return colliderNames;}
    //public HashMap<String, HashMap<String, Body>> getColliders() {return colliders;}


    public Body getBody() {return body;}
    public String getBbaName() {return bbaName;}
    public String getBoneName() {return boneName;}

    public Spine getSpine() {return spine;}

    public Enum getObjectType() {
        return objectType;
    }

    public void safeDelete(){
        //TODO handle deleting bodies!!! SpineBox2d is being refactored and this needs to be changed!
        /*

        SpineBox2d c = (SpineBox2d) component;
        for (Array<String> names : c.getColliderNames()) {
            String bbAName = names.get(0);
            String boneName = names.get(1);
            c.getWorld().getBox2dWorld().destroyBody(c.getColliders().get(bbAName).get(boneName));
        }
        colliders = null;
        colliderNames = null;

        */

        world.destroyBody(this.body);
        this.body = null;
    }

    public void recieveMessage(String message) {

    }
}
