(ns pixel-perversion-engine.component.timer
  (:use [pixel-perversion-engine.component]
        [pixel-perversion-engine.process.processor]))

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
;
(make-processor update-timer [:timer]
                (update-in e [:time]
                           (fn [time]
                             (inc time))))