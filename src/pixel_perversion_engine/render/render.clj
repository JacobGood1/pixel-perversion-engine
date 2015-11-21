(ns pixel-perversion-engine.render.render
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.pixel_perversion_engine.render Spine]
           [com.badlogic.gdx.utils Array])
  (:use [pixel-perversion-engine.render.spine]))

(defn render-all
  [{:keys [layers] :as root}]

  (let [root root
        layers layers;{0 {2 [:b] 1 [:a] 3 [:c]} 5 {6 [:d] 7 [:e] 8 [:f]}};(:layers root)
        sorted-keys (sort (keys layers))

        layers-sorted (map (fn [key]
                             (layers key)) sorted-keys)
        indices-sorted (for [r-i layers-sorted] (let [sorted-keys (sort (keys r-i))
                                                      indices-sorted (map (fn [key]
                                                                            (r-i key)) sorted-keys)
                                                      ]
                                                  indices-sorted
                                                  ))

        ;result is flattened for prototyping. unflatten to preserve layer detail!
        indices-flattened (flatten indices-sorted)

        ;batch rendering
        indices-batched (loop [i-f indices-flattened
                               batch []
                               common-type nil
                               common-shader nil
                               ]

                          (cond (empty? i-f) batch
                                (nil? common-type) (recur (rest i-f) (conj batch [(first i-f)]) (:type (first i-f)) (:shader (first i-f)))
                                (and (= common-type   (:type (first i-f)))
                                     (= common-shader (:shader (first i-f)))) (recur (rest i-f)
                                                                                     (conj (drop-last batch) (conj (last batch) (first i-f))) common-type common-shader)
                                :default (recur (rest i-f) (conj batch [(first i-f)]) (:type (first i-f)) (:shader (first i-f)))
                                )
                          )
        ]
    ;indices-batched

    ;render batch
    ;(comment
      (loop [batch indices-batched]
        (let [batch-f (first batch)]
        ;(println (first (first batch)))
        (cond (empty? batch) nil
              :default (do (render (:render root) ;Spine/draw
                                   (:fit-viewport root)
                                   ;com.pixel_perversion_engine.spine.Spine
                                   ;(into-array com.pixel_perversion_engine.spine.Spine [(:renderable i)])
                                   batch-f
                                   ((:shader (first batch-f)) (:shaders root)));(:greyscale (:shaders root))) ;(:shader (first batch))
                           (recur (rest batch))))))
     ; )

    indices-batched
    )
  )

(comment
  (doseq [i indices-flattened] (cond (= (:type i) :spine)
                                     (Spine/draw (:render root)
                                                 (:fit-viewport root)
                                                 ;com.pixel_perversion_engine.spine.Spine
                                                 ;(into-array com.pixel_perversion_engine.spine.Spine [(:renderable i)])
                                                 (let [ar (new Array)]
                                                   (.add ar (:renderable i))
                                                   ar)
                                                 ((:shader i) (:shaders root))
                                                 )

                                     :default nil))
  )

(defn render-text
  [root text x y]
  (.draw (get-in root [:bitmap-font])
         (get-in root [:sprite-batch])
         text (float x) (float y)))