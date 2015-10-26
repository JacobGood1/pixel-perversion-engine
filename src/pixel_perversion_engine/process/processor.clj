(ns pixel-perversion-engine.process.processor
  (:use pixel-perversion-engine.utilities))

(defmacro make-processor
  [name required-keys & code]
  (let [entity 'e
        ;code 'code
        ;name 'name
        ]
    `(defn ~name
       [~entity]
       (if (contains-keys? ~entity ~@required-keys)
         (let [{:keys [~@(map (fn [s] (->> (-> s
                                               str
                                               rest)
                                           (apply str)
                                           symbol))
                              required-keys)]} ~entity]
           ~@code)
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