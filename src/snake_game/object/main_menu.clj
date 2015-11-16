(ns snake-game.object.main-menu
  (:use pixel-perversion-engine.object.object))

(defn main-menu
  [root]
  (assoc root :main-menu (check-object {
                                        :name :main-menu
                                        :path [:main-menu]
                                        :type [:menu :main-menu]
                                        :proc []
                                        })))