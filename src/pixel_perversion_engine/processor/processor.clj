(ns pixel-perversion-engine.processor.processor
  (:use pixel-perversion-engine.utilities))

(defmacro make-processor
  [name required-keys & code]

  ;Easy symbol generation for use within the macro.
  (let [entity 'e
        ;code 'code
        ;name 'name
        ]

    ;BOOKMARK This fn will be stored within a vector and applied to entities.
    `(defn ~name
       [~entity]

       ;key-lock paradigm.
       ;Check to see if this entity meets the requirements (has the necessary components) for this action.
       (if (contains-keys? ~entity ~@required-keys)

         ;Convert all keys to symbols, then destructure the map with those symbols.
         ;This will give us access to all the components of the entity within the code block.
         (let [{:keys [~@(map (fn [s] (->> (-> s
                                               str
                                               rest)
                                           (apply str)
                                           symbol))
                              required-keys)]} ~entity]

           ;Exectute code. It is up the developer to return the entity within this code.
           ~@code)

         ;If statement FAILED. This entity doesn't meet the component requirements.
         ;Return the un-altered entity.
         ~entity))))

(defn attach-processors
  [{:keys [s-processors] :as scene} processors]
  (assoc scene :processors (vec (apply conj s-processors processors))))

;BOOKMARK backup
(comment (defmacro make-processor
  [name required-keys code]
  (let [entity 'entity]
    `(defn ~name [~entity] (if (contains-keys? ~entity ~@required-keys)
                             ~code
                             ~entity)))))

;BOOKMARK Examples of making a processor in production.
;BOOKMARK Notice how it's the developer job to return the altered entity!
(comment
  (make-processor                                                  ;example
    make-invisible                                                 ;name
    [:spine :box2d :render]                                        ;required-keys
    (update-in entity [:spine] (fn [spine]                         ;code
                                 (assoc spine :a (:x box2d)))))
  )

(comment
  (make-processor             ;example
    make-invisible            ;name
    [:spine :box2d :render]   ;required-keys
    (dissoc entity :render))  ;code
  )