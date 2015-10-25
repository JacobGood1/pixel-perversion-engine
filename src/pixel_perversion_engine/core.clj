(ns pixel-perversion-engine.core
  (:import [java_src Start]
           [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.badlogic.gdx.graphics GL20 Color]
           [com.badlogic.gdx Gdx])
  (:require [pixel-perversion-engine.config]))

(defonce sprite-batch nil)
(defonce font nil)

(defonce cfg
         (pixel-perversion-engine.config/new-config
           :title "conan"
           :height 640
           :width 480
           :use-gl-30 false))

(defn create []
  (def sprite-batch (new SpriteBatch))
  (def font (new BitmapFont))
  (.setColor font Color/RED))


(defn dispose []
  (.dispose sprite-batch)
  (.dispose font))

(defn render []
  (.glClearColor Gdx/gl 1 1 1 1)
  (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)
  (.begin sprite-batch)
  (.draw font sprite-batch "Hello world" (float 200) (float 200))
  (.end sprite-batch))

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






