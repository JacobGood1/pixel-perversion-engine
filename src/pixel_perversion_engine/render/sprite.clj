(ns pixel-perversion-engine.render.sprite
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics GL20 Pixmap Pixmap$Format]
           [com.badlogic.gdx.graphics.glutils FrameBuffer]
           [com.pixel_perversion_engine.render Render]
           [com.badlogic.gdx.utils.viewport Viewport]
           [com.badlogic.gdx.graphics.g2d TextureRegion]))

(defn render
  [^Render render ^Viewport viewport objs fbo]
  (let [
        camera (.getCamera viewport)
        sprite-batch (.getSpriteBatch render)]

    ;(.begin fbo)
    ;(.glClearColor Gdx/gl 0 0 0 0)
    ;(.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

    (.apply viewport false)
    ;(.update camera)

    (.setShader sprite-batch nil)
    (.setProjectionMatrix sprite-batch (.-combined camera))

    (.begin fbo)
    (.begin sprite-batch)

    (doseq [obj objs]
      (.draw (:renderable obj) sprite-batch))

    (.end sprite-batch)
    (.end fbo)

    ;(.dispose fbo)

    ;NOTE: The fbo does not need to be returned. All state is stored within a java object.
    ))

(comment
  (loop [obj objs]
    (let [obj-f (first obj)]
      (cond (empty? obj) nil
            :default (do
                       (.draw (:renderable obj-f) sprite-batch)
                       (recur (rest obj)))))))