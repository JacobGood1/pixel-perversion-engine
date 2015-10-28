(ns pixel-perversion-engine.component.timer
  (:use [pixel-perversion-engine.component.component]
        [pixel-perversion-engine.processor.processor]))

;BOOKMARK Maybe components should implement callbacks?

(defn create-timer
  [maximum-time loop?]
  (extend-component
    {:name :timer
     :type [:timer]
     :time 0.0
     :maximum-time maximum-time
     :loop? loop?
     :timer-in-progress? true
     :timer-finished? false}))

(def update-timer)
(def e)
(make-processor update-timer [:timer]
                (update-in e [:timer :time]
                           (fn [time]
                             (inc time))))