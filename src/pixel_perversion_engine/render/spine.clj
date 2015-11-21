(ns pixel-perversion-engine.render.spine
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics GL20 Pixmap Pixmap$Format]
           [com.badlogic.gdx.graphics.glutils FrameBuffer]
           [com.pixel_perversion_engine.render Render]
           [com.badlogic.gdx.utils.viewport Viewport]
           [com.badlogic.gdx.graphics.g2d TextureRegion]))

(defn render
  [^Render render ^Viewport viewport objs shader-program]
  (let [fbo (new FrameBuffer Pixmap$Format/RGBA8888 800 480 false)
        camera (.getCamera viewport)
        sprite-batch (.getSpriteBatch render)
        polygon-batch (.getPolygonBatch render)
        skeleton-renderer (.getSkeletonRenderer render)]

    (.begin fbo)
    (.glClearColor Gdx/gl 0 0 0 0)
    (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

    (.update camera)

    (.setShader polygon-batch nil) ;We normally do not set the shader for the polygon batch! This happens below within the sprite batch.
    (.setProjectionMatrix polygon-batch (.-combined camera))

    (.begin polygon-batch)

    (loop [obj objs]
      (let [obj-f (first obj)]
      (cond (empty? obj) nil
            :default (do
                       (.draw skeleton-renderer polygon-batch (.getSkeleton (:renderable obj-f)))
                       (recur (rest obj))))))

    (.end polygon-batch)
    (.end fbo)

    (let [fbo-region (new TextureRegion (.getColorBufferTexture fbo) 0 0 800 480)]
      (.flip fbo-region false true)
      (.begin sprite-batch)
      (.setShader sprite-batch shader-program) ;setting shader to nil for TESTING!!!
      (.draw sprite-batch fbo-region (float 0) (float 0))
      (.end sprite-batch)
      )

    (.dispose fbo)
    ))