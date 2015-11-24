(ns pixel-perversion-engine.render.spine
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics GL20 Pixmap Pixmap$Format]
           [com.badlogic.gdx.graphics.glutils FrameBuffer]
           [com.pixel_perversion_engine.render Render]
           [com.badlogic.gdx.utils.viewport Viewport]
           [com.badlogic.gdx.graphics.g2d TextureRegion]))

(def fbo nil)
(def fbo-region nil)

(defn setup-render
  []
  (def fbo (new FrameBuffer Pixmap$Format/RGBA8888 800 480 false))
  (def fbo-region (new TextureRegion (.getColorBufferTexture fbo) 0 0 800 480)))
(defn dispose-render
  []
  (.dispose fbo)
  (.dispose fbo-region))

(defn render
  [^Render render ^Viewport viewport objs shader-program]
  (let [
        camera (.getCamera viewport)
        sprite-batch (.getSpriteBatch render)
        polygon-batch (.getPolygonBatch render)
        skeleton-renderer (.getSkeletonRenderer render)]

    (.begin fbo)
    (.glClearColor Gdx/gl 0 0 0 0)
    (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

    (.apply viewport false)
    ;(.update camera)

    (.setShader polygon-batch nil) ;We normally do not set the shader for the polygon batch! This happens below within the sprite batch.
    (.setProjectionMatrix polygon-batch (.-combined camera))

    (.begin polygon-batch)

    (doseq [obj objs]
      (.draw skeleton-renderer polygon-batch (.getSkeleton (:renderable obj))))

    (.end polygon-batch)
    (.end fbo)

    (let [
          cam-x (.x (.position camera))
          cam-y (.y (.position camera))
          x (- cam-x (/ (.viewportWidth camera) 2))
          y (- cam-y (/ (.viewportHeight camera) 2))
          ]
      (.setTexture fbo-region (.getColorBufferTexture fbo))
      (if (not (.isFlipY fbo-region)) (.flip fbo-region false true))
      (.setProjectionMatrix sprite-batch (.-combined camera))
      (.begin sprite-batch)
      (.setShader sprite-batch shader-program)
      (.draw sprite-batch fbo-region (float x) (float y))
      (.end sprite-batch)
      )

    ;(.dispose fbo)
    ;(.dispose fbo-region)
    ))