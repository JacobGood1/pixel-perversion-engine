(ns pixel-perversion-engine.process.processor)

(defn contains-keys?
  "Checks if this entity contains specified keys."
  [entity & keys]
  (loop [[key & rest-keys :as keys] keys]
    (cond (empty? keys) true
          (contains? entity key) (recur rest-keys)
          :default false)))

(defmacro make-processor
  [name required-keys code]
  (let [entity 'entity
        ;code 'code
        ;name 'name
        ]
    `(defn ~name [~entity] (if (contains-keys? ~entity ~@required-keys)
                             (let [{:keys [~@(map (fn [s] (symbol (apply str (rest (str s))))) required-keys)]} ~entity]
                               ~code)
                             ~entity))))

(comment (defmacro make-processor
  [name required-keys code]
  (let [entity 'entity]
    `(defn ~name [~entity] (if (contains-keys? ~entity ~@required-keys)
                             ~code
                             ~entity)))))




(comment
  (make-processor                                                  ;example
    make-invisible                                                 ;name
    [:spine :box2d :render]                                        ;required-keys
    (update-in entity [:spine] (fn [spine]
                                 (assoc spine :a (:x box2d)))))    ;code
  )

(comment
  (make-processor             ;example
    make-invisible            ;name
    [:spine :box2d :render]   ;required-keys
    (dissoc entity :render))  ;code
  )