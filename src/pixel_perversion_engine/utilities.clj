(ns pixel-perversion-engine.utilities)

(defn contains-keys?
  "Checks if this entity contains specified keys."
  [entity & keys]
  (loop [[key & rest-keys :as keys] keys]
    (cond (empty? keys) true
          (contains? entity key) (recur rest-keys)
          :default false)))