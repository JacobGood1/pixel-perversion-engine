(ns pixel-perversion-engine.component.spine.spine)

(defn spine
  []
  (let [this {:type [:spine]}]

    ;extend from component
    (extend-component pixel-perversion-engine.component)
    ))