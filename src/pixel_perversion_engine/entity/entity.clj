(ns pixel-perversion-engine.entity.entity
  (:use [pixel-perversion-engine.utilities]
        [pixel-perversion-engine.vision.game
         :only [dec-counter inc-counter]]))

(defn make-entity
  "assumes vision contains :entities"
  [scene entity]
  (let [pos (keyword (str (:counter scene)))
        entity (assoc entity :pos pos)]
    (-> scene
        inc-counter
        (update-in [:entities]
                   (fn [e]
                     (assoc e pos entity))))))

(defn remove-entity
  "args [scene entity]"
  [{entities :entities :as scene} {pos :pos}]
  (assoc scene :entities (dissoc entities pos)))

(defn remove-components
  "Remove component(s) and return entity."
  [this & components]
  (apply dissoc (conj components this)))