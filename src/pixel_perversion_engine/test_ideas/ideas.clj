;this is ns is for testing purposes!  test new ideas here!

(ns pixel-perversion-engine.ideas
  (:use [pixel-perversion-engine.process.processor]
        [pixel-perversion-engine.component]
        [pixel-perversion-engine.component.timer]))

(defn update-game
  [scene]
  (swap! scene
         (fn [s]
           (assoc s :entities
                    (-> s
                        :entities
                        (->> (map update-timer)))))))

