(ns snake-game.map-editor.brush)

(defn change-state
  [root obj]
  ;update :state from UI
  )

(defn paint
  [root obj])
(defn stamp
  [root obj])
(defn fill
  [root obj])
(defn single-select
  [root obj])
(defn box-select
  [root obj])

(defn brush
  [root]
  {:name :brush
   :type [:brush]
   :path [:map-editor :brush] ;<- path should be calculated dynamically when attached to app heiarchy!
   :proc []

   :state :stamp ;:paint :stamp :box-select :single-select
   })