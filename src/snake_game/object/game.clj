(ns snake-game.object.game
  (:require [pixel-perversion-engine.object.object :as object]
            [snake-game.object.player :as player])
  (:import [com.badlogic.gdx.math Vector2]
           [com.badlogic.gdx.physics.box2d World]))

(def time-step (float 0.016))
(defn step-box2d-world
  [root {:keys [box2d-world] :as object}]
  (.step box2d-world time-step 6 6)
  root)

(defn game
  [root]

  ;game creates a player. and player RELIES on game to already have been created for it's box-2d world.
  ;player also searches for :game THROUGH root. this means that game should attach itself to root,
  ;then attach player to itself (:game).
  (let [
        ;create game object
        box2d-world (new World (new Vector2 (float 0) (float 0)) false) ;gravity -9.18
        game (object/check-object {
                                   :name         :game
                                   :path         [:game]
                                   :type         [:game]
                                   :proc         [step-box2d-world]
                                   :box2d-world  box2d-world

                                   ;:player       (player/player root)
                                   ;:player2      (player/player2 root)
                                   :tile-plain   {};(tile-plain/tile-plain root [0.0 0.0])
                            })

        ;attach game to root
        root (assoc root :game game)
        ;update dispose list for box2d-world
        root (update-in root [:dispose-list] conj box2d-world)
        ;attach player to game

        ;testing player
        ;root (update-in root [:game :player] (fn [_] (player/player root)))
        ;root (update-in root [:game :player2] (fn [_] (player/player2 root)))

        ;testing tile
        ;root (update-in root [:game :tile-plain] (fn [_] (tile-plain/tile-plain root [0.0 0.0])))
        ;return result
        ]
    root
    ))