(ns test-game.scene.root
  (:use pixel-perversion-engine.scene.scene
        pixel-perversion-engine.entity.entity
        test-game.entity.player
        pixel-perversion-engine.component.timer
        pixel-perversion-engine.processor.processor))

(def root (-> scene
              (attach-processors [update-timer
                                  ])
              (attach-entity player)))