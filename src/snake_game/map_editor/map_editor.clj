(ns snake-game.map-editor.map_editor
  (:use pixel-perversion-engine.object.object)
  (:require [snake-game.map-editor.brush :as brush]
            [snake-game.map-editor.gui :as gui])
  (:import [com.badlogic.gdx.graphics.glutils ShapeRenderer$ShapeType]
           [com.badlogic.gdx.math Vector3]))

(defn update-m
  [root {:keys [vec3] :as obj}]
  root)

(defn map-editor
  [root]
  (let [map-editor (check-object {:name  :data
                                  :path  [:data]
                                  :type  [:data]
                                  :proc  [update-m]
                                  :tiles []
                                  :brush (brush/brush root)
                                  :vec3  (new Vector3)
                                  :gui   (gui/gui root)
                                  :grid-size (float 1) ;16
                                  })
        ;attach map-editor to root
        root (assoc root :map-editor map-editor)]
    root
    ))