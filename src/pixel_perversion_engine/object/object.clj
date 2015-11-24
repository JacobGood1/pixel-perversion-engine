(ns pixel-perversion-engine.object.object
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.pixel_perversion_engine.asset_manager Assets]
           [com.badlogic.gdx.graphics OrthographicCamera Color]
           [com.badlogic.gdx.utils.viewport FitViewport]
           [com.pixel_perversion_engine.render Render]
           [com.pixel_perversion_engine.input Input]))

;TODO some stuff that exists in root also exists in Render such as sprite-batch... this is very bad stuff!
(defn root
  [width height]
  (let [orthographic-camera (new OrthographicCamera (float width) (float height))
        sprite-batch (new SpriteBatch)
        bitmap-font (new BitmapFont)
        render (new Render)]
    (.setColor bitmap-font Color/RED)
    {
     :name                :root
     :asset-manager       (new Assets)
     :orthographic-camera orthographic-camera
     :fit-viewport        (new FitViewport (float 800) (float 480) orthographic-camera) ;(float (* (* 0.001 5) 800)) (float (* (* 0.001 5) 480))
     :sprite-batch        sprite-batch
     :bitmap-font         bitmap-font
     :render              render
     :input               (new Input render)
     :dispose-list        [sprite-batch bitmap-font]
     :layers              {}
     :shaders             {:normal nil}
     }
    ))

(defn check-object
  "Object Safety!
  :type [] :proc [] :data [] :object {}
  Use this function to 'type-check' your objects which will ensure it will
  function properly when it is attached to the app heiarchy."
  [{:keys [type proc data object] :as obj}]
  (cond (nil? type)   (println "Error: Object is missing it's type!")
        (nil? proc)   (println "Error: Object is missing it's processors!")
        ;(nil? data)   (println "Error: Object is missing it's data!")
        ;(nil? object) (println "Error: Object is missing it's object map!")
        :default obj))

(comment
  "Example of using check-object:
  NOTE: check-object takes the object itself! (Before it's attached to the
  app's heiarchy). This can be problematic when an object needs to
  instantiate a Java object!

  Future adjustment: Only store a symbol which will instantiate the Java object.
      this symbol will only be evaluated when the object itself is attached
      to the app heiarchy."

  (check-object {:type [:kek :lol]
                 :proc [:a :b]
                 :health 1
                 :player player
                 :zombies {}})

  ;deprecated
  (check-object {:type [:kek :lol]
                 :proc [:a :b]
                 :data [:1 :2]
                 :object {:player player}})
  )

(defn attach-object
  "Attach object to heiarchy."
  [root path object]
  ;(update-in root path conj {name object})
  (update-in root path (fn [_] object))
  )

;deprecated from attach-object
(comment
  ;check for Java objects and evalute them
  (let [spine (if (contains? object :spine) (update-in object [:spine] (fn [obj] (eval obj))))
               box2d (if (contains? object :box2d) (update-in object [:box2d] (fn [obj] (eval obj))))
               final (reduce (fn [heiarchy obj]
                               (if (not (nil? obj))
                                 (conj heiarchy obj)
                                 heiarchy))
                             object [spine box2d])
               ]
           ))

(comment
  "Example of using attach-object"
  def root {:root {:game {:earth {:goblin {:health 2}}}}}
  (attach-object root [:root :game :earth] :player {:name :player :health 1})
  => {:root {:game {:earth {:goblin {:name :goblin :health 2}, :player {:name :player :health 1}}}}})

(defn get-in-root
  [path]
  (get-in @(resolve 'root-atomic) path))

(defn add-to-layer
  [root layer-index render-index obj]
  (let [contains-layer? (contains? (:layers root) layer-index)
        root (if contains-layer?
               root
               (update-in root [:layers] (fn [layers]
                                           (assoc layers layer-index {render-index []}))))]
    (update-in root
               [:layers layer-index render-index]
               conj obj)))

(defn attach
  [root path obj]
  (let [render? (contains? obj :render)
        root (if render?
               (add-to-layer
                 root
                 (get-in obj [:render :render-layer])
                 (get-in obj [:render :render-index])
                 obj)
               root)
        ]))

;BOOKMARK update heiarchy state ---
(defn clear-renderables
  [root]
  (assoc root :layers {}))

(defn *apply-proc-to-obj
  "IMPORTANT: All procs return the root and NOT the object!"
  [root path]
  (reduce (fn [root fn] (fn root (get-in root path)))
          root
          (:proc (get-in root path))))

(defn **gen-path
  [root path]
  (map (fn [k]
         (conj path k)) (for [[k v_] (get-in root path)] (keyword k))))

(defn construct-paths
  [root paths]
  (loop [paths paths
         final-paths []]
    (let [path-type (first (first paths))
          path (second (first paths))]
      (cond (empty? paths) final-paths
            (= path-type :*) (recur (rest paths) (conj final-paths path))
            :default         (recur (rest paths) (apply conj final-paths (**gen-path root path)))))
    ))

(defn update-heiarchy
  [root paths]
  (let [c (construct-paths root paths)]
    ;(println c)
  (reduce (fn [root path] (*apply-proc-to-obj root path)) root c)))

(defn update!
  [root paths]
  (let [root-new (update-heiarchy (clear-renderables @root) paths)] ;(get-in root [:layers]) (clear-renderables @root)
    (swap! root conj root-new)))
;BOOKMARK update heiarchy state ---