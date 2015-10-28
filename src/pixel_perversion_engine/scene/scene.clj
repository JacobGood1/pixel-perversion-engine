(ns pixel-perversion-engine.scene.scene)

(def scene {:counter 0 :entities {} :processors []})

(defn apply-processors-to-entities
  [{:keys [processors entities] :as scene}]
  (assoc scene
         :entities

         ;note:
         ;map returns a seq-able map, which we then need to turn back into a map. ([[:a 1] [:b 2]] -> {:a 1, :b 2})
         (into {}

               (loop [[proc & procs :as processors] processors
                      e entities]
                 (if (seq processors)
                   (recur procs

                          ;note:
                          ;map will convert this map to a seq. we then destructure the seq and apply proc to the entity.
                          ;and return the data back as it came in.
                          (map (fn [[c e]] [c (proc e)])
                               e))
                   e)))))

(defn inc-counter
  [scene]
  (update-in scene
             [:counter] (fn [c] (inc c))))

(defn update!
  [scene]
  (swap! scene apply-processors-to-entities))