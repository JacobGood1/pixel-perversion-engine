(ns pixel-perversion-engine.scene.scene)

(def scene {:counter 0
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



(defn- all-message-handler
  [message {:keys [entities messages] :as scene}]
  (assoc (assoc scene
             :entities
             (into {}
                   (map (fn [[pos e]]
                          [pos (message e)])
                        entities)))
      :messages
      (pop messages)))

(defn- select-message-handler
  [recievers message {entities :entities :as scene}]
  (loop [[key & keys :as recievers] recievers
         scene scene]
    (if (seq recievers)
      (if (key entities)
        (recur keys
               (assoc scene
                 :entities
                 (assoc entities key (message (key entities)))))
        (recur keys scene))
      (assoc scene :messages (pop (:messages scene))))))


(defn- apply-current-message
  [message-type message scene]
  (if (= (first message-type) :all)
      (all-message-handler message scene)
      (select-message-handler message-type message scene)))

(defn apply-messages
  "will apply all messages in the :messages que and then empties it out"
  [{:keys [messages] :as scene}]
  (if (> (count messages))
    (loop [[[message-type message] & messages] messages
           scene scene]
      (if message-type
        (recur messages
               (apply-current-message message-type message scene))
        scene))
    scene))



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
  (swap! scene (fn [s] (-> s
                           apply-processors-to-entities
                           apply-messages))))

(defn update-scenex
  [scene]
  (-> scene
      apply-processors-to-entities
      apply-messages))

(defn kek
  [scene u-s]
  (if (seq (:scene scene))
    (assoc
      ;(update-in scene [:v] (fn [v] (* 10 v)))
      (u-s scene)
      :scene (vec (for [s (:scene scene)] (kek s u-s))))
    ;(update-in scene [:v] (fn [v] (* 10 v)))
    (u-s scene)
    ))

