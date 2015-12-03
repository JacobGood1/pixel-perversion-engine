(ns pixel-perversion-engine.render.render
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont TextureRegion PolygonSpriteBatch]
           [com.pixel_perversion_engine.render Render Spine]
           [com.badlogic.gdx.utils Array]
           [com.badlogic.gdx.utils.viewport FitViewport]
           [com.badlogic.gdx.graphics.glutils FrameBuffer]
           [com.badlogic.gdx.graphics Pixmap$Format GL20 OrthographicCamera]
           [com.badlogic.gdx Gdx])
  (require [pixel-perversion-engine.render.spine :as spine]
           [pixel-perversion-engine.render.sprite :as sprite]
           [snake-game.shading-network.shader-network-1 :as shader-network-1]))

(defn render-all
  [{:keys [layers fbos sprite-batch polygon-batch skeleton-renderer fit-viewport] :as root}] ;layers ;;{0 {2 [:b] 1 [:a] 3 [:c]} 5 {6 [:d] 7 [:e] 8 [:f]}};(:layers root)

  (let [root root
        viewport-width (.getWorldWidth fit-viewport)
        viewport-height (.getWorldHeight fit-viewport)
        camera (.getCamera fit-viewport)

        layers-sorted-keys (sort (keys layers))

        render-type-state (atom :sprite)

        ;well... this is just neat-o lol
        ;storing these code snippets within a map really helps straight-ahead rendering.
        batch-symbols {:begin {:sprite (fn []
                                         (.begin sprite-batch))
                               :spine  (fn []
                                         (.begin polygon-batch))}
                       :end   {:sprite (fn [] (.end sprite-batch))
                               :spine  (fn [] (.end polygon-batch))}
                       :draw  {:sprite (fn [element] (.draw (:renderable element) sprite-batch))
                               :spine  (fn [element] (.draw skeleton-renderer polygon-batch (.getSkeleton (:renderable element))))}}



        ]

    ;debug camera controls
    (let [z? (.keyActive (get-in root [:input]) \Z)
          x? (.keyActive (get-in root [:input]) \X)
          zoom (.-zoom camera)]
      (cond z? (set! (.-zoom camera) (- zoom 0.1))
            x? (set! (.-zoom camera) (+ zoom 0.1))))

    ;setup for rendering, this only needs to be applied once.
    (set! (.-zoom camera) 1.0);1.0
    (.apply fit-viewport false)
    (.setProjectionMatrix sprite-batch (.-combined camera))
    (.setProjectionMatrix polygon-batch (.-combined camera))
    ;clear shaders!
    (.setShader sprite-batch nil)
    (.setShader polygon-batch nil)


    ;render layer contents to it's fbo ;all elements will respect their index order ;doseq for straight-ahead rendering
    ;batch type will be cached/swapped depending on what needs to be rendered.
    (doseq [li layers-sorted-keys] ;loop through sorted layer keys, we will use this to index the layer map
      (let [layer (layers li)
            indice-sorted-keys (sort (keys layer))]

        ;begin this layer's fbo
        (.begin (fbos li))
        (.glClearColor Gdx/gl 0 0 0 0)
        (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

        ;starting the batch with sprite for testing purposes... but this should be handled better.
        ((@render-type-state (:begin batch-symbols)))

        (doseq [ei indice-sorted-keys] ;not much different than the outter loop. we sort the keys of the layer to then access the indices within the layer.
          (let [indice (layer ei)] ;look up the indices in this layer for elements.
            (doseq [element indice] ;we are now looping over elements.
              ;check the rendering state. if there is a mismatch, we must change swap to the correct state
              (cond (not (= @render-type-state (:type element))) (do
                                                                   ;NOTE every call is wrapped because we are invoking anon fns retrieved from batch-symbols
                                                                   ;end/flush the current batch.
                                                                   ((@render-type-state (:end batch-symbols)))

                                                                   ;start next batch.
                                                                   (((:type element) (:begin batch-symbols)))

                                                                   ;draw element to fbo
                                                                   (((:type element) (:draw batch-symbols)) element) ;<- we are passing element this time!

                                                                   ;swap the atom's state!
                                                                   (swap! render-type-state (fn [a] (:type element)))
                                                                   )

                    ;rendering states match, render the element based off it's type
                    :default (do
                               ;NOTE just like before, every call is wrapped because we are invoking anon fns retrieved from batch-symbols

                               ;draw element to fbo
                               (((:type element) (:draw batch-symbols)) element) ;<- passing element
                               )
                    )
              )
            )
          )

        ;end batch
        ((@render-type-state (:end batch-symbols)))

        ;end this layer's fbo
        (.end (fbos li))

        )
      )

    ;BOOKMARK shader network commented for debugging
    (shader-network-1/shader-network-1 root fbos layers-sorted-keys)

    ;BOOKMARK
    ;SKIP SHADER NETWORKS AND COMPILE ALL FBOS TO SCREEN
    (comment
      (doseq [li layers-sorted-keys]
        (let [texture (.getColorBufferTexture (fbos li))
              t-r (new TextureRegion texture)]
          (.flip t-r false true)
          (.begin sprite-batch)
          (.draw sprite-batch t-r (float (/ (* -1 viewport-width) 2)) (float (/ (* -1 viewport-height) 2)))
          (.end sprite-batch)
          ))
      )



    )
  )

(defn render-text
  [root text x y]
  (.draw (get-in root [:bitmap-font])
         (get-in root [:sprite-batch])
         text (float x) (float y)))