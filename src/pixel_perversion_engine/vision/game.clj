(ns pixel-perversion-engine.vision.game)

(defn dec-counter
  [vision]
  (update-in vision
             [:counter] (fn [c] (dec c))))
(defn inc-counter
  [vision]
  (update-in vision
             [:counter] (fn [c] (inc c))))

(def game-world (atom {:counter 0 :entities {}}))
