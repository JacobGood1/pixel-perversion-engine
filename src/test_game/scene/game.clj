(ns test-game.scene.game
  (:use pixel-perversion-engine.scene.scene
        pixel-perversion-engine.entity.entity
        test-game.entity.player
        pixel-perversion-engine.component.timer
        pixel-perversion-engine.processor.processor))

(def game (-> scene
              (attach-processors [update-timer
                                  ])
              (attach-entity (assoc player :x 3))
              (attach-entity (assoc player :x 10))))



