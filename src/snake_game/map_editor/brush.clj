(ns snake-game.map-editor.brush
  (:require [snake-game.object.tile-plain :as tile-plain])
  (:import [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer$ShapeType]))

(defn change-state
  [root obj]
  ;update :state from UI
  )

(defn paint
  [root obj])
(defn stamp
  [root {:keys [position] :as obj}]
  (let [input (:input root)
        touch-diagnostic (first (.-touchDiagnostic input))
        touch-released? (.touchReleased touch-diagnostic)]
    ;(println position)
    (if touch-released?
      (update-in root [:game :tile-plain] (fn [tile-plains] (assoc tile-plains (keyword (gensym "tile-plain-")) (tile-plain/tile-plain root position))))
      root)))
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
  [root {:keys [position] :as brush}]
  (let [grid-size 16.0
        input (:input root)
        viewport (:fit-viewport root)
        touch-diagnostic (first (.-touchDiagnostic input))
        touch-released? (.touchReleased touch-diagnostic)
        coordinate (.getCoordinateHover touch-diagnostic viewport);(.getCoordinateReleased touch-diagnostic viewport)
        x (.x coordinate)
        y (.y coordinate)
        camera (.getCamera (:fit-viewport root))

        shape-renderer (.getShapeRenderer (:render root))]

    (.apply (:fit-viewport root) false)
    (let [snap-x (- x (mod x grid-size))
          snap-y (- y (mod y grid-size))]
      ;(println snap-x)
      (.setProjectionMatrix shape-renderer (.-combined camera))
      (.begin shape-renderer ShapeRenderer$ShapeType/Filled)
      (.rect shape-renderer snap-x snap-y 16.0 16.0)
      (.end shape-renderer)

      (update-in root [:map-editor :brush] (fn [brush] (assoc brush :position [snap-x snap-y]))))))

(defn brush
  [root]
  {:name :brush
   :type [:brush]
   :path [:map-editor :brush] ;<- path should be calculated dynamically when attached to app heiarchy!
   :proc [update-brush stamp] ;

   :state :stamp ;:paint :stamp :box-select :single-select
   :position [0.0 0.0]
   })

(comment [input (:input root)
          viewport (:fit-viewport root)
          touch-diagnostic (first (.-touchDiagnostic input))
          touch-released? (.touchReleased touch-diagnostic)
          coordinate (.getCoordinateReleased touch-diagnostic viewport)
          x (.x coordinate)
          y (.y coordinate)])