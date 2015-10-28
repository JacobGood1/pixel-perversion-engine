(ns pixel-perversion-engine.component.component)

(def component
  {:type [:component]})

(defn extend-component
  "Extend generic component from base component."
  [c]

  ;append :component to :type vector and return component
  (update-in c [:type]
             conj :component))

(defn add-children
  "Add child or children and return component."
  [this & children]
  (let [this this
        this-children (:children this)
        conj-children (apply conj this-children children)]
    (update-in this [:children] (fn [this] conj-children))))