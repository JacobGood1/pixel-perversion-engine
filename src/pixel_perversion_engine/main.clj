(ns pixel-perversion-engine.main
  (:import [java_src Start]
           [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.badlogic.gdx.graphics GL20 Color OrthographicCamera]
           [com.badlogic.gdx Gdx]
           [com.pixel_perversion_engine.asset_manager Assets]
           [com.badlogic.gdx.utils.viewport FitViewport]
           [com.pixel_perversion_engine.render Render]
           [com.pixel_perversion_engine.spine Spine])
  (:require [pixel-perversion-engine.config])
  (:use pixel-perversion-engine.scene.scene
        test-game.scene.game
        test-game.scene.root))

(def root1 (atom root))
(def game1 (atom game))

(conj (:scenes root1) game1)

(defonce sprite-batch nil)
(defonce font nil)
(defonce a-m nil)
(defonce camera nil)
(defonce viewport nil)
(defonce renderz nil)
(defonce spider nil)

(defonce cfg
         (pixel-perversion-engine.config/new-config
           :title "Age of Conan Ripoff"
           :width 800
           :height 480
           :use-gl-30 false))

(defn create []
  (def sprite-batch (new SpriteBatch))
  (def font (new BitmapFont))
  (.setColor font Color/RED)

  (def a-m (new Assets))

  (def camera (new OrthographicCamera (float 800) (float 480)))
  (def viewport (new FitViewport (float 800) (float 480) camera))
  (def renderz (new Render))
  (def spider (new Spine a-m "play/linear", "play/json/spider", (float 0), (float 0), (float 1)))
  (.add (.-spineDrawable renderz)
        spider ;0.005
        0)

  (.setAnimation spider 0 "run" true)
  )


(defn dispose []
  (.dispose sprite-batch)
  (.dispose font))

(defn render []
  (.glClearColor Gdx/gl 0 0 0 1)
  (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)
  (.begin sprite-batch)
  (.draw font sprite-batch "Hello world" (float 200) (float 200))
  (.end sprite-batch)

  (update! root1)
  ;(println (:entities @game1))

  (.update spider)
  (.update viewport (float 800) (float 480))
  (.draw (.-spineDrawable renderz) renderz viewport)
  )

(defn resize [width height])
(defn pause [])
(defn resume [])

(Start/start
  cfg
  create
  dispose
  render
  resize
  pause
  resume)

(doseq [x '[create dispose render resize pause resume]]
  (add-watch (resolve x)
             (keyword x)
             (fn [k r ov nv]
               (println "reloaded!")
               (Start/reLoad create
                             dispose
                             render
                             resize
                             pause
                             resume))))