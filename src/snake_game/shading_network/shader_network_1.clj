(ns snake-game.shading-network.shader-network-1
  (:import [com.badlogic.gdx.graphics Pixmap$Format GL20 Texture$TextureFilter]
           [com.badlogic.gdx.graphics.glutils FrameBuffer]
           [com.badlogic.gdx.graphics.g2d TextureRegion]
           [com.pixel_perversion_engine.render Render]
           [com.badlogic.gdx Gdx]))

;(def fbo-combine (new FrameBuffer Pixmap$Format/RGBA8888 800 480 false))
;(def fbo-scanline (new FrameBuffer Pixmap$Format/RGBA8888 800 480 false))
;(def fbo-scale (new FrameBuffer Pixmap$Format/RGBA8888 800 480 false))

(defn shader-network-1
  [{:keys [shaders fbos sprite-batch fit-viewport] :as root} fbos1 layers-sorted-keys]
  (let [viewport-width (.getWorldWidth fit-viewport)
        viewport-height (.getWorldHeight fit-viewport)
        render-position-x (float (/ (* -1 viewport-width) 2))
        render-position-y (float (/ (* -1 viewport-height) 2))
        fbo-combine (:passthrough fbos)
        fbo-scanline (:scanline fbos)
        fbo-scale (:greyscale fbos)

        ]

      ;BOOKMARK combine all layers
      (.begin fbo-combine)
      (.glClearColor Gdx/gl 0 0 0 0)
      (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

      (doseq [li layers-sorted-keys]
        (let [texture (.getColorBufferTexture (fbos1 li))]
          (.setShader sprite-batch (:normal shaders))
          (.begin sprite-batch)
          (.glViewport Gdx/gl20 0, 0, viewport-width, viewport-height)
          (.draw sprite-batch texture render-position-x render-position-y)
          (.end sprite-batch)
          ))
      (.end fbo-combine)

      ;BOOKMARK scale : 2
      (.begin fbo-scale)
      (.glClearColor Gdx/gl 0 0 0 0)
      (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

      (let [texture (.getColorBufferTexture fbo-combine)]
        ;shader.setUniformf("scalefac", 2);
        (.setFilter texture Texture$TextureFilter/Nearest Texture$TextureFilter/Nearest)
        (.setShader sprite-batch (:scale shaders))
        (.begin (:scale shaders))
        (.glViewport Gdx/gl20 0, 0, viewport-width, viewport-height)
        (.setUniformf (:scale shaders) "scalefac" (float 2.0))
        (.end (:scale shaders))
        (.begin sprite-batch)
        (.draw sprite-batch texture render-position-x render-position-y)
        (.end sprite-batch)
        )
      (.end fbo-scale)

      ;BOOKMARK scanline
      (.begin fbo-scanline)
      (.glClearColor Gdx/gl 0 0 0 0)
      (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

      (let [texture (.getColorBufferTexture fbo-scale)]
        (.setShader sprite-batch (:scanline shaders))
        (.begin sprite-batch)
        (.glViewport Gdx/gl20 0, 0, viewport-width, viewport-height)
        (.draw sprite-batch texture render-position-x render-position-y); (float 0.0) (float 0.0)
        (.end sprite-batch)
        )
      (.end fbo-scanline)

      ;BOOKMARK scale :
      ;(comment
        (.begin fbo-scale)
        (.glClearColor Gdx/gl 0 0 0 0)
        (.glClear Gdx/gl GL20/GL_COLOR_BUFFER_BIT)

        (let [texture (.getColorBufferTexture fbo-scanline)
              ]
          ;(.flip t-r false true)
          ;shader.setUniformf("scalefac", 2);
          (.setFilter texture Texture$TextureFilter/Nearest Texture$TextureFilter/Nearest)
          (.setShader sprite-batch (:scale shaders))
          (.begin (:scale shaders))
          (.glViewport Gdx/gl20 0, 0, viewport-width, viewport-height)
          (.setUniformf (:scale shaders) "scalefac" (float 2.0))
          (.end (:scale shaders))
          (.begin sprite-batch)
          (.draw sprite-batch texture render-position-x render-position-y)
          (.end sprite-batch)
          )
        (.end fbo-scale)
        ;)

      ;BOOKMARK final render
      (let [texture (.getColorBufferTexture fbo-scale) ; fbo-combine fbo-scanline fbo-scale
            t-r (get-in root [:fbo-textureRegion])
            ;t-r (new TextureRegion texture)
            ]
        (.setRegion t-r texture)
        (if (not (.isFlipY t-r)) (.flip t-r false true))
        (.begin sprite-batch)
        (.glViewport Gdx/gl20 0, 0, viewport-width, viewport-height)
        ;RESET SHADER!
        (.setShader sprite-batch nil)
        (.draw sprite-batch t-r render-position-x render-position-y) ; (float (/ -800.0 2)) (float (/ -480.0 2)) (float 0.0) (float 0.0)
        (.end sprite-batch)
        )

    ))