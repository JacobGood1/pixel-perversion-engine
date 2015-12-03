(ns snake-game.map-editor.gui
  (:use [pixel-perversion-engine.object.object])
  (:import [com.badlogic.gdx.graphics.g2d TextureAtlas SpriteBatch Sprite]
           [com.badlogic.gdx.graphics OrthographicCamera Color GL20]
           [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer$ShapeType]))

(defn update-gui
  [root {:keys [tiles path tiles-window] :as obj}]
  (let [brush (get-in root [:map-editor :brush])
        input (:input root)
        viewport (:fit-viewport root)
        touch-diagnostic (first (.-touchDiagnostic input))
        touch-released? (.touchReleased touch-diagnostic)
        touch-pressed? (.touchPressed touch-diagnostic)
        coordinate (.getCoordinatePressed_raw touch-diagnostic);(.getCoordinatePressed touch-diagnostic viewport);(.getCoordinateReleased touch-diagnostic viewport)
        x (.x coordinate)
        y (.y coordinate)
        coordinate-unproject (.unproject (:orthographic-camera (get-in root [:map-editor :gui])) (let [vec3 (:vec3 (:map-editor root))]
                                                                                                   (set! (.-x vec3) x)
                                                                                                   (set! (.-y vec3) y)
                                                                                                   (set! (.-z vec3) 0)
                                                                                                   vec3))
        x (.x coordinate-unproject)
        y (.y coordinate-unproject)
        filtered-tile (filter (fn [tile]
                        (if (and touch-released? (.contains (.getBoundingRectangle (:sprite tile)) x y))
                          tile)) tiles)
        brush (if (empty? filtered-tile)
                brush
                (assoc brush :brush-data (:brush-data (first filtered-tile))))

        ;window drag ;;;;;;;;;;;;;;;;;;;;;;;;;;
        ttd (:tiles-tab-drag tiles-window)
        ttd-pos (:position ttd)
        ttd-x (first ttd-pos)
        ttd-y (second ttd-pos)
        ttd-w (:width ttd)
        ttd-h (:height ttd)
        ttd-contains-point? (and
                       (and (> x ttd-x) (< x (+ ttd-x ttd-w)))
                       (and (> y ttd-y) (< y (+ ttd-y ttd-h))))

        ;inner window drag ;;;;;;;;;;;;;;;;;;;;;;;;;;
        tiw (:tiles-inner-window tiles-window)
        tiw-pos (:position tiw)
        tiw-x (first tiw-pos)
        tiw-y (second tiw-pos)
        tiw-w (:width tiw)
        tiw-h (:height tiw)
        tiw-contains-point? (and
                              (and (> x tiw-x) (< x (+ tiw-x tiw-w)))
                              (and (> y tiw-y) (< y (+ tiw-y tiw-h))))

        ;window scale ;;;;;;;;;;;;;;;;;;;;;;;;; TODO work on scaling later, it's more complex than I initially thought
        ttr (:tiles-tab-resize tiles-window)
        ttr-pos (:position ttr)
        ttr-x (first ttr-pos)
        ttr-y (second ttr-pos)
        ttr-w (:width ttr)
        ttr-h (:height ttr)
        ttr-contains-point? (and
                              (and (> x ttr-x) (< x (+ ttr-x ttr-w)))
                              (and (> y ttr-y) (< y (+ ttr-y ttr-h))))

        disp (.dragDisplacement touch-diagnostic);(.getCoordinatePressed touch-diagnostic viewport);(.getCoordinateReleased touch-diagnostic viewport)

        ;update brush
        root (update-in root [:map-editor :brush] (fn [_] brush))

        ;update dragging state
        root (update-in root [:map-editor :gui :tiles-window :tiles-tab-drag :dragging?]
                        (fn [d?]
                          (cond touch-released? false
                                :default (cond touch-pressed? ttd-contains-point?
                                               :default d?))))
        ]

    ;(println "tiw contains point? " tiw-contains-point?)
    ;(println "ttr contains point? " ttr-contains-point?)
    ;(println (.dragDisplacement touch-diagnostic))
    ;(println "contains-point?" ttd-contains-point?)
    ;(println "TTD-DRAG: " ttd-drag?)
    ;(println "x: " x)
    ;(println "y: " y)
    ;(println (:brush-data brush))

    ;update inner window displacement
    ;loop through tile sprites and apply drag vector
    (if (and
          (.touchDragged touch-diagnostic)
          tiw-contains-point?
          ;due to the cursor glitch, we need to check if the window itself is not being dragged.
          ;other wise we get double influence when the cursor 'bleeds' into the inner window.
          (not (get-in tiles-window [:tiles-tab-drag :dragging?])));(get-in tiles-window [:tiles-tab-drag :dragging?])
      (doseq [tile (get-in root [:map-editor :gui :tiles])]
        (.translate (:sprite tile) (.-x disp) (.-y disp))))

    ;update window displacement
    (if (and (.touchDragged touch-diagnostic) (get-in tiles-window [:tiles-tab-drag :dragging?]))
      (let [root (update-in root [:map-editor :gui :tiles-window :tiles-inner-window :position]
                         (fn [p]
                           [(+ (.-x disp) (first p)) (+ (.-y disp) (second p))]
                           ;[(first p) (second p)]
                           ))
            root (update-in root [:map-editor :gui :tiles-window :tiles-tab-drag :position]
                            (fn [p]
                              [(+ (.-x disp) (first p)) (+ (.-y disp) (second p))]
                              ;[(first p) (second p)]
                              ))
            root (update-in root [:map-editor :gui :tiles-window :tiles-tab-resize :position]
                            (fn [p]
                              [(+ (.-x disp) (first p)) (+ (.-y disp) (second p))]
                              ;[(first p) (second p)]
                              ))
            ]

        ;loop through tile sprites and apply drag vector
        (doseq [tile (get-in root [:map-editor :gui :tiles])]
          (.translate (:sprite tile) (.-x disp) (.-y disp)))

        ;return result of applying drag!
        root)
      root
      )

    ;update window scale TODO


    ))
(defn render-gui
  [root {:keys [orthographic-camera
                sprite-batch
                tiles
                tiles-window] :as obj}]

  (let [shape-renderer (:shape-renderer root)
        color (:color tiles-window)

        tiles-inner-window (:tiles-inner-window tiles-window)
        tiw-position (:position tiles-inner-window)
        tiw-x (first tiw-position)
        tiw-y (second tiw-position)
        tiw-width (:width tiles-inner-window)
        tiw-height (:height tiles-inner-window)

        tiles-tab-drag (:tiles-tab-drag tiles-window)
        ttd-position (:position tiles-tab-drag)
        ttd-x (first ttd-position)
        ttd-y (second ttd-position)
        ttd-width (:width tiles-tab-drag)
        ttd-height (:height tiles-tab-drag)

        tiles-tab-resize (:tiles-tab-resize tiles-window)
        ttr-position (:position tiles-tab-resize)
        ttr-x (first ttr-position)
        ttr-y (second ttr-position)
        ttr-width (:width tiles-tab-resize)
        ttr-height (:height tiles-tab-resize)]
    ;render tile-frame
    (.setProjectionMatrix shape-renderer (.-combined orthographic-camera))
    ;(.setProjectionMatrix shape-renderer (.-combined camera))
    (.glEnable Gdx/gl GL20/GL_BLEND)
    (.glBlendFunc Gdx/gl GL20/GL_SRC_ALPHA GL20/GL_ONE_MINUS_SRC_ALPHA)
    (.begin shape-renderer ShapeRenderer$ShapeType/Line)
    (.setColor shape-renderer color)
    ;render window tab
    (.rect shape-renderer ttd-x ttd-y ttd-width ttd-height)
    ;render window body
    (.rect shape-renderer tiw-x tiw-y tiw-width tiw-height)
    ;render window resize
    (.rect shape-renderer ttr-x ttr-y ttr-width ttr-height)
    (.end shape-renderer)
    (.glDisable Gdx/gl GL20/GL_BLEND)

    ;render tiles
    (.setProjectionMatrix sprite-batch (.-combined orthographic-camera))
    ;(.glEnable Gdx/gl GL20/GL_SCISSOR_TEST)
    ;(.glScissor Gdx/gl tiw-x tiw-y tiw-width tiw-height)
    (.begin sprite-batch)

    (doseq [tile tiles]
      ;TODO why is sprite-batch nil? it doesn't even seem to matter lol...
      ;(println "sprite: " (:sprite tile))
      ;(println "spritebatch: " (:sprite-batch tile))
      (.draw (:sprite tile) sprite-batch))
    (.end sprite-batch)
    ;(.glDisable Gdx/gl GL20/GL_SCISSOR_TEST)
    )

  root
  )

(defn create-gui-tile
  [root position symbol-ns symbol-fn symbol-name path]
  (let [obj {:sprite (let [sprite (.createSprite
                                    (.get (get-in root [:asset-manager])
                                          "resources/texture_atlas/terrain/terrain_atlas.atlas" TextureAtlas)
                                    (str symbol-name))]
                       (.setPosition sprite (first position) (second position))
                       sprite)
             :path [:map-editor :gui :tiles]

             :brush-data {:name symbol-name
                          :ns symbol-ns
                          :fn symbol-fn
                          :path path}}]
    obj)) ; ;(update-in root [:map-editor :gui :tiles] obj)

(defn gui
  [root]
  (let [gui (check-object {:name                :gui
                           :path                [:map-editor :gui]
                           :type                [:editor]
                           :proc                [update-gui render-gui]

                                                                                               ;the 3 symbols are [namespace of where the function resides] [the function's name] and [the type of tile we want to generate from the function]
                                                                                               ;this information is used by the brush in the map-editor
                           :tiles               [(create-gui-tile root [0.0 0.0]               'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-bottom-left   [:game :terrain-grass])
                                                 (create-gui-tile root [16.0 0.0]              'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-bottom-mid    [:game :terrain-grass])
                                                 (create-gui-tile root [(* 2 16.0) 0.0]        'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-bottom-right  [:game :terrain-grass])

                                                 (create-gui-tile root [0.0 16.0]              'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-mid-left      [:game :terrain-grass])
                                                 (create-gui-tile root [16.0 16]               'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-mid-mid       [:game :terrain-grass])
                                                 (create-gui-tile root [(* 2 16.0) 16]         'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-mid-right     [:game :terrain-grass])

                                                 (create-gui-tile root [0.0 (* 2 16.0)]        'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-top-left      [:game :terrain-grass])
                                                 (create-gui-tile root [16.0 (* 2 16.0)]       'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-top-mid       [:game :terrain-grass])
                                                 (create-gui-tile root [(* 2 16.0) (* 2 16.0)] 'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-top-right     [:game :terrain-grass])

                                                 (create-gui-tile root [(* 3 16.0) 0.0]        'snake-game.object.terrain.grass.terrain-grass-generator 'terrain-grass-generator 'terrain-grass-single        [:game :terrain-grass])

                                                 ]
                           :orthographic-camera (new OrthographicCamera 800 480)
                           :sprite-batch        (new SpriteBatch)
                           :tiles-window (let [position [0.0 0.0]
                                               width 200.0
                                               height 200.0

                                               resize-size 15.0
                                               tiles-tab-resize {:position [(- width resize-size) (second position)]
                                                                 :width resize-size
                                                                 :height resize-size
                                                                 :scaling? false}
                                               window {:tiles-tab-drag {:position [0.0 height]
                                                                        :width width
                                                                        :height 10.0
                                                                        :dragging? false}
                                                       :tiles-inner-window  {:position position
                                                                             :width width
                                                                             :height height}
                                                       :tiles-tab-resize tiles-tab-resize
                                                       :color (new Color 1.0 1.0 1.0 1.0)
                                                       }]
                                           window)
                           })
        ]
    gui));