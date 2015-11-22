(ns pixel-perversion-engine.main
  (:import [java_src Start]
           [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont]
           [com.badlogic.gdx.graphics GL20 Color OrthographicCamera Texture]
           [com.badlogic.gdx Gdx]
           [com.pixel_perversion_engine.asset_manager Assets]
           [com.badlogic.gdx.utils.viewport FitViewport]
           [com.pixel_perversion_engine.render Render]
           [com.pixel_perversion_engine.spine Spine]
           [com.badlogic.gdx.physics.box2d World]
           [com.pixel_perversion_engine.box2d SpineBox2dController]
           [com.badlogic.gdx.math Vector2 Vector3]
           [com.badlogic.gdx.graphics.glutils ShaderProgram ShapeRenderer$ShapeType]
           [com.pixel_perversion_engine.shader TestShader_NMap]
           [com.pixel_perversion_engine.tests Cube_3D]
           [org.lwjgl.input Mouse])
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
                [:game :player2]
                 ;update game last (box2d step)
                [:game]
                ])

(def layers {:background 0 :midground 1 :foreground 2})

(def vertexShader-BW nil)
(def fragmentShader-BW nil)
(def shaderProgram nil)
(def testShader_NMap nil)
(def testCube_3D nil)
(defonce vec3 nil)

(defn create []
  (def vertexShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/vertex.glsl")))
  (def fragmentShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/fragment.glsl")))
  (def shaderProgram (new ShaderProgram vertexShader-BW fragmentShader-BW))

  (let [root (root 800 480)
        game-attached (game root) ;(attach-object root [:game] (game root))
        main-menu-attached (main-menu game-attached) ;(attach-object game-attached [:main-menu] (main-menu root))
        root main-menu-attached

        ;attach shaders
        root (assoc root :shaders {:greyscale shaderProgram})
        ]
    (def root-atomic (atom root)))


  ;(set! (.-shaderProgram (.-spineDrawable (get-in @root-atomic [:render]))) shaderProgram)
  ;(set! (.-useShader (.-spineDrawable (get-in @root-atomic [:render]))) true)

  (def testShader_NMap (new TestShader_NMap
                            (new Texture (.internal Gdx/files "resources/cmap/cmap_brickwall.png"))
                            (new Texture (.internal Gdx/files "resources/nmap/nmap_brickwall.png"))))
  (def testCube_3D (new Cube_3D))
  (def vec3 (new Vector3)))

(defn render []
  (.glClearColor Gdx/gl 0 0 0 1)
  (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

  (.render testShader_NMap)

  ;update all app logic
  (update! root-atomic proc_path)

  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float 800) (float 480))

  ;render result
  (.begin (get-in @root-atomic [:sprite-batch]))
  (render-text @root-atomic "Hello world." 200 200)
  (.end (get-in @root-atomic [:sprite-batch]))

  (render-all @root-atomic)

  (.render testCube_3D)

  (let [grid-size 32.0
        x (.getX Gdx/input);(Mouse/getX);(/ (Mouse/getX) (float (.getWidth Gdx/graphics)))
        y (.getY Gdx/input);(Mouse/getY);(/ (Mouse/getY) (float (.getHeight Gdx/graphics)))
        camera (.getCamera (:fit-viewport @root-atomic))

        shape-renderer (.getShapeRenderer (:render @root-atomic))]
    ;(println (mod x grid-size))
    ;(println [snap-x snap-y])
    ;(println (.getY Gdx/input))
    (set! (.-x vec3) x)
    (set! (.-y vec3) y)
    (.unproject camera vec3)
    (let [x (.-x vec3)
          y (.-y vec3)
          snap-x (- x (mod x grid-size))
          snap-y (- y (mod y grid-size))]
      (println snap-x)
      (.setProjectionMatrix shape-renderer (.-combined camera))
      (.begin shape-renderer ShapeRenderer$ShapeType/Filled)
      (.rect shape-renderer snap-x snap-y 32.0 32.0)
      (.end shape-renderer)
      ))
  )

(defn resize [width height]
  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float width) (float height)))
(defn pause [])
(defn resume [])

(defn dispose []
  (let [dispose-seq (for [obj (get-in @root-atomic [:dispose-list])] obj)]
    (map (fn [obj] (.dispose obj)) dispose-seq))

  ;(println "ALL OBJECTS DISPOSED!")
  (.dispose testShader_NMap)
  (.dispose testCube_3D)
  )

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