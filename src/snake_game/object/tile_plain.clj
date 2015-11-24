(ns snake-game.object.tile-plain
  (:import [com.badlogic.gdx.graphics.g2d TextureAtlas])
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

(defn tile-plain
  [root position]
  (let [size 32
        sprite (.createSprite
                 (.get (get-in root [:asset-manager])
                       "resources/texture_atlas/terrain/terrain_atlas.atlas" TextureAtlas)
                 "terrain_grass")]
    {:name     :tile-plain
     :type     :tile
     :size     32
     :path     [:game :tile-plain] ;<- path should be calculated dynamically when attached to app heiarchy!
     :proc     [ render-obj];update-position
     :render   {:render-layer 5;(:midground layers)
                :render-index 0
                :type :sprite
                :renderable sprite
                :shader :normal}

     :position position
     :sprite   sprite
     }
    ))