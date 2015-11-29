(ns snake-game.object.terrain.grass.terrain-grass-generator
  (:import [com.pixel_perversion_engine.box2d BodySimple$Type BodySimple]
           [com.badlogic.gdx.graphics.g2d TextureAtlas])
  (:use pixel-perversion-engine.object.object))

(defn render-obj
  [root obj]
  (let [r-l (get-in obj [:render :render-layer])
        r-i (get-in obj [:render :render-index])
        r   (get-in obj [:render :renderable])
        s   (get-in obj [:render :shader])
        sprite (:renderable (:render obj))
        pos (:position obj)
        x (first pos)
        y (second pos)]
    ;(println [x y])
    (.setPosition sprite x y)
    (add-to-layer root r-l r-i (:render obj))))

(defn update-position
  [root {:keys [path] :as obj}]
  (update-in root path
             (fn [object]
               (update-in object [:position]
                          (fn [pos]
                            [0.0 0.0]))))
  )

(defn terrain-grass-generator
  [root name position atlas-name]
  (let [size (get-in root [:map-editor :grid-size])
        sprite (.createSprite
                 (.get (get-in root [:asset-manager])
                       "resources/texture_atlas/terrain/terrain_atlas.atlas" TextureAtlas)
                 (str atlas-name))]
    {:name       name
     :type       :tile
     :size       size
     :path       [:game :terrain-grass] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc       [render-obj] ;update-position
     :render     {:render-layer 5 ;(:midground layers)
                  :render-index 0
                  :type         :sprite
                  :renderable   sprite
                  :shader       :scanline}; :normal :scanline

     :position   position
     :sprite     sprite
     :box2d-body (new BodySimple (get-in root [:game :box2d-world]) (first position) (second position) size size (str name) (BodySimple$Type/Static))
     }
    ))