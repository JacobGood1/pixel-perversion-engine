package com.pixel_perversion_engine.asset_manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.SkeletonData;
import com.pixel_perversion_engine.asset_manager.loader.SkeletonDataLoader;
import com.pixel_perversion_engine.asset_manager.loader.SkeletonDataLoader.SkeletonDataLoaderParameter;

/**
 * Created by Travis on 6/20/2015.
 *
 */
public class Assets extends AssetManager {
    public SkeletonDataLoaderParameter parameter_linear;
    public String commonJsonPath = "resources/spine/play/json/";
    public String extension = ".json";

    //public String commonAtlasTerrainPath =

    public Assets(){
        //spine
        setLoader(SkeletonData.class, new SkeletonDataLoader(new InternalFileHandleResolver()));
        //SkeletonDataLoaderParameter parameter_mipmap = new SkeletonDataLoaderParameter("spine/play/mipmap.atlas");
        parameter_linear =
                new SkeletonDataLoaderParameter("resources/spine/play/linear.atlas");

        _load();
        finishLoading();
        TextureAtlas textureAtlas = get("resources/texture_atlas/terrain/terrain_atlas.atlas", TextureAtlas.class);
        //TextureRegion textureRegion = textureAtlas.findRegion("terrain_grass");
        //System.out.println("got: " + new Sprite(textureRegion.getTexture()));
    }

    public void _load(){
        //png
        //load("pixmap/bg_lights.png", Texture.class);

        //atlas
        load("resources/texture_atlas/terrain/terrain_atlas.atlas", TextureAtlas.class);

        //spine
        load(pathext(commonJsonPath, extension, "spider"), SkeletonData.class, parameter_linear);

        //UI skin
        //load("scene2d/oozeskin.json", Skin.class);//uiskin

        //sounds ////
        //fx
        //load("sounds/fx/running_extended.ogg", Sound.class);

        //music
        //load("sounds/music/music_gameplay_loop.ogg", Music.class);
    }

    public String pathext(String path, String ext, String name){
        return path + name + ext;
    }
}
