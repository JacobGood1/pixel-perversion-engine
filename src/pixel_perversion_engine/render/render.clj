(ns pixel-perversion-engine.render.render
  (:import [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]))

(defn render-text
  [root text x y]
  (.draw (get-in root [:bitmap-font])
         (get-in root [:sprite-batch])
         text (float x) (float y)))