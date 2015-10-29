(ns pixel-perversion-engine.scene.scene)

(def scene {:counter 0
            :entities {}
            :processors []
            :messages []})

(defn send-message
  ([scene message]
   (assoc scene
     :messages
     (conj (:messages scene) [[:all] message])))
  ([scene recievers message]
   (assoc scene
     :messages
     (conj (:messages scene) [recievers message]))))


;TODO apply-message needs to work with :all
;TODO messages need to work like a stack! pop off the end
(defn apply-message
  [{[[recievers message] _] :messages entities :entities :as scene}]
  (loop [[key & keys :as recievers] recievers
         scene scene]
    (if (seq recievers)
      (if (key entities)
        (recur keys
               (assoc scene
                 :entities
                 (assoc entities key (message (key entities)))))
        (recur keys scene))
      (assoc scene :messages []))))


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
                          (map (fn [[c e]] [c (proc scene e)])
                               e))
                   e)))))

(defn inc-counter
  [scene]
  (update-in scene
             [:counter] (fn [c] (inc c))))

(defn update!
  [scene]
  (swap! scene apply-processors-to-entities))