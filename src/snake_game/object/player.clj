(ns snake-game.object.player
  (:use pixel-perversion-engine.object.object)
  (:import [com.pixel_perversion_engine.spine Spine]
           [com.pixel_perversion_engine.box2d SpineBox2dController]
           [com.badlogic.gdx Gdx]))

(defn change-direction
  [root {:keys [path box2d-controller] :as object}]
  (let [force 1]
    (cond (.keyPressed (:input root) \D)
          (do (.setLinearVelocity (.getController box2d-controller) (float force) (float 0))
              root)

          (.keyActive (:input root) \A)
          (do (.setLinearVelocity (.getController box2d-controller) (float (* -1 force)) (float 0))
              root)

          (.keyPressed (:input root) \W)
          (do (.setLinearVelocity (.getController box2d-controller) (float 0) (float force))
              root)

          (.keyPressed (:input root) \S)
          (do (.setLinearVelocity (.getController box2d-controller) (float 0) (float (* -1 force)))
              root)

          :default root
          )))

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

(defn player
  [root]
  (let [spine (new Spine
                   (get-in root [:render])
                   (get-in root [:asset-manager]) "play/linear", "play/json/spider", 0, 0, 0.01)]
    (.setAnimation spine 0 "run" true)


    {
     :name             :player
     :type             [:player]
     :path             [:game :player] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc             [spine-box2d-copyPos!! change-direction update-timeaaa];

     :position         [0 0]
     :direction        [1 0]
     ;create a spine object and set it's animation to 'run' and loop.
     :spine            spine
     :box2d-controller (new SpineBox2dController (get-in root [:game :box2d-world]) spine "collider_controller" "root")
     :time 0.0
     }))

(comment
  (.debug_Spine (get-in @root-atomic [:render])
                (get-in @root-atomic [:render])
                (get-in @root-atomic [:fit-viewport])
                (.-drawables (get-in @root-atomic [:render])))
  )