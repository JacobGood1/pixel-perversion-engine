(ns snake-game.object.player
  (:use pixel-perversion-engine.object.object)
  (:import [com.pixel_perversion_engine.spine Spine]
           [com.pixel_perversion_engine.box2d SpineBox2dController BodySimple_Square BodySimple_Triangle Body$Type BodyComplex_CharacterController Body$Category]
           [com.badlogic.gdx.physics.box2d Body BodyDef$BodyType Fixture Shape]
           [com.badlogic.gdx Gdx]
           [com.badlogic.gdx.graphics Color]
           [com.badlogic.gdx.graphics.glutils ShapeRenderer$ShapeType]
           [com.badlogic.gdx.math Vector2]))

(def contact1 nil)
(def contact2 nil)

(defn line-intersection
  [root p1 p2 p3 p4]
  (let [shape-renderer (:shape-renderer root)
        camera (.getCamera (:fit-viewport root))
        color-green (new Color 0.0 1.0 0.0 1.0)
        color-red (new Color 1.0 0.0 0.0 1.0)
        color-white (new Color 1.0 1.0 1.0 1.0)

        x1 (.-x p1) y1 (.-y p1)
        x2 (.-x p2) y2 (.-y p2)

        x3 (.-x p3) y3 (.-y p3)
        x4 (.-x p4) y4 (.-y p4)
        ua (/ (- (*(- x4 x3)(- y1 y3)) (*(- y4 y3)(- x1 x3)))
              (- (*(- y4 y3)(- x2 x1)) (*(- x4 x3)(- y2 y1))))
        cx (+ x1 (- (* ua x2)(* ua x1)))
        cy (+ y1 (- (* ua y2)(* ua y1)))]

    (.setProjectionMatrix shape-renderer (.-combined camera))
    (.begin shape-renderer ShapeRenderer$ShapeType/Line)
    (.setColor shape-renderer color-green)
    (.line shape-renderer x1 y1 x2 y2)
    (.setColor shape-renderer color-red)
    (.line shape-renderer x3 y3 x4 y4)
    (.setColor shape-renderer color-white)
    (.circle shape-renderer cx cy 0.1 100)
    (.end shape-renderer)

    [cx cy])
  )

(defn reposition-body
  [root obj]
  (if contact2 ;nil pun for contact 1
    (let [;a (.getUserData contact1)
          ;b (.getUserData contact2)
          c1-wp (.getWorldPoints (.get contact1 "body"))
          c2-wp (.getWorldPoints (.get contact2 "body"))
          pos (line-intersection root
                                 (.get c1-wp 0) (.get c1-wp 1)
                                 (.get c2-wp 0) (.get c2-wp 1)
                                 )
          half-height (/ 1.2 2)]
      (if
        (not (> (.-y (.getLinearVelocity (.-body (.get contact1 "body")))) 0.0))
        (do (.setTransform (.-body (.get contact1 "body")) (.get pos 0) (+ (.get pos 1) half-height) (float 0.0))
            (.setLinearVelocity (.-body (.get contact1 "body"))
                                (.-x (.getLinearVelocity (.-body (.get contact1 "body"))))
                                0.0))
        )
      ))
  root)

(defn reset-position
  [root {:keys [path box2d-controller] :as object}]
  (let [body (.-body box2d-controller)]
    (if (.keyPressed (:input root) \R) (.setTransform body (float 0.0) (float 1.0) (float 0.0))))
  root)

(defn movement
  [root {:keys [path box2d-controller] :as object}]
  (let [force 100
        jump-force 10
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

(defn player
  [root]
  (let []


    ;test platform
    (new BodySimple_Square (get-in root [:game :box2d-world]) 0.0 -2 1.0 1.0 "temp-platform-asd" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         (fn [this other contact manifold])
         (fn [this other contact contactImpulse])
         )
    ;test platform
    (new BodySimple_Square (get-in root [:game :box2d-world]) 1.0 -2 1.0 1.0 "temp-platform-asd" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         (fn [this other contact manifold])
         (fn [this other contact contactImpulse])
         )
    ;test platform
    (new BodySimple_Square (get-in root [:game :box2d-world]) -2.0 -1 1.0 1.0 "temp-platform-asd" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         (fn [this other contact manifold])
         (fn [this other contact contactImpulse])
         )
    ;test slope
    (new BodySimple_Triangle (get-in root [:game :box2d-world]) -1.0 0.0 1.0 1.0 "temp-slope" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         (fn [this other contact manifold])
         (fn [this other contact contactImpulse])
         )
    (new BodySimple_Triangle (get-in root [:game :box2d-world]) -2.0 1.0 1.0 1.0 "temp-slope" (Body$Type/Static)
         (fn [this other])
         (fn [this other])
         (fn [this other contact manifold])
         (fn [this other contact contactImpulse])
         )
    {
     :name      :player
     :type      [:player]
     :path      [:game :player] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc      [reposition-body movement update-timeaaa reset-position] ;

     :position  [0 0]
     :direction [1 0]
     :box2d-controller

                (new BodyComplex_CharacterController (get-in root [:game :box2d-world]) 0.0 0.0 0.8 1.2 "player" (Body$Type/Dynamic) ;width height : 0.8 1.2
                     ;begin contact
                     (fn [this other]
                       (let [
                             body (.-body (.get this "body"))
                             lv (.getLinearVelocity body)
                             ly (.-y lv)

                             this-fixture (.-main (.get this "body"))
                             filter-data (.getFilterData this-fixture)]
                         ;(if (.equals (.get other "name") "detectPlayer"))

                         (if (and (.equals (.get other "name") "slope") (.equals (.get this "name") "character_slope"))
                           (do ;(.setEnabled contact false)
                             ;remove collision from squares and slopes
                             (set! (.-maskBits filter-data) (bit-and (.getNumVal (Body$Category/Slope)) (.getNumVal (Body$Category/Square)))) ;0x0004 0x0008
                             (.setFilterData this-fixture filter-data)

                             (def contact1 this)
                             (def contact2 other)

                             ;set slope connection to true
                             (set! (.-slopeConnection (.get this "body")) true)
                             )

                         (if (and (.equals (.get other "name") "detectPlayer") (.equals (.get this "name") "character_slope"))
                           (do
                             (set! (.-maskBits filter-data) (bit-and (.getNumVal (Body$Category/Slope)) (.getNumVal (Body$Category/Square)))) ;0x0004 0x0008
                             (.setFilterData this-fixture filter-data)))
                           )



                         ))
                     ;end contact
                     (fn [this other]
                       (let [
                             body this
                             this-fixture (.-main (.get this "body"))
                             filter-data (.getFilterData this-fixture)]

                         (if (and (.equals (.get other "name") "detectPlayer") (.equals (.get this "name") "character_slope"))
                           (do
                             (if (not (.-slopeConnection (.get this "body")))
                               ;add collision for squares
                               (do
                                 (println true)
                                 (set! (.-maskBits filter-data) (.getNumVal (Body$Category/Square))) ;0x0008
                                 (.setFilterData this-fixture filter-data)
                                 )
                               (do
                                 (println false)

                                 (set! (.-slopeConnection (.get this "body")) false)
                                 )
                               )
                             ))
                         (if (and (.equals (.get other "name") "slope") (.equals (.get this "name") "character_slope"))
                           (do ;(.setEnabled contact false)
                             ;add collision for squares
                             ;(set! (.-maskBits filter-data) (.getNumVal (Body$Category/Square))) ;0x0008
                             ;(.setFilterData this-fixture filter-data)

                             ;only set contact1/2 to nil if the contact list doesn't contain a slope object
                             ;(println (.-contacts (.get this "body")))

                             (let [contacts (.-contacts (.get this "body"))
                                   contains-slope? (.checkSet (.get this "body") "temp-slope")]
                               ;(println (.get other "name"))
                               (if (not contains-slope?);(.isEmpty contacts);(not (.contains contacts (.-body (.get other "body"))))
                                 (do
                                   (def contact1 nil)
                                   (def contact2 nil)

                                   (set! (.-maskBits filter-data) (.getNumVal (Body$Category/Square))) ;0x0008
                                   (.setFilterData this-fixture filter-data)
                                   )
                                 )
                               )
                             )
                           )

                         ;reset slope connection
                         ;(set! (.-slopeConnection (.get this "body")) false)
                         ))
                     ;pre solve
                     (fn [this other contact manifold]
                       (let [;thisFixture (.getFixtureA contact)
                             ;otherFixture (.getFixtureB contact)
                             ;this (.getUserData thisFixture)
                             ;other (.getUserData otherFixture)

                             local-normal (.getLocalNormal manifold)

                             this-fixture (.-main (.get this "body"))
                             filter-data (.getFilterData this-fixture)
                             ]
                         ;(println this)
                         )
                       )
                     ;post solve
                     (fn [this other contact contactImpulse]
                       (let [thisFixture (.getFixtureA contact)
                             otherFixture (.getFixtureB contact)
                             this (.getUserData thisFixture)
                             other (.getUserData otherFixture)]

                         )
                       )
                     )
     :time      0.0
     }
    ))

(comment
  (.debug_Spine (get-in @root-atomic [:render])
                (get-in @root-atomic [:render])
                (get-in @root-atomic [:fit-viewport])
                (.-drawables (get-in @root-atomic [:render])))
  )