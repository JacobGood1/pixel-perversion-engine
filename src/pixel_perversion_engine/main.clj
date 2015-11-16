(ns pixel-perversion-engine.main
  (:import [java_src Start]
           [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.badlogic.gdx.graphics GL20 Color OrthographicCamera]
           [com.badlogic.gdx Gdx]
           [com.pixel_perversion_engine.asset_manager Assets]
           [com.badlogic.gdx.utils.viewport FitViewport]
           [com.pixel_perversion_engine.render Render]
           [com.pixel_perversion_engine.spine Spine]
           [com.badlogic.gdx.physics.box2d World]
           [com.pixel_perversion_engine.box2d SpineBox2dController]
           [com.badlogic.gdx.math Vector2]
           [com.badlogic.gdx.graphics.glutils ShaderProgram])
  (:require [pixel-perversion-engine.config])
  (:use
    ;pixel-perversion-engine.scene.scene
    pixel-perversion-engine.render.render
    pixel-perversion-engine.object.object
    snake-game.object.main-menu
    snake-game.object.game
    snake-game.object.player
    ))

(defonce root-atomic nil)

(defonce cfg
         (pixel-perversion-engine.config/new-config
           :title "Age of Conan Ripoff"
           :width 800
           :height 480
           :use-gl-30 false))

(def proc_path [
                ;[:main-menu]

                ;game elements
                [:game :player]
                 ;update game last (box2d step)
                [:game]
                ])

(def vertexShader-BW nil)
(def fragmentShader-BW nil)
(def shaderProgram nil)

(defn create []
  (let [root (root 800 480)
        game-attached (game root) ;(attach-object root [:game] (game root))
        main-menu-attached (main-menu game-attached) ;(attach-object game-attached [:main-menu] (main-menu root))
        root-final main-menu-attached
        ]
    (def root-atomic (atom root-final)))

  (def vertexShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/vertex.glsl")))
  (def fragmentShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/fragment.glsl")))
  (def shaderProgram (new ShaderProgram vertexShader-BW fragmentShader-BW))
  (set! (.-shaderProgram (.-spineDrawable (get-in @root-atomic [:render]))) shaderProgram)
  (set! (.-useShader (.-spineDrawable (get-in @root-atomic [:render]))) true)
  )


(defn dispose []
  (let [dispose-seq (for [obj (get-in @root-atomic [:dispose-list])] obj)]
    (map (fn [obj] (.dispose obj)) dispose-seq))

  ;(println "ALL OBJECTS DISPOSED!")
  )

(defn render []
  (.glClearColor Gdx/gl 0 0 0 1)
  (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

  ;update all app logic
  (update! root-atomic proc_path)

  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float 800) (float 480))

  ;render result
  (.begin (get-in @root-atomic [:sprite-batch]))
  (render-text @root-atomic "Hello world." 200 200)
  (.end (get-in @root-atomic [:sprite-batch]))

  (.draw (.-spineDrawable (get-in @root-atomic [:render]))
         (get-in @root-atomic [:render])
         (get-in @root-atomic [:fit-viewport]))
  )

(defn resize [width height]
  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float width) (float height)))
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