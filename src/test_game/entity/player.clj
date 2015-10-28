(ns test-game.entity.player
  (:require [pixel-perversion-engine.component.timer :as timer]))

(def player
  {:name :player
   :type [:entity]
   :timer (timer/create-timer 1 true)
   })