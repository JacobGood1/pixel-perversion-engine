(ns snake-game.object.player
  (:use pixel-perversion-engine.object.object)
  (:import [com.pixel_perversion_engine.spine Spine]
           [com.pixel_perversion_engine.box2d SpineBox2dController BodySimple BodySimple_Triangle Body$Type]
           [com.badlogic.gdx.physics.box2d Body]
           [com.badlogic.gdx Gdx]))

(defn reset-position
  [root {:keys [path box2d-controller] :as object}]
  (let [body (.-body box2d-controller)]
    (if (.keyPressed (:input root) \R) (.setTransform body (float 0.0) (float 1.0) (float 0.0))))
  root)

(defn movement
  [root {:keys [path box2d-controller] :as object}]
  (let [force 100
        jump-force 8
        lv-max 5
        body (.-body box2d-controller)
        body-pos (.getPosition body)
        body-x (.-x body-pos)
        body-y (.-y body-pos)
        lv (.getLinearVelocity body)
        lv-x (.-x lv)
        lv-y (.-y lv)]
    (cond (.keyActive (:input root) \D) (do
                                          (.applyForceToCenter body (float force) lv-y true))

          (.keyActive (:input root) \A) (do
                                          (.applyForceToCenter body (float (* -1 force)) lv-y true))

          :default (.setLinearVelocity body 0.0 lv-y))

    (cond (and (.keyActive (:input root) \D) (>= lv-x lv-max)) (.setLinearVelocity body lv-max lv-y)
          (and (.keyActive (:input root) \A) (<= lv-x (* -1 lv-max))) (.setLinearVelocity body (* -1 lv-max) lv-y)
          :default nil)

    (if (.keyPressed (:input root) \W)
      (do
        (.setLinearVelocity body lv-x 0.0)
        (.applyLinearImpulse body 0.0 (float jump-force) body-x body-y true)))

    root
    ))

(defn test-box2d
  [root {:keys [path box2d-controller] :as object}]
  (let [force 1]
    (cond (.keyActive (:input root) \D)
          (do (.applyForceToCenter (.getController box2d-controller) (float force) (float 0) true)
              root)

          (.keyActive (:input root) \A)
          (do (.applyForceToCenter (.getController box2d-controller) (float (* -1 force)) (float 0) true)
              root)

          :default root)))

(defn update-timeaaa
  [root {:keys [path time] :as object}]
  (update-in root path
             (fn [object]
               (update-in object [:time]
                          (fn [time] (+ time (.getDeltaTime Gdx/graphics)))))))

(defn spine-box2d-copyPos!!
  [root {:keys [path spine box2d-controller] :as object}]

  (let [pos (.getPosition (.getController box2d-controller))]
    (.setX spine (.-x pos))
    (.setY spine (.-y pos))
    (.update spine)

    root))

(defn update-spine!!
  [root {:keys [path spine] :as object}]
  (.update spine)

  root)

(defn render-obj
  [root obj]
  (let [r-l (get-in obj [:render :render-layer])
        r-i (get-in obj [:render :render-index])
        r   (get-in obj [:render :renderable])
        s   (get-in obj [:render :shader])]

    (add-to-layer root r-l r-i (:render obj))))

(defn player-comment
  [root]
  (let [spine (new Spine
                   (get-in root [:asset-manager]) "play/linear", "play/json/spider", 0, 0, 1.0)];0.01
    (.setAnimation spine 0 "run" true)

    {
     :name             :player
     :type             [:player]
     :path             [:game :player] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc             [render-obj spine-box2d-copyPos!! movement update-timeaaa];

     :render           {:render-layer 2;(:midground layers)
                        :render-index 0
                        :type :spine
                        :renderable spine
                        :shader :greyscale}

     :position         [0 0]
     :direction        [1 0]
     ;create a spine object and set it's animation to 'run' and loop.
     :spine            spine
     :box2d-controller (new SpineBox2dController (get-in root [:game :box2d-world]) spine "collider_controller" "root")
     :time 0.0
     }))

(defn player
  [root]
  (let []

    ;test platform
    (new BodySimple (get-in root [:game :box2d-world]) 0.0 -3 2.0 2.0 "temp-platform" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         )
    ;test slope
    (new BodySimple_Triangle (get-in root [:game :box2d-world]) -1.5 -2.0 2.0 2.0 "temp-platform" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         )

    {
     :name       :player
     :type       [:player]
     :path       [:game :player] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc       [movement update-timeaaa reset-position] ;

     :position   [0 0]
     :direction  [1 0]
     :box2d-controller

                 (new BodySimple (get-in root [:game :box2d-world]) 0.0 0.0 0.8 1.2 "player" (Body$Type/Dynamic)
                      ;begin contact
                      (fn [this other]
                        (let [name (.-name this)
                              body (.-body this)
                              lv (.getLinearVelocity body)
                              ly (.-y lv)]
                          ;body.setGravityScale(0.0f);
                          ;(.setGravityScale (.-body this) (float 0.0))
                          ;(.setLinearVelocity body (float 0.0) (float 0.0))
                          (println "begin contact: " name)))
                      ;end contact
                      (fn [this other]
                        (let [name (.-name this)]
                          ;body.setGravityScale(3f * -9.18f);
                          ;(.setGravityScale (.-body this) (float 1.0))
                          (println "end contact: " name)))
                      )
     :time       0.0
     }
    ))

(comment
  (.debug_Spine (get-in @root-atomic [:render])
                (get-in @root-atomic [:render])
                (get-in @root-atomic [:fit-viewport])
                (.-drawables (get-in @root-atomic [:render])))
  )