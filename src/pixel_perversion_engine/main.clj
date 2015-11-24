(ns pixel-perversion-engine.main
  (:import [java_src Start]
           [com.badlogic.gdx.graphics.g2d SpriteBatch BitmapFont TextureRegion TextureAtlas Sprite]
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
    )
  (:require [pixel-perversion-engine.render.spine :as spine-r]
            [snake-game.map-editor.map_editor :as map-editor]))

(defonce root-atomic nil)

(defonce cfg
         (pixel-perversion-engine.config/new-config
           :title "Age of Conan Ripoff"
           :width 800
           :height 480
           :use-gl-30 false))

(def proc-path [
                ;[:main-menu]
                ;game elements
                [:*   [:game :player]]
                [:*   [:game :player2]]
                [:**  [:game :tile-plain]]
                 ;update game last (box2d step)
                [:*   [:game]]

                ;map-editor elements
                [:*   [:map-editor :brush]]
                [:*   [:map-editor]]
                ])

(def layers {:background 0 :midground 1 :foreground 2})

(def vertexShader-BW nil)
(def fragmentShader-BW nil)
(def shaderProgram nil)
(def testShader_NMap nil)
(def testCube_3D nil)

(defn create []
  (def vertexShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/vertex.glsl")))
  (def fragmentShader-BW (.readString (.internal (Gdx/files) "src/pixel_perversion_engine/shader/greyscale/fragment.glsl")))
  (def shaderProgram (new ShaderProgram vertexShader-BW fragmentShader-BW))
  (spine-r/setup-render)
  (let [root (root 800 480)
        ;attach game
        root (game root) ;(attach-object root [:game] (game root))
        ;attach map-editor
        root (map-editor/map-editor root)
        ;attach root
        root (main-menu root) ;(attach-object game-attached [:main-menu] (main-menu root))

        ;attach shaders
        root (assoc root :shaders {:greyscale shaderProgram})
        ;testing viewport zooming
        camera (.getCamera (get-in root [:fit-viewport]))]
    ;(set! (.-zoom camera) 0.5) ;<-- converts screen from 16px to 32px
    (def root-atomic (atom root)))


  ;(set! (.-shaderProgram (.-spineDrawable (get-in @root-atomic [:render]))) shaderProgram)
  ;(set! (.-useShader (.-spineDrawable (get-in @root-atomic [:render]))) true)

  (def testShader_NMap (new TestShader_NMap
                            (new Texture (.internal Gdx/files "resources/cmap/cmap_brickwall.png"))
                            (new Texture (.internal Gdx/files "resources/nmap/nmap_brickwall.png"))))
  (def testCube_3D (new Cube_3D))
  )

(defn render []
  (.glClearColor Gdx/gl 0 0 0 1)
  (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

  (.render testShader_NMap)

  ;update all app logic
  (update! root-atomic proc-path)

  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float 800) (float 480))

  (render-all @root-atomic)

  ;(.render testCube_3D)



  ;BOOKMARK println fps
  ;(println (.getFramesPerSecond Gdx/graphics))
  ;BOOKMARK render fps
  (.begin (get-in @root-atomic [:sprite-batch]))
  (render-text @root-atomic (str (.getFramesPerSecond Gdx/graphics)) 200 200)
  (.end (get-in @root-atomic [:sprite-batch]))

  (.update (get-in @root-atomic [:input]))
  )

(defn resize [width height]
  ;update viewport
  (.update (get-in @root-atomic [:fit-viewport]) (float width) (float height)))
(defn pause [])
(defn resume [])

(defn dispose []
  (let [dispose-seq (for [obj (get-in @root-atomic [:dispose-list])] obj)]
    (map (fn [obj] (.dispose obj)) dispose-seq))

  (spine-r/dispose-render)
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