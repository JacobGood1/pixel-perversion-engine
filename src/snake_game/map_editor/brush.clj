(ns snake-game.map-editor.brush
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer$ShapeType]
           [com.badlogic.gdx.graphics GL20 Color])
  (:use [snake-game.object.terrain.grass.terrain-grass-generator]))

;TODO all brush states are currently hardcoded to use only [:game :tile-plain]
;TODO this is for testing purposes... cuz we're still in fking alpha.. k.

(defn scan-for-tile
  [root point]
  (let [tile-plain-dict (get-in root [:game :terrain-grass])
                     ;only return the names of the found tiles by reducing their names into a vector
        tiles-found (reduce (fn [c x] (conj c (first x))) []
                            ;filter the tiles that match the position
                            (filter (fn [x]
                                      (= (:position (second x)) point))
                                    tile-plain-dict))]
    tiles-found))

(defn destroy
  [root name]
  (let [obj-group (get-in root [:game :terrain-grass])
        obj (obj-group name)
        box2d-body (.-body (:box2d-body obj))
        box2d-world (get-in root [:game :box2d-world])]
    (.destroyBody box2d-world box2d-body)
    ;(dissoc obj-group name)
    name
    ))

(defn change-state
  [root obj]
  ;update :state from UI
  )
(defn paint
  [root obj])
(defn stamp
  [root {:keys [brush-data position] :as obj}]
  (if brush-data
    (let [input (:input root)
          touch-diagnostic (first (.-touchDiagnostic input))
          touch-released? (.touchReleased touch-diagnostic)
          a-active? (.keyActive input \A)
          d-active? (.keyActive input \D)

          ;brush data
          name (:name brush-data)
          ns (:ns brush-data)
          fun (:fn brush-data)
          path (:path brush-data)]
      ;(println position)
      (cond
        ;delete tile
        (and touch-released? d-active?) (let [tiles-found (scan-for-tile root position)]
                                          (println "DELETE: " tiles-found)
                                          (update-in root
                                                     path
                                                     (fn [tile-plains]
                                                       (loop [disc tile-plains
                                                              tiles-found tiles-found]
                                                         (cond (empty? tiles-found) disc
                                                               :default (recur
                                                                          (dissoc disc (destroy root (first tiles-found)))
                                                                          (rest tiles-found)))
                                                         )
                                                       ))
                                          )
        ;stamp tile
        (and touch-released? a-active?) (let [name- (keyword (gensym (str name '-))) ;generate a gensym for it's name when it get's stored within the app heiarchy.
                                              fn-ns (ns-resolve ns fun)] ;name ;resolve the symbol's namespace; ex. snake-game.object.terrain.grass.terrain_grass_single
                                          (println "STAMP: " name-)
                                          ;(println "NS: " fn-ns)
                                          (update-in root
                                                     path
                                                     (fn [obj]
                                                       (assoc obj
                                                         name-
                                                         (fn-ns root name- position name))))) ;create new tile and attach it to app heiarchy.
        ;do nothing and return root
        :default root)

      )
    root))
(defn fill
  [root obj])
(defn single-select
  [root obj])
(defn box-select
  [root obj])

(defn test-brush
  [root obj]
  (println "brush!"))

(defn update-brush
  [{:keys [fit-viewport input] :as root} {:keys [position tile-preview-color-add] :as brush}]
  (let [sprite-size 16
        box2d-size 1
        shader-offset 4]
    (set! (.-zoom (.getCamera fit-viewport)) (/ (/ box2d-size sprite-size) shader-offset));0.25
    (.apply fit-viewport false))
  (let [grid-size (get-in root [:map-editor :grid-size])
        touch-diagnostic (first (.-touchDiagnostic input))
        touch-released? (.touchReleased touch-diagnostic)
        coordinate (.getCoordinateHover touch-diagnostic fit-viewport);(.getCoordinateReleased touch-diagnostic viewport)
        x (.x coordinate)
        y (.y coordinate)
        camera (.getCamera (:fit-viewport root))

        shape-renderer (:shape-renderer root)]

    (let [snap-x (- x (mod x grid-size))
          snap-y (- y (mod y grid-size))]
      ;(println snap-x)
      (.setProjectionMatrix shape-renderer (.-combined camera))
      (.glEnable Gdx/gl GL20/GL_BLEND)
      (.glBlendFunc Gdx/gl GL20/GL_SRC_ALPHA GL20/GL_ONE_MINUS_SRC_ALPHA)
      (.begin shape-renderer ShapeRenderer$ShapeType/Line)
      (.setColor shape-renderer tile-preview-color-add)
      (.rect shape-renderer snap-x snap-y grid-size grid-size)
      (.end shape-renderer)
      (.glDisable Gdx/gl GL20/GL_BLEND)

      (update-in root [:map-editor :brush] (fn [brush] (assoc brush :position [snap-x snap-y]))))))

(defn brush
  [root]
  {:name                      :brush
   :type                      [:brush]
   :path                      [:map-editor :brush] ;<- path should be calculated dynamically when attached to app heiarchy!
   :proc                      [update-brush stamp] ;

   :state                     :stamp ;:paint :stamp :box-select :single-select
   :position                  [0.0 0.0]
   :tile-preview-color-add    (new Color 0.0 1.0 0.0 1.0)
   :tile-preview-color-delete (new Color 1.0 0.0 0.0 1.0)
   })

(comment [input (:input root)
          viewport (:fit-viewport root)
          touch-diagnostic (first (.-touchDiagnostic input))
          touch-released? (.touchReleased touch-diagnostic)
          coordinate (.getCoordinateReleased touch-diagnostic viewport)
          x (.x coordinate)
          y (.y coordinate)])