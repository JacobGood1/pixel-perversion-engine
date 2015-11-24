(ns pixel-perversion-engine.render.render
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.pixel_perversion_engine.render Spine]
           [com.badlogic.gdx.utils Array])
  (require [pixel-perversion-engine.render.spine :as spine]
           [pixel-perversion-engine.render.sprite :as sprite]))

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
      (doseq [batch indices-batched]
          (cond (empty? batch) nil
                :default (let [spine 'spine/render
                             sprite 'sprite/render
                             batchf (first batch)]
                         ((cond (= :spine (batchf :type)) spine/render
                                :default sprite/render)
                           (:render root) ;Spine/draw ;spine/render
                           (:fit-viewport root)
                           ;com.pixel_perversion_engine.spine.Spine
                           ;(into-array com.pixel_perversion_engine.spine.Spine [(:renderable i)])
                           batch
                           ((:shader (first batch)) (:shaders root)));(:greyscale (:shaders root))) ;(:shader (first batch))
                         )))
     ;)

    indices-batched
    )
  )

(defn render-text
  [root text x y]
  (.draw (get-in root [:bitmap-font])
         (get-in root [:sprite-batch])
         text (float x) (float y)))



;render performance bottle-kneck
(comment
  (loop [batch indices-batched]
    (let [batch-f (first batch)]
      (cond (empty? batch) nil
            :default (let [spine 'spine/render
                           sprite 'sprite/render
                           batch-ff (first batch-f)]
                       ((cond (= :spine (batch-ff :type)) spine/render
                              :default sprite/render)
                         (:render root) ;Spine/draw ;spine/render
                         (:fit-viewport root)
                         ;com.pixel_perversion_engine.spine.Spine
                         ;(into-array com.pixel_perversion_engine.spine.Spine [(:renderable i)])
                         batch-f
                         ((:shader (first batch-f)) (:shaders root)));(:greyscale (:shaders root))) ;(:shader (first batch))
                       (recur (rest batch))))))
  )