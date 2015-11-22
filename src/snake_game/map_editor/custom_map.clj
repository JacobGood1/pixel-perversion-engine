(ns snake-game.map-editor.custom_map
  (:use pixel-perversion-engine.object.object))

(defn custom-map
  [root]
  (let [custom-map (check-object {:name  :data
                                  :path  [:data]
                                  :type  [:data]
                                  :proc  []
                                  :tiles []
                                  })]))