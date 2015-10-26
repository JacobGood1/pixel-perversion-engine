(ns pixel-perversion-engine.entity.entity)

(defn make-entity
  []
  )

(defn remove-components
  "Remove component(s) and return entity."
  [this & components]
  (apply dissoc (conj components this)))